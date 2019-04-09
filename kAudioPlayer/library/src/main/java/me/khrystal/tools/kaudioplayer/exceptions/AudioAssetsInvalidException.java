package me.khrystal.tools.kaudioplayer.exceptions;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/9
 * update time:
 * email: 723526676@qq.com
 */
public class AudioAssetsInvalidException extends Exception {
    public AudioAssetsInvalidException(String path) {
        super("The file name is not a valid Assets file: " + path);
    }
}
