package me.khrystal.widget.viewpager;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/6/3
 * update time:
 * email: 723526676@qq.com
 */
public abstract class DynamicFragmentStatePagerAdapter<T> extends PagerAdapter {

    private static final String TAG = DynamicFragmentStatePagerAdapter.class.getSimpleName();
    private static final boolean DEBUG = BuildConfig.DEBUG;

    private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;

    private ArrayList<Fragment.SavedState> mSaveState = new ArrayList<>();
    private ArrayList<ItemInfo<T>> mItemInfos = new ArrayList<>();
    private Fragment mCurrentPrimaryItem = null;
    private boolean mNeedProcessCache = false;

    public DynamicFragmentStatePagerAdapter(FragmentManager fm) {
        mFragmentManager = fm;
    }

    /**
     * return the fragment associated with a specified position
     */
    public abstract Fragment getItem(int position);

    protected Fragment getCacheItem(int position) {
        return mItemInfos.size() > position ? mItemInfos.get(position).fragment : null;
    }

    @Override
    public void startUpdate(@NonNull ViewGroup container) {
        if (container.getId() == View.NO_ID) {
            throw new IllegalStateException("Viewpager with adapter " + this + " requires a view id");
        }
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        // if we already have this item instantiated, there is nothing to do.
        // This can happen when we are restoring the entire pager from its save state,
        // where the fragment manager has already taken care of restoring the fragments we
        // previously had instantiated
        if (mItemInfos.size() > position) {
            ItemInfo i = mItemInfos.get(position);
            if (i != null) {
                // judge position is equals, if not equals that mean data has new add or delete
                // but now `notifyDataSetChanged() has not complete` viewpager will call `instantiateItem`
                // to fetch new page at first, so we need to check cache and adjust position: `checkProcessCacheChanged`
                if (i.position == position) {
                    return i;
                } else {
                    checkProcessCacheChanged();
                }
            }
        }

        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }

        Fragment fragment = getItem(position);
        if (DEBUG)
            Log.v(TAG, "Adding item #" + position + ": f=" + fragment);
        if (mSaveState.size() > position) {
            Fragment.SavedState fss = mSaveState.get(position);
            if (fss != null) {
                fragment.setInitialSavedState(fss);
            }
        }

        while (mItemInfos.size() <= position) {
            mItemInfos.add(null);
        }
        fragment.setMenuVisibility(false);
        fragment.setUserVisibleHint(false);
        ItemInfo<T> iNew = new ItemInfo<>(fragment, getItemData(position), position);
        mItemInfos.set(position, iNew);
        mCurTransaction.add(container.getId(), fragment);
        return iNew;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ItemInfo i = (ItemInfo) object;
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        if (DEBUG)
            Log.v(TAG, "Removing item #" + position + ": f=" + object + " v=" + ((Fragment) object).getView());

        while (mSaveState.size() <= position) {
            mSaveState.add(null);
        }

        mSaveState.set(position, i.fragment.isAdded() ? mFragmentManager.saveFragmentInstanceState(i.fragment) : null);
        mItemInfos.set(position, null);
        mCurTransaction.remove(i.fragment);
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ItemInfo i = (ItemInfo) object;
        Fragment fragment = i.fragment;
        if (fragment != mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem.setMenuVisibility(false);
                mCurrentPrimaryItem.setUserVisibleHint(false);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }

            mCurrentPrimaryItem = fragment;
        }
    }

    @Override
    public void finishUpdate(@NonNull ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitAllowingStateLoss();
            mCurTransaction = null;
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        Fragment fragment = ((ItemInfo) o).fragment;
        return fragment.getView() == view;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        mNeedProcessCache = true;
        ItemInfo<T> itemInfo = (ItemInfo) object;
        int oldPosition = mItemInfos.indexOf(itemInfo);
        if (oldPosition >= 0) {
            T oldData = itemInfo.data;
            T newData = getItemData(oldPosition);
            if (dataEquals(oldData, newData)) {
                return POSITION_UNCHANGED;
            } else {
                ItemInfo<T> oldItemInfo = mItemInfos.get(oldPosition);
                int oldDataNewPosition = getDataPosition(oldData);
                if (oldDataNewPosition < 0) {
                    oldDataNewPosition = POSITION_NONE;
                }
                // take new position to reference cache itemInfo, use by adjust
                if (oldItemInfo != null) {
                    oldItemInfo.position = oldDataNewPosition;
                }
                checkProcessCacheChanged();
                return oldDataNewPosition;
            }
        }
        return POSITION_UNCHANGED;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        // notify viewpager update complete, adjust cached itemInfo list
        checkProcessCacheChanged();
    }

    private void checkProcessCacheChanged() {
        // 只有调用个过getItemPosition 也就是有notifyDataSetChanged 才进行缓存的调整
        if (!mNeedProcessCache) return;
        mNeedProcessCache = false;
        ArrayList<ItemInfo<T>> pendingItemInfos = new ArrayList<>(mItemInfos.size());
        // 先存入空数据
        for (int i = 0; i < mItemInfos.size(); i++) {
            pendingItemInfos.add(null);
        }
        // 根据缓存的itemInfo中的新position把itemInfo放入正确的为止
        for (ItemInfo<T> itemInfo : mItemInfos) {
            if (itemInfo != null) {
                if (itemInfo.position >= 0) {
                    while (pendingItemInfos.size() <= itemInfo.position) {
                        pendingItemInfos.add(null);
                    }
                    pendingItemInfos.set(itemInfo.position, itemInfo);
                } else {
                    // 移除已经被删除掉的
                    Fragment fragment = itemInfo.fragment;
                    if (mCurTransaction == null) {
                        mCurTransaction = mFragmentManager.beginTransaction();
                    }
                    mCurTransaction.remove(fragment);
                }
            }
        }
        mItemInfos = pendingItemInfos;
    }

    @Nullable
    @Override
    public Parcelable saveState() {
        Bundle state = null;
        if (mSaveState.size() > 0) {
            state = new Bundle();
            Fragment.SavedState[] fss = new Fragment.SavedState[mSaveState.size()];
            mSaveState.toArray(fss);
            state.putParcelableArray("states", fss);
        }
        for (int i = 0; i < mItemInfos.size(); i++) {
            ItemInfo info = mItemInfos.get(i);
            if (info == null) {
                continue;
            } else {
                Fragment f = mItemInfos.get(i).fragment;
                if (f != null && f.isAdded()) {
                    if (state == null) {
                        state = new Bundle();
                    }
                    String key = "f" + i;
                    mFragmentManager.putFragment(state, key, f);
                }
            }
        }
        return state;
    }

    @Override
    public void restoreState(@Nullable Parcelable state, @Nullable ClassLoader loader) {
        if (state != null) {
            Bundle bundle = (Bundle) state;
            bundle.setClassLoader(loader);
            Parcelable[] fss = bundle.getParcelableArray("states");
            mSaveState.clear();
            mItemInfos.clear();
            if (fss != null) {
                for (int i = 0; i < fss.length; i++) {
                    mSaveState.add((Fragment.SavedState) fss[i]);
                }
            }
            Iterable<String> keys = bundle.keySet();
            for (String key : keys) {
                if (key.startsWith("f")) {
                    int index = Integer.parseInt(key.substring(1));
                    Fragment f = mFragmentManager.getFragment(bundle, key);

                    if (f != null) {
                        while (mItemInfos.size() <= index) {
                            mItemInfos.add(null);
                        }
                        f.setMenuVisibility(false);
                        ItemInfo<T> iNew = new ItemInfo<>(f, getItemData(index), index);
                        mItemInfos.set(index, iNew);
                    } else {
                        Log.w(TAG, "Bad fragment at key " + key);
                    }
                }
            }
        }
    }

    protected Fragment getCurrentPrimaryItem() {
        return mCurrentPrimaryItem;
    }

    protected Fragment getFragmentByPosition(int position) {
        if (position < 0 || position >= mItemInfos.size())
            return null;
        return mItemInfos.get(position).fragment;
    }

    public abstract T getItemData(int position);

    public abstract boolean dataEquals(T oldData, T newData);

    // if < 0, its mean data is not exist its mean data has already deleted
    public abstract int getDataPosition(T data);

    static class ItemInfo<D> {
        Fragment fragment;
        D data;
        int position;

        public ItemInfo(Fragment fragment, D data, int position) {
            this.fragment = fragment;
            this.data = data;
            this.position = position;
        }
    }
}
