package me.itxfrosty.frostymusic.audio.sources.applemusic.entites.playlist;

import lombok.Data;

import java.util.List;

@Data
public class PlayList {

    private List<PlayListData> data;

    public PlayListData getPlayList() {
        if (data != null && data.size() > 0) {
            return data.get(0);
        } else {
            return null;
        }
    }
}