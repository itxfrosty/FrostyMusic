package me.itxfrosty.frostymusic.audio.sources.applemusic.entites.song;

import lombok.Data;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.Attributes;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.Relationships;

/**
 * Created by aaronasencio on 9/1/18.
 */

@Data
public class SongData {

    private Attributes attributes;

    private String href;

    private String id;

    private Relationships relationships;

    private String type;
}