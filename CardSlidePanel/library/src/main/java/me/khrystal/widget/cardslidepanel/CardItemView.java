package me.khrystal.widget.cardslidepanel;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/6/11
 * update time:
 * email: 723526676@qq.com
 */
public class CardItemView extends FrameLayout {

    private SimpleDraweeView imageView;
    private View shadowView;

    public CardItemView(@NonNull Context context) {
        this(context, null);
    }

    public CardItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.card_item, this);
        imageView = (SimpleDraweeView) findViewById(R.id.card_image_view);
        shadowView = findViewById(R.id.shade);
    }

    public void fillData(CardDataItem itemData) {
        imageView.setImageURI(Uri.parse(itemData.imagePath));
    }

    public void setShadeLayer(int shadeLayer) {
        shadowView.setBackgroundResource(shadeLayer);
    }
}
