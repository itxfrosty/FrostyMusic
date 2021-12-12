package me.itxfrosty.frostymusic.audio.sources.applemusic.entites.genre;

import lombok.Data;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.Attributes;

@Data
public class GenreData {

    private Attributes attributes;

    private String href;

    private String id;

    private String type;
}