package me.itxfrosty.frostymusic.audio.sources.applemusic.entites;

import lombok.Data;

@Data
public class AtributesTrack {

    private String artistName;

    private String composerName;

    private int discNumber;

    private long durationInMillis;

    private String isrc;

    private String name;

    private String releaseDate;

    private int trackNumber;

    private String url;
}
