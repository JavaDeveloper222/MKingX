package me.galtap.mkingx;

import me.galtap.mkingx.api.VaultAPI;
import me.galtap.mkingx.api.placeholder.ConfigPlaceholder;
import me.galtap.mkingx.api.placeholder.PlayerPlaceholder;
import me.galtap.mkingx.command.GameCMD;
import me.galtap.mkingx.core.KingGame;
import me.galtap.mkingx.event.EventListener;
import me.galtap.mkingx.event.GuiManager;
import me.galtap.mkingx.core.MainGUI;
import me.galtap.mkingx.loader.DataLoader;
import me.galtap.mkingx.loader.impl.DatabaseLoader;
import me.galtap.mkingx.loader.impl.FileLoader;
import me.galtap.mkingx.settings.ConfigSettings;
import me.galtap.mkingx.util.Cooldown;
import me.galtap.mkingx.util.LoggerManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class MKingX extends JavaPlugin {
    private static MKingX instance;
    private DatabaseLoader databaseLoader;
    private VaultAPI vaultAPI;

    @Override
    public void onEnable() {
        instance = this;
        ConfigSettings configSettings = new ConfigSettings();
        DataLoader dataLoader = createLoader(configSettings);
        loadPlugins(configSettings,dataLoader);

        KingGame kingGame = new KingGame(configSettings,dataLoader,vaultAPI);
        GuiManager guiManager = new GuiManager();
        Bukkit.getPluginManager().registerEvents(new EventListener(guiManager),this);
        MainGUI mainGUI = new MainGUI(configSettings,dataLoader,guiManager);
        new GameCMD(configSettings,kingGame,mainGUI);

        Cooldown.create();
        kingGame.start();

    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        if(databaseLoader != null){
            databaseLoader.closeConnection();
        }
    }

    public static MKingX getInstance() {
        return instance;
    }
    private DataLoader createLoader(ConfigSettings settings){
        if(settings.isMySQLEnabled()){
            databaseLoader = new DatabaseLoader(settings.getHost(),settings.getPort(),settings.getRoot(),settings.getPassword(),settings.getDatabaseName());
            return databaseLoader;
        }
        return new FileLoader();
    }
    private void loadPlugins(ConfigSettings settings,DataLoader loader){
        vaultAPI = new VaultAPI();
        if(!vaultAPI.isEnabled()){
            LoggerManager.VAULT_LOAD.logJustError();
        }

        Plugin plugin = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        if(plugin == null || !plugin.isEnabled()){
            LoggerManager.PLACEHOLDER_LOAD.logJustError();
        } else{
            new ConfigPlaceholder(settings).register();
            new PlayerPlaceholder(loader).register();
        }
    }
}
