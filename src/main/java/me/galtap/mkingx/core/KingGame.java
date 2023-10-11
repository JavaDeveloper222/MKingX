package me.galtap.mkingx.core;

import me.clip.placeholderapi.PlaceholderAPI;
import me.galtap.mkingx.MKingX;
import me.galtap.mkingx.api.VaultAPI;
import me.galtap.mkingx.loader.DataLoader;
import me.galtap.mkingx.model.Winner;
import me.galtap.mkingx.settings.ConfigSettings;
import me.galtap.mkingx.util.Cooldown;
import me.galtap.mkingx.util.LoggerManager;
import me.galtap.mkingx.util.SimpleUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BoundingBox;

import java.util.List;

public class KingGame {
    private final ConfigSettings configSettings;
    private final DataLoader dataLoader;
    private final VaultAPI vaultAPI;

    private boolean isWaiting = true;
    private boolean isStarted;

    private Player kingNow;

    private int remainingTime;
    private BukkitTask task;

    public KingGame(ConfigSettings configSettings, DataLoader dataLoader, VaultAPI vaultAPI){

        this.configSettings = configSettings;
        this.dataLoader = dataLoader;
        this.vaultAPI = vaultAPI;
    }
    public void start(){
        Location location = configSettings.getLocation();
        if(location == null){
            LoggerManager.LOCATION_NULL.logJustError();
            return;
        }
        if(task == null || task.isCancelled()){
            startGame(location);
            return;
        }
        task.cancel();
        Cooldown.reset("kingGame");
        startGame(location);
    }

    private void startGame(Location center){
        BoundingBox boundingBox = SimpleUtil.createCubZone(center,configSettings.getRadius());
        isWaiting = true;
        isStarted = false;
        task = Bukkit.getScheduler().runTaskTimer(MKingX.getInstance(), () -> {
            List<Player> playersInZone = SimpleUtil.getPlayersInZone(boundingBox,center);
            playersInZone.removeIf(this::isCheater);
            Player newKing = SimpleUtil.getPlayersInCenter(center,playersInZone);
            if(newKing == null || !newKing.isOnline()){
                remainingTime = configSettings.getCooldown();
                return;
            }
            Winner winner = dataLoader.getWinner(newKing.getUniqueId().toString());
            if(winner == null){
                return;
            }
            if(isWaiting){
                waitingProcess(newKing,playersInZone.size());
                return;
            }
            if(isStarted){
                startingProcess(newKing,newKing);
            }

        },0,20);
    }

    private void startingProcess(Player newKing, Player player){
        if(kingNow == null || !kingNow.getUniqueId().toString().equals(newKing.getUniqueId().toString())){
            remainingTime = configSettings.getCooldown();
            kingNow = newKing;
            return;
        }
        if(remainingTime <= 0){
            remainingTime = configSettings.getCooldown();
            Cooldown.setCooldown("kingGame",configSettings.getReCooldown());
            int money = (int) SimpleUtil.rndInt(configSettings.getMinPriceRandom(),configSettings.getMaxPriceRandom());
            dataLoader.setWinner(newKing.getUniqueId().toString(),newKing.getName(),1,money);
            SimpleUtil.throwPlayer(player,configSettings.getDiscarding());

            vaultAPI.giveMoney(newKing,money);

            sendPlayerMessage(newKing,money);
            sendGlobalMessage(money,player);

            kingNow = null;
            isWaiting = true;
            isStarted = false;
            return;
        }
        String text = configSettings.getTimerMsg();
        text = SimpleUtil.replace(text,String.valueOf(remainingTime),"%winner_wait%");
        text = replaceMainPlaceholders(text,player);
        player.sendMessage(text);
        remainingTime--;
    }
    private void waitingProcess(Player newKing, int playerZoneSize){
        if(Cooldown.isHasCooldown("kingGame")){
            String text = configSettings.getCooldownMsg();
            text = replaceMainPlaceholders(text,newKing);
            newKing.sendMessage(text);
            SimpleUtil.throwPlayer(newKing,configSettings.getDiscarding());
            return;
        }
        if(playerZoneSize < configSettings.getMinPlayers()){
            String text = configSettings.getNotFountPlayerMsg();
            text = replaceMainPlaceholders(text,newKing);
            text = SimpleUtil.replace(text,String.valueOf(playerZoneSize),"%winner_playersNow%");
            newKing.sendMessage(text);
            return;
        }
        isStarted = true;
        isWaiting = false;
    }

    private boolean isCheater(Player player){
        if(player == null){
            return false;
        }
        if(player.getGameMode() == GameMode.CREATIVE && configSettings.isGameModeIsCheat()){
            return true;
        }
        boolean vanishIsCheat = configSettings.isVanishIsCheat();
        if(player.getGameMode() == GameMode.SPECTATOR && vanishIsCheat){
            return true;
        }
        if(player.isFlying() && configSettings.isFlyIsCheat()){
            return true;
        }
        for(PotionEffect effect: player.getActivePotionEffects()){
            if(effect.getType() == PotionEffectType.INVISIBILITY && vanishIsCheat){
                return true;
            }
        }
        return false;
    }
    private void sendPlayerMessage(Player player, int price){
        String titleText = configSettings.getWinTitleText();
        String subText = configSettings.getWinTitleSubtext();
        if(titleText != null){
            titleText = replaceMainPlaceholders(titleText,player);
            titleText = SimpleUtil.replace(titleText,String.valueOf(price),"%winner_randomPrice%");
        }
        if(subText != null){
            subText = replaceMainPlaceholders(subText,player);
            subText = SimpleUtil.replace(subText,String.valueOf(price),"%winner_randomPrice%");
        }
        player.sendTitle(titleText,subText,configSettings.getWinTitleTime1(),configSettings.getWinTitleTime2(),configSettings.getWinTitleTime3());
        List<String> lines = configSettings.getPlayerMsgList();
        lines.forEach(text -> {
            String modifiedText = replaceMainPlaceholders(text,player,price);
            player.sendMessage(modifiedText);
        });
    }
    private void sendGlobalMessage(int money, Player player){
        List<String> lines = configSettings.getGlobalMsgList();
        lines.forEach(text ->{
            String modifiedText = replaceMainPlaceholders(text,player,money);
            for(Player online: Bukkit.getOnlinePlayers()){
                online.sendMessage(modifiedText);
            }
        });
    }
    private static String replaceMainPlaceholders(String text, Player player){
        text = PlaceholderAPI.setPlaceholders(player,text);
        text = PlaceholderAPI.setPlaceholders(null,text);
        text = SimpleUtil.replace(text,String.valueOf(Cooldown.getDelay()),"%winner_cooldown%");
        return text;
    }
    private static String replaceMainPlaceholders(String text, Player player, int price){
        String modifiedText = text;
        modifiedText = replaceMainPlaceholders(modifiedText,player);
        modifiedText = SimpleUtil.replace(modifiedText, String.valueOf(price), "%winner_randomPrice%");
        return modifiedText;
    }
}
