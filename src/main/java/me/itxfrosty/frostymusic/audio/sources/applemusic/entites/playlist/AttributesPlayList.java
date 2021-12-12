package me.itxfrosty.frostymusic.audio.sources.applemusic.entites.playlist;

import lombok.Data;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.Artwork;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.PlayParams;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.storefront.Description;

@Data
public class AttributesPlayList {

    private Artwork artwork;

    private String curatorName;

    private Description description;

    private String lastModifiedDate;

    private String name;

    private PlayParams playParams;

    private String playlistType;

    private String url;
}