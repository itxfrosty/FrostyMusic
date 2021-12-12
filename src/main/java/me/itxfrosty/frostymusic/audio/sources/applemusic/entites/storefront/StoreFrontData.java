package me.itxfrosty.frostymusic.audio.sources.applemusic.entites.storefront;

import lombok.Data;

@Data
public class StoreFrontData {

    private StoreFrontAttributes attributes;

    private String href;

    private String id;

    private String type;
}