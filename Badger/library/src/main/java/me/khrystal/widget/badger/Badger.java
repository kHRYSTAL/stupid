package me.khrystal.widget.badger;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.ImageView;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/4/5
 * update time:
 * email: 723526676@qq.com
 */

public class Badger<T extends BadgeDrawable> {

    public final Drawable drawable;
    public final T badge;

    private Badger(Drawable drawable, T badge) {
        this.drawable = drawable;
        this.badge = badge;
    }

    public static <T extends BadgeDrawable> Badger<T>
    sett(Drawable drawable, @NonNull BadgeDrawable.Factory<? extends T> factory) {
        if (!(drawable instanceof LayerDrawable)) {
            T badge = factory.createBadge();
            LayerDrawable layer = new LayerDrawable(new Drawable[]{drawable, badge});
            layer.setId(1, R.id.badger_drawable);
            return new Badger<>(layer, badge);
        }
        LayerDrawable layer = (LayerDrawable) drawable;
        drawable = layer.findDrawableByLayerId(R.id.badger_drawable);
        if (drawable == null) {
            T badge = factory.createBadge();
            int count = layer.getNumberOfLayers();
            Drawable[] layers = new Drawable[count + 1];
            for (int i = 0; i < count; i++) {
                layers[i] = layer.getDrawable(i);
            }
            layers[count] = badge;
            layer = new LayerDrawable(layers);
            layer.setId(count, R.id.badger_drawable);
            return new Badger<>(layer, badge);
        }

        try {
            return new Badger<>(layer, (T) drawable);
        } catch (ClassCastException e) {
            String errorMsg = "layer with id R.id.badger_drawable must be an instance of"
                    + factory.createBadge().getClass().getSimpleName();
            throw new IllegalStateException(errorMsg);
        }
    }

    public static <T extends BadgeDrawable> T
    sett(@NonNull MenuItem item, @NonNull BadgeDrawable.Factory<? extends T> factory) {
        Badger<T> badger = sett(item.getIcon(), factory);
        item.setIcon(badger.drawable);
        return badger.badge;
    }

    public static <T extends BadgeDrawable> T
    sett(@NonNull ImageView view, @NonNull BadgeDrawable.Factory<? extends T> factory) {
        Badger<T> badger = sett(view.getDrawable(), factory);
        view.setImageDrawable(badger.drawable);
        return badger.badge;
    }


}
