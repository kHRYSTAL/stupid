package me.khrystal.jobpopupwindow;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.khrystal.widget.popupwindow.JobItem;
import me.khrystal.widget.popupwindow.JobPopupWindow;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    private JobPopupWindow popupWindow;
    private List<JobItem> jobItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initData();
        initView();
    }

    private void initView() {
        editText = findViewById(R.id.et);
        popupWindow = new JobPopupWindow(this);
        popupWindow.updateList(jobItems);
        popupWindow.setOnItemClickListener(new JobPopupWindow.OnItemClickListener() {
            @Override
            public void onItemClickListener(JobPopupWindow source, int position, JobItem jobItem) {
                Toast.makeText(MainActivity.this, jobItem.getJobText(), Toast.LENGTH_SHORT).show();
            }
        });
        editText.addTextChangedListener(new EditChangedListener());
    }

    private void initData() {
        jobItems = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            jobItems.add(new JobItem("测试职位item" + i, i));
        }
    }

    class EditChangedListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            popupWindow.dismiss();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                // TODO: 18/9/4 发起请求 请求成功后弹出 popup window
                if (popupWindow != null) {
                    popupWindow.show(editText);
                }
            }
        }
        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}
