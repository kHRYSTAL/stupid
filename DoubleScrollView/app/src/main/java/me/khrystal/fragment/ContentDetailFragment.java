package me.khrystal.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.khrystal.doublescrollview.R;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/3/12
 * update time:
 * email: 723526676@qq.com
 */
public class ContentDetailFragment extends Fragment {

    public ContentDetailFragment() {
    }

    private static ContentDetailFragment fragment = null;

    public static ContentDetailFragment newInstance() {
        if (fragment == null)
            fragment = new ContentDetailFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content_detail, container, false);
    }
}
