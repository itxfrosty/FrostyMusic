package me.itxfrosty.frostymusic.audio.sources.applemusic.entites;

import java.util.List;

import lombok.Data;

@Data
public class Tracks {

    private List<Track> data;

    private String href;
}