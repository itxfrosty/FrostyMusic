package me.itxfrosty.frostymusic.audio.sources.applemusic.entites.applecurators;

import java.util.List;
import lombok.Data;

@Data
public class AppleCurator {

    private List<AppleCuratorsData>  data;

    private String href;

    public AppleCuratorsData getAppleCurator() {
        if (data != null && data.size() > 0) {
            return data.get(0);
        } else {
            return null;
        }
    }
}