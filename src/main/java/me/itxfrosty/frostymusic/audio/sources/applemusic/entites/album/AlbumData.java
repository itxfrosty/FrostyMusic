package me.itxfrosty.frostymusic.audio.sources.applemusic.entites.album;

import lombok.Data;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.Attributes;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.Relationships;

@Data
public class AlbumData {

    private Attributes attributes;

    private String href;

    private String id;

    private Relationships relationships;

    private String type;
}