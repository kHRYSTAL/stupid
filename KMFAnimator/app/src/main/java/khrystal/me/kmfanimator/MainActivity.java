package khrystal.me.kmfanimator;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.btn);
        imageView = findViewById(R.id.iv);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAnim();
            }
        });
    }

    private void startAnim() {
        imageView.setVisibility(View.VISIBLE);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(imageView,"scaleX", 1.0f, 1.5f, 1.2f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(imageView,"scaleY", 1.0f, 1.5f, 1.2f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator1, animator2);
        animatorSet.setDuration(2000);
        animatorSet.start();
    }
}
