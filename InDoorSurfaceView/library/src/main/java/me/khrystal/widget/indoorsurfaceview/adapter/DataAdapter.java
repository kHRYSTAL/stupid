package me.khrystal.widget.indoorsurfaceview.adapter;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

import me.khrystal.widget.indoorsurfaceview.unit.PathUnit;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/6/28
 * update time:
 * email: 723526676@qq.com
 */

public class DataAdapter extends BitAdapter {

    private List<PathUnit> list;
    private Bitmap bmp;

    public DataAdapter() {
    }

    public DataAdapter(List<PathUnit> list) {
        this.list = list;
        drawBitmap(this);
        drawBuffer(this);
    }

    public DataAdapter(List<PathUnit> list, Bitmap bmp) {
        this.list = list;
        this.bmp = bmp;
        drawBitmap(this);
        drawBuffer(this);
    }

    public void setList(List<PathUnit> list) {
        this.list = list;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }

    public void refreshData() {
        drawBitmap(this);
        drawBuffer(this);
        if (!bmp.isRecycled()) {
            bmp.recycle();
        }
        this.bmp = null;
    }

    @Override
    public List<PathUnit> getPathUnit() {
        if (list == null) {
            return new ArrayList<>();
        } else
            return list;
    }

    @Override
    public Bitmap getBgBitmap() {
        return this.bmp;
    }
}
