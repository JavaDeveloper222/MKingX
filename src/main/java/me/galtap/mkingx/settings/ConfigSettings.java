package me.galtap.mkingx.settings;

import me.galtap.mkingx.MKingX;
import me.galtap.mkingx.util.ErrorHandle;
import me.galtap.mkingx.util.SimpleUtil;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
public class ConfigSettings {
    private final boolean mySQLEnabled;

    private final String host;
    private final int port;
    private final String root;
    private final String password;
    private final String databaseName;


    private final int minPlayers;
    private final int radius;
    private final int cooldown;
    private final int reCooldown;
    private final double discarding;


    private final boolean gameModeIsCheat;
    private final boolean flyIsCheat;
    private final boolean vanishIsCheat;


    private final String inventoryDisplayName;
    private final String backPageName;
    private final String nextPageName;
    private final String closeName;
    private final String itemName;
    private final List<String> itemLore;


    private final String notFountPlayerMsg;
    private final String cooldownMsg;
    private final String timerMsg;
    private final List<String> playerMsgList;
    private final List<String> globalMsgList;


    private final String winTitleText;
    private final String winTitleSubtext;
    private final int winTitleTime1;
    private final int winTitleTime2;
    private final int winTitleTime3;


    private int minPriceRandom;
    private int maxPriceRandom;


    private Location location;

    public ConfigSettings(){
        JavaPlugin plugin = MKingX.getInstance();
        plugin.saveDefaultConfig();
        ConfigurationSection section = plugin.getConfig();
        ErrorHandle errorHandle = new ErrorHandle("config.yml",plugin);


        mySQLEnabled = section.getBoolean("SQL.mySQL");
        host = section.getString("SQL.host");
        port = section.getInt("SQL.port");
        root = section.getString("SQL.root");
        password = section.getString("SQL.password");
        databaseName = section.getString("SQL.databaseName");


        minPlayers = errorHandle.check(section,1,"King.minPlayer");
        radius = errorHandle.check(section,5,"King.radius");
        cooldown = errorHandle.check(section,15,"King.cooldown");
        reCooldown = errorHandle.check(section,60,"King.reCooldown");
        discarding = errorHandle.check(section,1.0,"King.discarding");


        gameModeIsCheat = section.getBoolean("CheatsProtect.gameMode");
        flyIsCheat = section.getBoolean("CheatsProtect.fly");
        vanishIsCheat = section.getBoolean("CheatsProtect.vanish");


        inventoryDisplayName = SimpleUtil.getColorText(errorHandle.check(section,"null","GUI.displayName"));
        backPageName = SimpleUtil.getColorText(errorHandle.check(section,"null","GUI.backPage"));
        nextPageName = SimpleUtil.getColorText(errorHandle.check(section,"null","GUI.nextPage"));
        closeName = SimpleUtil.getColorText(errorHandle.check(section,"null","GUI.close"));
        itemName = SimpleUtil.getColorText(errorHandle.check(section,"null","GUI.itemName"));
        itemLore = SimpleUtil.getColorText(section.getStringList("GUI.itemLore"));


        notFountPlayerMsg = SimpleUtil.getColorText(errorHandle.check(section,"null","EveryWinPrize.notFoundPlayer"));
        cooldownMsg = SimpleUtil.getColorText(errorHandle.check(section,"null","EveryWinPrize.cooldownMessage"));
        timerMsg = SimpleUtil.getColorText(errorHandle.check(section,"null","EveryWinPrize.timerMessage"));
        playerMsgList = SimpleUtil.getColorText(section.getStringList("EveryWinPrize.player"));
        globalMsgList = SimpleUtil.getColorText(section.getStringList("EveryWinPrize.global"));


        winTitleText = SimpleUtil.getColorText(section.getString("EveryWinPrize.winnerTitle.text"));
        winTitleSubtext = SimpleUtil.getColorText(section.getString("EveryWinPrize.winnerTitle.subtext"));
        winTitleTime1 = errorHandle.check(section,30,"EveryWinPrize.winnerTitle.time1");
        winTitleTime2 = errorHandle.check(section,60,"EveryWinPrize.winnerTitle.time2");
        winTitleTime3 = errorHandle.check(section,30,"EveryWinPrize.winnerTitle.time3");


        maxPriceRandom = errorHandle.check(section,500,"Random.max");
        minPriceRandom = errorHandle.check(section,100,"Random.min");
        if(maxPriceRandom < minPriceRandom){
            maxPriceRandom ^= minPriceRandom;
            minPriceRandom ^= maxPriceRandom;
            maxPriceRandom ^= minPriceRandom;
        }



        location = section.getLocation("Location");
    }
    public void setLocation(Location location){
        ConfigurationSection section = MKingX.getInstance().getConfig();
        section.set("Location",location);
        MKingX.getInstance().saveConfig();
        this.location = location;
    }

    public boolean isMySQLEnabled() {
        return mySQLEnabled;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getRoot() {
        return root;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getRadius() {
        return radius;
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getReCooldown() {
        return reCooldown;
    }

    public double getDiscarding() {
        return discarding;
    }

    public boolean isGameModeIsCheat() {
        return gameModeIsCheat;
    }

    public boolean isFlyIsCheat() {
        return flyIsCheat;
    }

    public boolean isVanishIsCheat() {
        return vanishIsCheat;
    }

    public String getInventoryDisplayName() {
        return inventoryDisplayName;
    }

    public String getBackPageName() {
        return backPageName;
    }

    public String getNextPageName() {
        return nextPageName;
    }

    public String getItemName() {
        return itemName;
    }

    public List<String> getItemLore() {
        return List.copyOf(itemLore);
    }

    public String getNotFountPlayerMsg() {
        return notFountPlayerMsg;
    }

    public String getCooldownMsg() {
        return cooldownMsg;
    }

    public String getTimerMsg() {
        return timerMsg;
    }

    public List<String> getPlayerMsgList() {
        return List.copyOf(playerMsgList);
    }

    public List<String> getGlobalMsgList() {
        return List.copyOf(globalMsgList);
    }

    public String getWinTitleText() {
        return winTitleText;
    }

    public String getWinTitleSubtext() {
        return winTitleSubtext;
    }

    public int getWinTitleTime1() {
        return winTitleTime1;
    }

    public int getWinTitleTime2() {
        return winTitleTime2;
    }

    public int getWinTitleTime3() {
        return winTitleTime3;
    }

    public int getMinPriceRandom() {
        return minPriceRandom;
    }

    public int getMaxPriceRandom() {
        return maxPriceRandom;
    }

    public Location getLocation() {
        return location;
    }

    public String getCloseName() {
        return closeName;
    }
}
