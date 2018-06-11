package me.khrystal.util.stetho;

import java.io.IOException;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/6/11
 * update time:
 * email: 723526676@qq.com
 */

public interface ResponseHandler {
    void onRead(int numBytes);

    void onReadDecoded(int numBytes);

    void onEOF();

    void onError(IOException e);
}
