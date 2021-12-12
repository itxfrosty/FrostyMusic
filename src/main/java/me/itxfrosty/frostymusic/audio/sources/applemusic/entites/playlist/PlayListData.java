package me.itxfrosty.frostymusic.audio.sources.applemusic.entites.playlist;

import lombok.Data;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.Relationships;

@Data
public class PlayListData {

    private AttributesPlayList attributes;

    private String href;

    private String id;

    private Relationships relationships;

    private String type;
}