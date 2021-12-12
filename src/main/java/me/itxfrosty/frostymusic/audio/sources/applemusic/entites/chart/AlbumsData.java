package me.itxfrosty.frostymusic.audio.sources.applemusic.entites.chart;

import lombok.Data;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.album.AlbumData;

import java.util.List;

@Data
public class AlbumsData {

    private String chart;

    private List<AlbumData> data;

    private String href;

    private String name;

    private String next;
}