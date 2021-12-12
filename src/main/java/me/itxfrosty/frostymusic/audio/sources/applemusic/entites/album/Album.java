package me.itxfrosty.frostymusic.audio.sources.applemusic.entites.album;

import lombok.Data;

import java.util.List;

@Data
public class Album {

    private List<AlbumData> data;

    public AlbumData getAlbum() {
        if (data != null && data.size() > 0) {
            return data.get(0);
        } else {
            return null;
        }
    }
}