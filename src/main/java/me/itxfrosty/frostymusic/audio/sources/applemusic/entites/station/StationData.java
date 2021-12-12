package me.itxfrosty.frostymusic.audio.sources.applemusic.entites.station;

import lombok.Data;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.Attributes;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.Relationships;

@Data
public class StationData {

    private Attributes attributes;

    private String href;

    private String id;

    private Relationships relationships;

    private String type;
}