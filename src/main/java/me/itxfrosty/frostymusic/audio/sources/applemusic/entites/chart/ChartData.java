package me.itxfrosty.frostymusic.audio.sources.applemusic.entites.chart;

import lombok.Data;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.Attributes;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.Relationships;

@Data
public class ChartData {

    private String href;

    private String id;

    private String type;

    private Attributes attributes;

    private Relationships relationships;
}