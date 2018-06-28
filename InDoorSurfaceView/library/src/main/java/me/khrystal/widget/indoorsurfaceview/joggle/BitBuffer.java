package me.khrystal.widget.indoorsurfaceview.joggle;

import android.graphics.Bitmap;

import java.util.List;

import me.khrystal.widget.indoorsurfaceview.adapter.BitAdapter;
import me.khrystal.widget.indoorsurfaceview.unit.PathUnit;

/**
 * usage: 为绘制canvas供应getBitBuffer和路径
 * author: kHRYSTAL
 * create time: 18/6/28
 * update time:
 * email: 723526676@qq.com
 */

public interface BitBuffer {
    List<PathUnit> getPathUnit();

    Bitmap getBitBuffer();

    void setOnAdapterListener(BitAdapter.AttrListener listener);
}
