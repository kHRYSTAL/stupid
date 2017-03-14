package me.khrystal.widget.friendcircle.adapter;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.ImageView;

import me.khrystal.widget.friendcircle.adapter.observer.PhotoAdapterObservable;
import me.khrystal.widget.friendcircle.adapter.observer.PhotoBaseDataObserver;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/3/14
 * update time:
 * email: 723526676@qq.com
 */

public abstract class PhotoContentsBaseAdapter {

    private PhotoAdapterObservable mObservable = new PhotoAdapterObservable();

    /**
     * 向被观察者注册观察者
     * @param observer
     */
    public void registerDataSetObserver(PhotoBaseDataObserver observer) {
        mObservable.registerObserver(observer);
    }

    /**
     * 向被观察者解除绑定观察者
     * @param observer
     */
    public void unregisterDataSetObserver(PhotoBaseDataObserver observer) {
        mObservable.unregisterObserver(observer);
    }

    public void notifyDataChaged() {
        mObservable.notifyChanged();
        mObservable.notifyInvalidated();
    }

    public abstract ImageView onCreateView(ImageView convertView, ViewGroup parent, int position);

    public abstract void onBindData(int position, @NonNull ImageView convertView);

    public abstract int getCount();
}
