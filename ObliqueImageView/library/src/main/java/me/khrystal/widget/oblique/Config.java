package me.khrystal.widget.oblique;

import android.graphics.Path;
import android.util.Log;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/5/4
 * update time:
 * email: 723526676@qq.com
 */

public class Config {

    public int getTanWithOutConfilict(float h, float w, float angle, double hype) {
        if (angle > 90) {
            angle = 180 - angle;
        }
        int val = (int) Math.ceil(h / Math.tan(Math.toRadians(angle)));
        if (val <= hype) {
            return val;
        }
        int p = (int) Math.ceil(w * Math.tan(Math.toRadians(angle)));
        return getTanWithOutConfilict(p, w, angle, hype);
    }

    public Path getPath(float h, float w, float leftAngle, float rightAngle) {
        double hyp = Math.hypot(w, h);
        int leftBase = getTanWithOutConfilict(h, w, leftAngle, hyp);
        int rightBase = getTanWithOutConfilict(h, w, rightAngle, hyp);
        float a1 = 0, a2 = 0, b1 = w, b2 = 0, c1 = w, c2 = h, d1 = 0, d2 = h;
        try {
            if (leftAngle > 90 & leftAngle <= 180) {
                if (leftAngle > 145) {
                    d1 = w;
                    d2 = (float) Math.ceil(w * Math.tan(Math.toRadians(180 - leftAngle)));
                } else {
                    d1 = Math.abs(leftBase);
                }
            } else {
                a1 = Math.abs(leftBase);
                if (a1 == 0) {
                    a1 = w;
                }
                if (leftAngle < 45)
                    a2 = h - (float) Math.ceil(w * Math.tan(Math.toRadians(leftAngle)));
            }

            if (rightAngle > 90) {

                if (rightAngle > 135) {
                    b1 = 0;
                    b2 = h - (float) Math.ceil(w * Math.tan(Math.toRadians(180 - rightAngle)));
                } else {
                    b1 = Math.abs(w - rightBase);//w - rightBase;
                }

            } else if (rightAngle <= 180) {
                if (rightAngle < 45) {
                    c2 = (float) Math.floor(w * Math.tan(Math.toRadians(rightAngle)));
                    c1 = 0;
                } else {
                    c1 = w - Math.abs(rightBase);//w - rightBase;
                }
            }
        } catch (Exception e) {
            Log.e("Oblique", e.getMessage());
        }

        Path path = new Path();
        path.moveTo(a1, a2);
        path.lineTo(b1, b2);
        path.lineTo(c1, c2);
        path.lineTo(d1, d2);
        path.lineTo(a1, a2);
        path.close();
        return path;
    }
}
