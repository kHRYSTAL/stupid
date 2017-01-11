package me.khrystal.customtransition.part1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import me.khrystal.customtransition.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.sample_language);

        View input = findViewById(R.id.input);
        View inputDone = findViewById(R.id.input_done);
        final View focusHolder = findViewById(R.id.focus_holder);

        input.setOnFocusChangeListener(Part1TransitionController.newInstance(this));
        inputDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                focusHolder.requestFocus();
            }
        });
    }
}
