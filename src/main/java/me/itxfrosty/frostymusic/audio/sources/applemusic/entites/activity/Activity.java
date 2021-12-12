package me.itxfrosty.frostymusic.audio.sources.applemusic.entites.activity;

import lombok.Data;

import java.util.List;

@Data
public class Activity {

    private List<ActivityData>  data;

    private String href;

    public ActivityData getActivity() {
        if (data != null && data.size() > 0) {
            return data.get(0);
        } else {
            return null;
        }
    }
}