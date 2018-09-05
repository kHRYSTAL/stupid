package me.khrystal.spinneredittext;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.khrystal.widget.edittext.SpinnerEditText;

public class MainActivity extends AppCompatActivity {

    private SpinnerEditText<BaseBean> spinnerEditText;
    private RelativeLayout rlContainer;
    private int distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initDIYAttSpinnerEditText();
    }

    private void initDIYAttSpinnerEditText(){
        spinnerEditText = (SpinnerEditText<BaseBean>) findViewById(R.id.set_div_att);
        rlContainer = findViewById(R.id.rlContainer);
        spinnerEditText.setOnItemClickListener(new SpinnerEditText.OnItemClickListener<BaseBean>() {
            @Override
            public void onItemClick(BaseBean baseBean, SpinnerEditText<BaseBean> var1, View var2, int position, long var4, String selectContent) {
                showToast("名称:" + baseBean.Name + " Id:" + baseBean.Id);
            }
        });
        // 设置假数据 模拟网络请求
        spinnerEditText.setList(getFakeList());

        spinnerEditText.setShowType(SpinnerEditText.TYPE_DOWN); // 设置默认展示位置在edittext下方
        // 获取页面container高度 整体进行scrollto移动
        // 根据是否获取到焦点进行页面的整体滑动 将整个页面滑动至edittext正好显示在屏幕的顶部
        // 当失去焦点时复原
        spinnerEditText.addOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    rlContainer.scrollBy(0, 600);
                } else {
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
