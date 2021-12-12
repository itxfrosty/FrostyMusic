package me.itxfrosty.frostymusic.audio.sources.applemusic.entites.musicvideo;

import java.util.List;

import lombok.Data;

@Data
public class MusicVideo {

    private List<MusicVideoData> data;

    public MusicVideoData getMusicVideo() {
        if (data != null && data.size() > 0) {
            return data.get(0);
        } else {
            return null;
        }
    }
}