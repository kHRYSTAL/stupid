package me.khrystal.hxwitheaseuidemo.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.socks.library.KLog;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import me.khrystal.hxwitheaseuidemo.R;
import me.khrystal.hxwitheaseuidemo.base.GlobalField;
import me.khrystal.hxwitheaseuidemo.base.MyConnectionListener;
import me.khrystal.hxwitheaseuidemo.ui.LoginActivity;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/6/26
 * update time:
 * email: 723526676@qq.com
 */

public class PersonFragment extends Fragment {
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tv_person_username)
    TextView tvPersonUsername;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);
        ButterKnife.inject(this, view);
        tvTitle.setText("个人中心");
        String userName = getActivity().getSharedPreferences(GlobalField.USERINFO_FILENAME, Context.MODE_PRIVATE)
                .getString("username", "hdl");
        tvPersonUsername.setText(userName);
        EMClient.getInstance().addConnectionListener(new MyConnectionListener(getActivity()));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.btn_person_add, R.id.btn_exit, R.id.btn_remove_friend})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_person_add:
                addFriend();
                break;
            case R.id.btn_remove_friend:
                removeFriend();
                break;
            case R.id.btn_exit:
                EMClient.getInstance().logout(true, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        Log.e("main", "下线成功了");
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.e("main", "下线失败了! " + s);
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
                break;
        }
    }

    private void removeFriend() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("删除好友");
        final EditText newFriendName = new EditText(getActivity());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        newFriendName.setLayoutParams(layoutParams);
        newFriendName.setHint("要删除的好友名");
        builder.setView(newFriendName);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("移除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread() {
                    @Override
                    public void run() {
                        String friendName = newFriendName.getText().toString().trim();
                        try {
                            EMClient.getInstance().contactManager().deleteContact(friendName);
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void addFriend() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("添加好友");
        final EditText newFriendName = new EditText(getActivity());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        newFriendName.setLayoutParams(layoutParams);
        newFriendName.setHint("新好友用户名");
        builder.setView(newFriendName);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("添加", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread() {
                    @Override
                    public void run() {
                        String friendName = newFriendName.getText().toString().trim();
                        try {
                            EMClient.getInstance().contactManager().addContact(friendName, "我是你朋友");
                            KLog.e("添加好友成功, 等待回应: " + friendName);
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
    }
}
