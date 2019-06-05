package me.khrystal.dynamicfragmentstatepageradapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/6/5
 * update time:
 * email: 723526676@qq.com
 */
public class BlankFragment extends Fragment {

    public BlankFragment() {
        // Required empty public constructor
    }

    public static BlankFragment newInstance(Subject subject) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();

        args.putParcelable("1", subject);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_blank, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Subject subject = getArguments().getParcelable("1");
        TextView textView = view.findViewById(R.id.tv_name);
        textView.setText(subject.getName());
    }
}
