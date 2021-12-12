package me.itxfrosty.frostymusic.audio.sources.applemusic.entites.storefront;

import java.util.List;
import lombok.Data;

@Data
public class StoreFront {

    private List<StoreFrontData> data;

    public StoreFrontData getStoreFront() {
        if (data != null && data.size() > 0) {
            return data.get(0);
        } else {
            return null;
        }
    }
}
