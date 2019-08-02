package com.lite.imageloader.models;

/**
 * Created by Saket on 02,August,2019
 */
public class Photo {
    private String id;
    private Integer width;
    private Integer height;
    private Urls urls;

    public Urls getUrls() {
        return urls;
    }

    public void setUrls(Urls urls) {
        this.urls = urls;
    }
}
