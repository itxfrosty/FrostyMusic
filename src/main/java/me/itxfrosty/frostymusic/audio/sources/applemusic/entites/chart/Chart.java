package me.itxfrosty.frostymusic.audio.sources.applemusic.entites.chart;

import lombok.Data;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.musicvideo.MusicVideoData;

import java.util.List;

@Data
public class Chart {

    private List<AlbumsData> albums;

    private List<SongsData> songs;

    private List<MusicVideoData> musicVideos;
}