package me.itxfrosty.frostymusic.audio.sources.applemusic.entites;

import lombok.Data;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.album.Albums;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.artist.Artist;

@Data
public class Relationships {

    private Artist artists;

    private Tracks tracks;

    private Albums albums;
}