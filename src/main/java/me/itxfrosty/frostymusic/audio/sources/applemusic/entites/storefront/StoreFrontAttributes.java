package me.itxfrosty.frostymusic.audio.sources.applemusic.entites.storefront;

import java.util.List;

import lombok.Data;

@Data
public class StoreFrontAttributes {

    private String defaultLanguageTag;

    private String name;

    private List<String> supportedLanguageTags;
}