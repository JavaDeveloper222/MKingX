package me.galtap.mkingx.util;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class CustomFile {
    private final FileConfiguration section;
    private final File file;

    public CustomFile(String name, JavaPlugin plugin) {
        File dataFolder = plugin.getDataFolder();

        if (!dataFolder.exists()) {
            boolean created = dataFolder.mkdirs();
            if (!created) {
                LoggerManager.DERICTORY_CREATE_ERROR.logJustError(name);
            }
        }

        file = new File(dataFolder, name);

        if (!file.exists()) {
            try {
                boolean created = file.createNewFile();
                if (!created) {
                    LoggerManager.FILE_CREATE_ERROR.logJustError(name);
                }
            } catch (IOException e) {
                LoggerManager.FILE_CREATE_ERROR.logFatalError(e,name);
            }
        }

        section = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getSection() {
        return section;
    }

    public void save() {
        try {
            section.save(file);
        } catch (IOException e) {
            LoggerManager.FILE_SAVE_ERROR.logFatalError(e,file.getName());
        }
    }

    public void reload() {
        try {
            section.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            LoggerManager.FILE_RELOAD_ERROR.logFatalError(e,file.getName());
        }
    }

    public File getFile() {
        return file;
    }
}


