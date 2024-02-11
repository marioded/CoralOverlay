package tech.zmario.coraloverlay.objects;

import com.google.gson.annotations.SerializedName;

public class StatsUser {

    @SerializedName("bedwars")
    private BedWarsUser bedWarsUser;

    public BedWarsUser getBedWarsUser() {
        return bedWarsUser;
    }

    @Override
    public String toString() {
        return "StatsUser{" +
                "bedWarsUser=" + bedWarsUser +
                '}';
    }
}
