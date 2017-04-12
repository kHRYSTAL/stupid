package me.khrystal.badgerdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import me.khrystal.widget.badger.BadgeShape;
import me.khrystal.widget.badger.Badger;
import me.khrystal.widget.badger.CountBadge;

public class MainActivity extends AppCompatActivity {

    CountBadge.Factory ovalFactory;
    CountBadge.Factory squareFactory;
    CountBadge.Factory circleFactory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ovalFactory = new CountBadge.Factory(this, BadgeShape.oval(1f, 2f, Gravity.BOTTOM));
        squareFactory = new CountBadge.Factory(this, BadgeShape.square(1f, Gravity.NO_GRAVITY, .5f));
        circleFactory = new CountBadge.Factory(this, BadgeShape.circle(.5f, Gravity.END | Gravity.TOP));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_badger, menu);
        Badger.sett(menu.findItem(R.id.action_oval), ovalFactory).setCount(0);
        Badger.sett(menu.findItem(R.id.action_square), squareFactory).setCount(0);
        Badger.sett(menu.findItem(R.id.action_circle), circleFactory).setCount(0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_oval:
            case R.id.action_square:
            case R.id.action_circle:
                // factory is not used for getting the badge
                //noinspection ConstantConditions
                CountBadge badge = Badger.sett(item, null);
                badge.setCount(badge.getCount() + 1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void resetBadges(View view) {
        invalidateOptionsMenu();
    }
}
