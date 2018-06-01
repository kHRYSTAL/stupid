package me.khrystal.widget;

import android.view.animation.Interpolator;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.cos;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/6/1
 * update time:
 * email: 723526676@qq.com
 */

public class BounceInterpolator implements Interpolator {

    private int mBounces;
    private double mEnergy;

    public BounceInterpolator() {
        this(3);
    }

    public BounceInterpolator(int bounces) {
        this(bounces, 0.3f);
    }

    public BounceInterpolator(int bounces, double energyFactor) {
        this.mBounces = bounces;
        this.mEnergy = energyFactor + 0.5f;
    }

    @Override
    public float getInterpolation(float x) {
        return (float) (1d + (-abs(cos(x * 10 * mBounces/PI)) * getCurveAdjustment(x)));
    }

    private double getCurveAdjustment(double  x){
        return -(2 * (1 - x) * x * mEnergy + x * x) + 1;
    }
}
