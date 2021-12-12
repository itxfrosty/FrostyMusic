package me.itxfrosty.frostymusic.audio.sources.applemusic.entites;

import lombok.Data;

@Data
public class Error {

    private String id;

    private String title;

    private String detail;

    private String status;

    private String code;
}