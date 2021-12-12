package me.itxfrosty.frostymusic.audio.sources.applemusic.entites.song;

import lombok.Data;
import java.util.List;

@Data
public class Song {

    private List<SongData> data;

    public SongData getSong() {
        if (data != null && data.size() > 0) {
            return data.get(0);
        } else {
            return null;
        }
    }
}