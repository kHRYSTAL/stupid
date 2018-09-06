package me.khrystal.spinneredittext;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.khrystal.widget.edittext.SpinnerEditText;

/**
 * 注意 该demo 页面移动与软键盘是否显示正相关 与其他焦点, 文字输入无关
 */
public class MainActivity extends AppCompatActivity {

    private SpinnerEditText<BaseBean> spinnerEditText;
    private RelativeLayout rlContainer;
    private boolean hasMove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDIYAttSpinnerEditText();
    }

    private void initDIYAttSpinnerEditText() {
        spinnerEditText = findViewById(R.id.set_div_att);
        rlContainer = findViewById(R.id.rlContainer);
        spinnerEditText.setOnItemClickListener(new SpinnerEditText.OnItemClickListener<BaseBean>() {
            @Override
            public void onItemClick(BaseBean baseBean, SpinnerEditText<BaseBean> var1, View var2, int position, long var4, String selectContent) {
                showToast("名称:" + baseBean.Name + " Id:" + baseBean.Id);
            }
        });
        // 设置默认展示位置在edit text下方
        spinnerEditText.setShowType(SpinnerEditText.TYPE_DOWN);
        // 设置远程请求数据监听器
        spinnerEditText.setRemoteDataAdapter(new SpinnerEditText.RemoteDataAdapter() {
            @Override
            public void doOnRemote() {
                // 获取网络数据设置到列表上展示
                List remoteData = getFakeList();
                if (remoteData != null && !remoteData.isEmpty()) {
                    spinnerEditText.setList(remoteData);
                    spinnerEditText.showPopup();
                } else {
                    spinnerEditText.hidePopup();
                }
            }
        });

        spinnerEditText.addOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus && !hasMove) {
                    hasMove = true;
                    rlContainer.scrollBy(0, 600);
                }
            }
        });

        SoftKeyBoardListener.setListener(MainActivity.this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                if (spinnerEditText.hasFocus() && !hasMove) {
                    hasMove = true;
                    rlContainer.scrollBy(0, 600);
                }
            }

            @Override
            public void keyBoardHide(int height) {
                if (hasMove) {
                    hasMove = false;
                    rlContainer.scrollBy(0, -600);
                }
            }
        });
    }

    public static class BaseBean {
        public String Name;
        public int Id;

        @Override
        public String toString() {
            return Name;
        }
    }


    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT)
                .show();
    }

    private List<BaseBean> getFakeList() {
        List<BaseBean> baseBeanList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            BaseBean baseBean = new BaseBean();
            baseBean.Name = "学生:" + i;
            baseBean.Id = i;
            baseBeanList.add(baseBean);
        }
        return baseBeanList;
    }
}
