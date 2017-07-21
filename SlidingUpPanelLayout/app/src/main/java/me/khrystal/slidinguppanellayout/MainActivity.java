package me.khrystal.slidinguppanellayout;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.khrystal.widget.sliding.ISlidingUpPanel;
import me.khrystal.widget.sliding.SlidingUpPanelLayout;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.pick_hint_text)
    TextView pickHintText;
    @BindView(R.id.pay_hint_text)
    TextView payHintText;
    @BindView(R.id.bg_layout)
    ConstraintLayout bgLayout;
    @BindView(R.id.sliding_up_panel_layout)
    SlidingUpPanelLayout slidingUpPanelLayout;

    private ISlidingUpPanel mSlidingUpPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // transparent status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        slidingUpPanelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSliding(ISlidingUpPanel panel, float slideProgress) {
                if (mSlidingUpPanel == null || mSlidingUpPanel == panel) {
                    pickHintText.setAlpha(1 - slideProgress);
                    payHintText.setAlpha(slideProgress);
                }
            }

            @Override
            public void onPanelCollapsed(ISlidingUpPanel panel) {

            }

            @Override
            public void onPanelExpanded(ISlidingUpPanel panel) {
                mSlidingUpPanel = panel;
                int childCount = slidingUpPanelLayout.getChildCount();
                CardPanelView card;
                for (int i = 1; i < childCount; i++) {
                    card = (CardPanelView) slidingUpPanelLayout.getChildAt(i);
                    if (card != panel && card.getSlideState() == SlidingUpPanelLayout.EXPANDED) {
                        slidingUpPanelLayout.collapsePanel(card);
                        break;
                    }
                }
                payHintText.setAlpha(1.0f);
            }

            @Override
            public void onPanelHidden(ISlidingUpPanel panel) {

            }
        });

        int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = getResources().getDimensionPixelSize(resId);
        bgLayout.setPadding(0, statusBarHeight, 0, 0);
    }
}
