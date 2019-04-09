package me.khrystal.tools.kaudioplayer.exceptions;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/9
 * update time:
 * email: 723526676@qq.com
 */
public class AudioRawInvalidException extends Exception {
    public AudioRawInvalidException(String rawId) {
        super("Not a valid raw file id: " + rawId);
    }
}
