package khrystal.me.arcslidinghelper;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import me.khrystal.widget.ArcSlidingHelper;

public class MainActivity extends AppCompatActivity {

    private ArcSlidingHelper arcSlidingHelper;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView view = findViewById(R.id.textView);
        view.post(() -> {
            arcSlidingHelper = ArcSlidingHelper.create(view, angle -> view.setRotation(view.getRotation() + angle));
            arcSlidingHelper.enableInertialSliding(true);
        });
        getWindow().getDecorView()
//                view
                        .setOnTouchListener((v, event) -> {
            arcSlidingHelper.handleMovement(event);
            return true;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        arcSlidingHelper.release();
    }
}
