package tech.zmario.coraloverlay.objects;

import com.google.gson.annotations.SerializedName;

public class BedWarsUser {

    @SerializedName("final_kills")
    private int finalKills;

    @SerializedName("final_deaths")
    private int finalDeaths;

    @SerializedName("wins")
    private int wins;

    @SerializedName("winstreak")
    private int winStreak;

    @SerializedName("played")
    private int played;

    @SerializedName("level")
    private int level;

    public int getFinalKills() {
        return finalKills;
    }

    public int getFinalDeaths() {
        return finalDeaths;
    }

    public int getWins() {
        return wins;
    }

    public int getWinStreak() {
        return winStreak;
    }

    public int getPlayed() {
        return played;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "BedWarsUser{" +
                ", finalKills=" + finalKills +
                ", finalDeaths=" + finalDeaths +
                ", wins=" + wins +
                ", winStreak=" + winStreak +
                ", played=" + played +
                '}';
    }
}
