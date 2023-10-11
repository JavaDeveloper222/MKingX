package me.galtap.mkingx.loader;

import me.galtap.mkingx.model.Winner;

import java.util.List;

public interface DataLoader {
    void setWinner(String uuid, String name, int winners, long prizes);
    Winner getWinner(String uuid);
    List<Winner> getAllWinners();

}
