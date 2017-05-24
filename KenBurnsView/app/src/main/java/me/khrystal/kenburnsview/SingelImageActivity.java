package me.khrystal.kenburnsview;

import android.os.Bundle;

import me.khrystal.widget.kenburnsview.KenBurnsView;

public class SingelImageActivity extends KenBurnsActivity {

    private KenBurnsView mImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singel_image);
        mImg = (KenBurnsView) findViewById(R.id.img);
    }

    @Override
    protected void onPlayClick() {
        mImg.resume();
    }

    @Override
    protected void onPauseClick() {
        mImg.pause();
    }
}
