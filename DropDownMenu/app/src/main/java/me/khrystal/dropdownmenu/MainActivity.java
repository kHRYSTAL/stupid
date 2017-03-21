package me.khrystal.dropdownmenu;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import me.khrystal.widget.menu.DropDownMenu;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    DropDownMenu dropDownMenu;
    View menuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dropDownMenu = (DropDownMenu) findViewById(R.id.ddm);
        menuView = LayoutInflater.from(MainActivity.this).inflate(R.layout.content_layout, null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(150));

        dropDownMenu.setTopSpaceHight(dp2px(48))
                .setDividerColor(Color.RED)
                .setDropDownMenu(menuView, params)
                .setOnMenuStateChangeListener(new DropDownMenu.OnMenuStateChangeListener() {
                    @Override
                    public void onShow() {
                        Toast.makeText(MainActivity.this, "show", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDismiss() {
                        Toast.makeText(MainActivity.this, "dismiss", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private int dp2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void showMenu(View view) {
        dropDownMenu.openMenu();
    }
}
