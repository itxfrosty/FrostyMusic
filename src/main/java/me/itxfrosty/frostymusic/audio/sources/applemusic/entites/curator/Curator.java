package me.itxfrosty.frostymusic.audio.sources.applemusic.entites.curator;

import lombok.Data;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.artist.ArtistData;

import java.util.List;

@Data
public class Curator {

    private List<ArtistData> data;

    private String href;

    public ArtistData getCurator() {
        if (data != null && data.size() > 0) {
            return data.get(0);
        } else {
            return null;
        }
    }
}