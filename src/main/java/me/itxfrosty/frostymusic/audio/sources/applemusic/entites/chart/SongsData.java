package me.itxfrosty.frostymusic.audio.sources.applemusic.entites.chart;

import lombok.Data;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.song.SongData;

import java.util.List;

@Data
public class SongsData {

    private String chart;

    private List<SongData> data;

    private String href;

    private String name;

    private String next;
}