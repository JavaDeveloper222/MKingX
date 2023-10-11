package me.galtap.mkingx.model;


import java.util.Objects;

public class Winner {
     private final String uuid;

     private final String name;

     private final int winnings;

     private final long prizes;


    public Winner(String uuid, String name, int winnings, long prizes) {
        this.uuid = uuid;
        this.name = name;
        this.winnings = winnings;
        this.prizes = prizes;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public int getWinnings() {
        return winnings;
    }

    public long getPrizes() {
        return prizes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Winner winner = (Winner) o;
        return Objects.equals(uuid, winner.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
