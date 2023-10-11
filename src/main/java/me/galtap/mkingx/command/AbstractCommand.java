package me.galtap.mkingx.command;

import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractCommand implements CommandExecutor, TabCompleter {
    protected AbstractCommand(String command, JavaPlugin plugin){
        PluginCommand pluginCommand = plugin.getCommand(command);
        if(pluginCommand != null){
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);
        }
    }
    public abstract void execute(CommandSender sender, String label, String[] args);
    public List<String> complete(CommandSender sender,String[] args){
        return null;
    }
    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        execute(sender,label,args);
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return complete(sender,args);
    }
}
