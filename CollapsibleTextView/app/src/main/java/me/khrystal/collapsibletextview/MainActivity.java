package me.khrystal.collapsibletextview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import me.khrystal.widget.CollapsibleTextView;


public class MainActivity extends AppCompatActivity {

    CollapsibleTextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (CollapsibleTextView) findViewById(R.id.coll_tv);
        textView.setText(getString(R.string.tips_short));
    }

    public void onButtonClick(View view) {
        textView.setText(getString(R.string.tips));
        textView.resetState(true);
    }

    public void onGoToClick(View view) {
        startActivity(new Intent(MainActivity.this, ListActivity.class));
    }
}
