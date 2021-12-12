package me.itxfrosty.frostymusic.audio.sources.applemusic.entites.station;

import lombok.Data;

import java.util.List;

@Data
public class Station {

    private List<StationData> data;

    public StationData getStation() {
        if (data != null && data.size() > 0) {
            return data.get(0);
        } else {
            return null;
        }
    }
}