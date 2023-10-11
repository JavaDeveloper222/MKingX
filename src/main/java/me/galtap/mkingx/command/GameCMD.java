package me.galtap.mkingx.command;

import me.galtap.mkingx.MKingX;
import me.galtap.mkingx.core.KingGame;
import me.galtap.mkingx.core.MainGUI;
import me.galtap.mkingx.settings.ConfigSettings;
import me.galtap.mkingx.util.SimpleUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GameCMD extends AbstractCommand{
    private final ConfigSettings settings;
    private final KingGame kingGame;
    private final MainGUI mainGUI;

    public GameCMD(ConfigSettings settings, KingGame kingGame, MainGUI mainGUI) {
        super("king", MKingX.getInstance());
        this.settings = settings;
        this.kingGame = kingGame;

        this.mainGUI = mainGUI;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if(!(sender instanceof Player)) return;
        Player player = (Player) sender;
        if(args.length == 1){
            if(args[0].equalsIgnoreCase("set")){
                if(!SimpleUtil.isHavePermission("king.game.set",player)){
                    player.sendMessage(ChatColor.RED+"У вас не достаточно прав");
                    return;
                }
                settings.setLocation(player.getLocation());
                player.sendMessage(ChatColor.GREEN+"центр успешно установлен");
                kingGame.start();
                return;
            }
            if(args[0].equalsIgnoreCase("top")){
                if(!SimpleUtil.isHavePermission("king.game.top",player)){
                    player.sendMessage(ChatColor.RED+"У вас не достаточно прав");
                    return;
                }
                mainGUI.open(player);
                return;
            }
        }
        player.sendMessage(ChatColor.RED+"Неправильно введены аргументы");
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args){
        if(!(sender instanceof Player)) return null;
        Player player = (Player) sender;
        if(args.length == 1){
            List<String> commands = new ArrayList<>();
            if(SimpleUtil.isHavePermission("king.game.top",player)){
                commands.add("top");
            }
            if(SimpleUtil.isHavePermission("king.game.set",player)){
                commands.add("set");
            }
            return commands;
        }
        return null;
    }

}
