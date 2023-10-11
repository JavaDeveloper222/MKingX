package me.galtap.mkingx.api.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.galtap.mkingx.settings.ConfigSettings;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ConfigPlaceholder extends PlaceholderExpansion {
    private final ConfigSettings configSettings;

    public ConfigPlaceholder(ConfigSettings configSettings){

        this.configSettings = configSettings;
    }
    @Override
    public @NotNull String getIdentifier() {
        return "config";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Galtap";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }
    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if(configSettings == null){
            return null;
        }
        if(params.equals("minimumPlayers")){
            return String.valueOf(configSettings.getMinPlayers());
        }
        if(params.equals("maximumCooldown")){
            return String.valueOf(configSettings.getCooldown());
        }
        if(params.equals("minimumPrice")){
            return String.valueOf(configSettings.getMinPriceRandom());
        }
        if(params.equals("maximumPrice")){
            return String.valueOf(configSettings.getMaxPriceRandom());
        }
        return null;
    }
}
