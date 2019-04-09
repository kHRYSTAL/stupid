package me.khrystal.tools.kaudioplayer.exceptions;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/9
 * update time:
 * email: 723526676@qq.com
 */
public class AudioFilePathInvalidException extends Exception {
    public AudioFilePathInvalidException(String url) {
        super("The file path is not a valid path: " + url + "Have you add File Access Permission?");
    }
}
