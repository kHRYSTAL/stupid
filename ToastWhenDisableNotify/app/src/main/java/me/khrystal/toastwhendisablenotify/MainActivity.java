package me.khrystal.toastwhendisablenotify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import me.khrystal.widget.BooheeToast;
import me.khrystal.widget.toast.Toast;
import me.khrystal.widget.util.DensityUtil;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "我是一个屏蔽通知我也是可以显示的Toast", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.textView3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                View view = inflater.inflate(R.layout.lay_toast_txt, null);
                TextView tvContent = (TextView) view.findViewById(R.id.tvContent);
                tvContent.setText("我是一个屏蔽通知我也是可以显示的Toast");
//                Toast toast = new Toast(ZHApplication.APP_CONTEXT);
                BooheeToast toast = new BooheeToast(MainActivity.this);
                toast.setGravity(Gravity.BOTTOM, 0, DensityUtil.dip2px(MainActivity.this, 70));
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(view);
                toast.show();
            }
        });
        findViewById(R.id.textView1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SecondActivity.class));
            }
        });
        findViewById(R.id.textView2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
