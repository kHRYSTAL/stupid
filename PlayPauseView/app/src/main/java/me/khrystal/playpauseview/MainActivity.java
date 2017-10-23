package me.khrystal.playpauseview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import me.khrystal.widget.PlayPauseView;

public class MainActivity extends AppCompatActivity {

    PlayPauseView playPauseView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playPauseView = (PlayPauseView) findViewById(R.id.ppv);
        playPauseView.setAnimDuration(200);
        playPauseView.setPlayPauseListener(new PlayPauseView.PlayPauseListener() {
            @Override
            public void play() {
                Toast.makeText(MainActivity.this, "play", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void pause() {
                Toast.makeText(MainActivity.this, "pause", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
