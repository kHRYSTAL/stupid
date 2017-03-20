package me.khrystal.readmoretextview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import me.khrystal.widget.ReadMoreTextView;

public class MainActivity extends AppCompatActivity {

    ReadMoreTextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (ReadMoreTextView) findViewById(R.id.rmtv);
        textView.setText(getString(R.string.tips_short), TextView.BufferType.NORMAL);
    }

    public void setOnText(View view) {
        textView.setText(getString(R.string.tips), TextView.BufferType.NORMAL);
    }

    public void goToList(View view) {
        Intent intent = new Intent(MainActivity.this, ListActivity.class);
        startActivity(intent);
    }
}
