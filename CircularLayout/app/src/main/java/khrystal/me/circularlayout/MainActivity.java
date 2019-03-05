package khrystal.me.circularlayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import khrystal.me.circularlayout.widget.SampleCircleLayoutAdapter;
import me.khrystal.widget.circularlayout.CircularLayout;
import me.khrystal.widget.circularlayout.CircularLayoutAdapter;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    int progressChanged = 0;
    private CircularLayout circularLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SeekBar seekBar = findViewById(R.id.skRad);
        seekBar.setOnSeekBarChangeListener(this);

        SeekBar skOffsetX = findViewById(R.id.skOffsetX);
        skOffsetX.setOnSeekBarChangeListener(this);

        SeekBar skOffsetY = findViewById(R.id.skOffsetY);
        skOffsetY.setOnSeekBarChangeListener(this);

        SeekBar skChildCount = findViewById(R.id.skChildCount);
        skChildCount.setOnSeekBarChangeListener(this);

        circularLayout = findViewById(R.id.circularLayout);

        SampleCircleLayoutAdapter adapter = new SampleCircleLayoutAdapter();
        adapter.add(R.drawable.a1);
        adapter.add(R.drawable.a2);
        adapter.add(R.drawable.a3);
        adapter.add(R.drawable.a4);
        adapter.add(R.drawable.a5);
        adapter.add(R.drawable.a6);
        adapter.add(R.drawable.a7);
        adapter.add(R.drawable.a8);
        adapter.add(R.drawable.a9);
        adapter.add(R.drawable.a10);
        adapter.add(R.drawable.a11);
        adapter.add(R.drawable.a12);
        circularLayout.setAdapter(adapter);
        circularLayout.setChildrenCount(10);
        circularLayout.setRadius(120);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        progressChanged = i;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(seekBar.getId()==R.id.skRad) {
            circularLayout.setRadius(progressChanged);
            circularLayout.init();
        }
        if(seekBar.getId()==R.id.skChildCount) {
            circularLayout.setChildrenCount(progressChanged);
            circularLayout.init();
            circularLayout.balanceRotate();
        }
        if(seekBar.getId()==R.id.skOffsetX) {
            circularLayout.setOffsetX(progressChanged-90);
            circularLayout.init();
        }
        if(seekBar.getId()==R.id.skOffsetY) {
            circularLayout.setOffsetY(progressChanged-60);
            circularLayout.init();
        }
    }
}
