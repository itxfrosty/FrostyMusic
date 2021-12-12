package me.itxfrosty.frostymusic.audio.sources.applemusic.entites;

import lombok.Data;

@Data
public class Track {

    private AtributesTrack attributes;

    private String href;

    private String id;

    private String type;
}