package me.galtap.mkingx.api;

import me.galtap.mkingx.util.LoggerManager;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultAPI {
    private final boolean isEnabled;
    private Economy economy;
    public VaultAPI(){
        isEnabled = loadPlugin();
    }
    public final boolean loadPlugin(){
        Plugin plugin = Bukkit.getPluginManager().getPlugin("Vault");
        if(plugin == null || !plugin.isEnabled()){
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if(rsp == null){
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }
    public void giveMoney(Player player, double count){
        if(player == null || count < 0 || !isEnabled){
            LoggerManager.VAULT_ERROR.sendPlayerMessage(player);
            return;
        }
        EconomyResponse response = economy.depositPlayer(player,count);
        if(!response.transactionSuccess()){
            LoggerManager.VAULT_ERROR.sendPlayerMessage(player);
            player.sendMessage(response.errorMessage);
        }
    }
    public boolean isEnabled(){
        return isEnabled;
    }
}
