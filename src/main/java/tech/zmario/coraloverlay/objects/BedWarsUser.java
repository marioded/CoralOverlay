package tech.zmario.coraloverlay.objects;

import com.google.gson.annotations.SerializedName;

public class BedWarsUser {

    @SerializedName("final_kills")
    private int finalKills = 0;

    @SerializedName("final_deaths")
    private int finalDeaths = 0;

    @SerializedName("wins")
    private int wins = 0;

    @SerializedName("winstreak")
    private int winStreak = 0;

    @SerializedName("played")
    private int played = 0;

    @SerializedName("level")
    private int level = 0;

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
