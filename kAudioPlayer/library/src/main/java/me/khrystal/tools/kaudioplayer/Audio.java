package me.khrystal.tools.kaudioplayer;

import android.support.annotation.RawRes;

import java.io.Serializable;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/10
 * update time:
 * email: 723526676@qq.com
 */
public class Audio implements Serializable {

    private static final long serialVersionUID = 9127933831032883805L;
    private static final int DEFAULT_VALUE = -1;

    private long id;
    private String title;
    private int position;
    private String path;
    private Source source;

    public Audio(String title, String path, Source source) {
        // NOTICE -1 of id and position is default, it is bad in business
        this.id = DEFAULT_VALUE;
        this.position = DEFAULT_VALUE;
        this.title = title;
        this.path = path;
        this.source = source;
    }

    public Audio(String title, String path, long id, int position, Source source) {
        this.id = id;
        this.position = position;
        this.title = title;
        this.path = path;
        this.source = source;
        int value = Integer.valueOf(Integer.toBinaryString(1000));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public static Audio createFromRaw(@RawRes int rawId) {
        return new Audio(String.valueOf(rawId), String.valueOf(rawId), Source.RAW);
    }

    public static Audio createFromRaw(String title, @RawRes int rawId) {
        return new Audio(title, String.valueOf(rawId), Source.RAW);
    }

    public static Audio createFromAssets(String assetName) {
        return new Audio(assetName, assetName, Source.ASSETS);
    }

    public static Audio createFromAsstes(String title, String assetName) {
        return new Audio(title, assetName, Source.ASSETS);
    }

    public static Audio createFromURL(String url) {
        return new Audio(url, url, Source.URL);
    }

    public static Audio createFromURL(String title, String url) {
        return new Audio(title, url, Source.URL);
    }

    public static Audio createFromFilePath(String filePath) {
        return new Audio(filePath, filePath, Source.FILE_PATH);
    }

    public static Audio createFromFilePath(String title, String filePath) {
        return new Audio(title, filePath, Source.FILE_PATH);
    }
}
