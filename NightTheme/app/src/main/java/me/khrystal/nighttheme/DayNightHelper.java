package me.khrystal.nighttheme;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/2/23
 * update time:
 * email: 723526676@qq.com
 */

public class DayNightHelper {
    private static final String FILE_NAME = "settings";
    private static final String MODE = "day_night_mode";

    private SharedPreferences mSharedPreference;

    public DayNightHelper(Context context) {
        this.mSharedPreference = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public boolean setMode(DayNight mode) {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putString(MODE, mode.getName());
        return editor.commit();
    }

    public boolean isNight() {
        String mode = mSharedPreference.getString(MODE, DayNight.DAY.getName());
        if (DayNight.NIGHT.getName().equals(mode)) {
            return true;
        } else
            return false;
    }

    public boolean isDay() {
        String mode = mSharedPreference.getString(MODE, DayNight.NIGHT.getName());
        if (DayNight.DAY.getName().equals(mode)) {
            return true;
        } else
            return false;
    }
}
