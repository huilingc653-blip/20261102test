package com.bytter.poi.word.entity;
/**
 * Created by zhouj on 2017/6/2.
 */
public class WordImageEntity {
    public static String URL = "url";
    public static String Data = "data";
    private String type;
    private int width;
    private int height;
    private String url;
    private byte[] data;
    public WordImageEntity() {
        this.type = URL;
    }
    public WordImageEntity(byte[] data, int width, int height) {
        this.type = URL;
        this.data = data;
        this.width = width;
        this.height = height;
        this.type = Data;
    }
    public WordImageEntity(String url, int width, int height) {
        this.type = URL;
        this.url = url;
        this.width = width;
        this.height = height;
    }
    public byte[] getData() {
        return this.data;
    }
    public int getHeight() {
        return this.height;
    }
    public String getType() {
        return this.type;
    }
    public String getUrl() {
        return this.url;
    }
    public int getWidth() {
        return this.width;
    }
    public void setData(byte[] data) {
        this.data = data;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public void setWidth(int width) {
        this.width = width;
    }
}
