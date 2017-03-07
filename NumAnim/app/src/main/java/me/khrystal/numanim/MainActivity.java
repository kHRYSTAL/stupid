package me.khrystal.numanim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import me.khrystal.anim.util.NumberAnim;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.tv);
    }

    public void startAnim(View view) {
        if (textView != null) {
            NumberAnim.startAnim(textView, 3456f);
        }
    }
}
