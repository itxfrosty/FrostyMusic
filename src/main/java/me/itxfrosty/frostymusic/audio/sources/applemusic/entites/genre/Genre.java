package me.itxfrosty.frostymusic.audio.sources.applemusic.entites.genre;

import java.util.List;

import lombok.Data;

@Data
public class Genre {

    private List<GenreData> data;

    public GenreData getGenre() {
        if (data != null && data.size() > 0) {
            return data.get(0);
        } else {
            return null;
        }
    }
}