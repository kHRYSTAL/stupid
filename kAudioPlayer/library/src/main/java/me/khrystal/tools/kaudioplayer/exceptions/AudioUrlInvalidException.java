package me.khrystal.tools.kaudioplayer.exceptions;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/9
 * update time:
 * email: 723526676@qq.com
 */
public class AudioUrlInvalidException extends IllegalStateException {
    public AudioUrlInvalidException(String url) {
        super("The url does not appear valid: " + url);
    }
}
