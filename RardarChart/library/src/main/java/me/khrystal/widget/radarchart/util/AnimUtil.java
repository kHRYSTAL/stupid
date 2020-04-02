package me.khrystal.widget.radarchart.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.khrystal.widget.radarchart.RadarData;
import me.khrystal.widget.radarchart.RadarView;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 2020-04-02
 * update time:
 * email: 723526676@qq.com
 */
public class AnimUtil {
    private WeakReference<RadarView> mWeakRadarView;
    private HashMap<RadarData, ValueAnimator> mAnims;

    public AnimUtil(RadarView view) {
        mWeakRadarView = new WeakReference<>(view);
        mAnims = new HashMap<>();
    }

    public void animeValue(AnimeType type, int duration, RadarData data) {
        switch (type) {
            case ZOOM:
                startZoomAnime(duration, data);
                break;
        }
    }

    public boolean isPlaying() {
        boolean isPlaying = false;
        for (ValueAnimator anime : mAnims.values()) {
            isPlaying = anime.isStarted();
            if (isPlaying) {
                break;
            }
        }
        return isPlaying;
    }

    public boolean isPlaying(RadarData data) {
        ValueAnimator anime = mAnims.get(data);
        return anime != null && anime.isStarted();
    }

    private void startZoomAnime(final int duration, final RadarData data) {
        final ValueAnimator anime = ValueAnimator.ofFloat(0, 1f);
        final List<Float> values = data.getValue();
        final List<Float> values2 = new ArrayList<>(values);
        anime.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                RadarView view = mWeakRadarView.get();
                if (view == null) {
                    anime.end();
                } else {
                    float percent = Float.parseFloat(animation.getAnimatedValue().toString());
                    for (int i = 0; i < values.size(); i++) {
                        values.set(i, values2.get(i) * percent);
                    }
                    view.invalidate();
                }
            }
        });
        anime.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnims.remove(data);
            }
        });
        anime.setDuration(duration).start();
        mAnims.put(data, anime);
    }

    public enum AnimeType {
        ZOOM, ROTATE
    }
}
