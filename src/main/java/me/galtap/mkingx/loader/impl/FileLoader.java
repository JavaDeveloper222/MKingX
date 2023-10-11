package me.galtap.mkingx.loader.impl;

import me.galtap.mkingx.MKingX;
import me.galtap.mkingx.loader.DataLoader;
import me.galtap.mkingx.model.Winner;
import me.galtap.mkingx.util.CustomFile;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FileLoader implements DataLoader {
    private CustomFile dataFile;
    public FileLoader(){
        createIfNotExists();
    }
    @Override
    public void setWinner(String uuid, String name, int winners, long prizes) {
        createIfNotExists();
        String winPath = uuid+".winners";
        String prizePath = uuid+".prizes";
        ConfigurationSection section = dataFile.getSection();
        section.set(uuid+".name",name);

        int allWinners = section.getInt(winPath,0);
        allWinners = winners+allWinners;
        section.set(winPath,allWinners);

        long allPrizes = section.getLong(prizePath,0);
        allPrizes = prizes+allPrizes;
        section.set(prizePath,allPrizes);

        dataFile.save();
        dataFile.reload();
    }

    @Override
    public Winner getWinner(String uuid) {
        createIfNotExists();
        ConfigurationSection section = dataFile.getSection();
        String name = section.getString(uuid+".name");
        int winners = section.getInt(uuid+".winners");
        long prizes = section.getLong(uuid+".prizes");
        return new Winner(uuid,name,winners,prizes);
    }

    @Override
    public List<Winner> getAllWinners() {
        List<Winner> winners = new ArrayList<>();
        createIfNotExists();
        ConfigurationSection section = dataFile.getSection();
        for(String uuid: section.getKeys(false)){
            String name = section.getString(uuid+".name");
            int allWinners = section.getInt(uuid+".winners",0);
            long allPrizes = section.getLong(uuid+".prizes",0);
            Winner winner = new Winner(uuid,name,allWinners,allPrizes);
            winners.add(winner);
        }
        if(!winners.isEmpty()){
            winners.sort(Comparator.comparingInt(Winner::getWinnings));
        }
        return winners;
    }

    private void createIfNotExists(){
        if(dataFile == null || !dataFile.getFile().getName().equals("data.yml") || dataFile.getFile().isDirectory()) {
            dataFile = new CustomFile("data.yml", MKingX.getInstance());
            dataFile.save();
        }
    }
}
