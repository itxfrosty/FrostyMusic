package me.itxfrosty.frostymusic.audio.sources.applemusic.entites.search;

import lombok.Data;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.album.Albums;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.artist.Artists;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.curator.Curators;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.playlist.PlayLists;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.song.Songs;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.station.Stations;

@Data
public class Search {

    private Albums albums;

    private Songs songs;

    private Artists artists;

    private Curators curators;

    private PlayLists playLists;

    private Stations stations;
}