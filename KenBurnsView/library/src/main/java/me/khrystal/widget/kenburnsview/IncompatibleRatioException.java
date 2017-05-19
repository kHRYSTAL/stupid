package me.khrystal.widget.kenburnsview;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/5/18
 * update time:
 * email: 723526676@qq.com
 */

public class IncompatibleRatioException extends RuntimeException {

    public IncompatibleRatioException() {
        super("Can't perform Ken Burns effect on rects with distinct aspect ratios");
    }
}
