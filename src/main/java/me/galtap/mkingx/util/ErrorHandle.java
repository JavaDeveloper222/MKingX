package me.galtap.mkingx.util;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class ErrorHandle {
    private final String fileName;
    private final JavaPlugin plugin;

    public ErrorHandle(String fileName, JavaPlugin plugin){

        this.fileName = fileName;
        this.plugin = plugin;
    }

    public int check(ConfigurationSection section, int def, String path){
        int value = section.getInt(path,-1);
        if(value == -1){
            reportError(section.getCurrentPath()+"."+path,def);
            return def;
        }
        return value;
    }
    public double check(ConfigurationSection  section, double def, String path){
        double value = section.getDouble(path,-1.0);
        if(value < 0.0){
            reportError(section.getCurrentPath()+"."+path,def);
            return def;
        }
        return value;
    }
    public String check(ConfigurationSection section, String def,String path){
        String value = section.getString(path);
        if(value == null){
            reportError(section.getCurrentPath()+"."+path,def);
            return def;
        }
        return value;
    }
    public void reportError(String path, Object def) {
        String message = String.format("Обнаружена ошибка в %s. Недопустимое значение в пути: %s. Исправьте ошибку. Переход в первоначальное значение: %s", fileName, path, def);
        plugin.getLogger().severe(message);
    }
}