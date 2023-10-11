package me.galtap.mkingx.api.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.galtap.mkingx.loader.DataLoader;
import me.galtap.mkingx.model.Winner;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlayerPlaceholder extends PlaceholderExpansion {
    private final DataLoader dataLoader;

    public PlayerPlaceholder(DataLoader dataLoader){
        this.dataLoader = dataLoader;
    }
    @Override
    public @NotNull String getIdentifier() {
        return "winner";
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
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if(player == null){
            return null;
        }
        Winner winner = dataLoader.getWinner(player.getUniqueId().toString());
        if(winner == null){
            return null;
        }

        if(params.equals("top")){
            List<Winner> winners = dataLoader.getAllWinners();
            int index = winners.indexOf(winner);
            if (index != -1) {
                return String.valueOf(index + 1);
            }
        }
        if(params.equals("name")){
            return winner.getName();
        }
        if(params.equals("count")){
            return String.valueOf(winner.getWinnings());
        }
        if(params.equals("balance")){
            return String.valueOf(winner.getPrizes());
        }
        if(params.equals("uuid")){
            return String.valueOf(winner.getUuid());
        }
        return null;
    }
}
