package me.khrystal.tools.kaudioplayer.exceptions;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/9
 * update time:
 * email: 723526676@qq.com
 */
public class AudioListNullPointerException extends NullPointerException {
    public AudioListNullPointerException() {
        super("The playlist is empty or null");
    }
}
