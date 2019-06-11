package me.khrystal.widget.cardslidepanel;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Property;
import android.widget.FrameLayout;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/6/11
 * update time:
 * email: 723526676@qq.com
 */
public class DrawShadowFrameLayout extends FrameLayout {

    private Drawable mShadowDrawable;
    private NinePatchDrawable mShadowNinePatchDrawable;
    private int mShadowTopOffset;
    private boolean mShadowVisible;
    private int mWidth, mHeight;
    private ObjectAnimator mAnimator;
    private float mAlpha = 1f;

    public DrawShadowFrameLayout(@NonNull Context context) {
        this(context, null, 0);
    }

    public DrawShadowFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawShadowFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DrawShadowFrameLayout, 0, 0);
        mShadowDrawable = ta.getDrawable(R.styleable.DrawShadowFrameLayout_shadowDrawable);
        if (mShadowDrawable != null) {
            mShadowDrawable.setCallback(this);
            if (mShadowDrawable instanceof NinePatchDrawable) {
                mShadowNinePatchDrawable = (NinePatchDrawable) mShadowDrawable;
            }
        }
        mShadowVisible = ta.getBoolean(R.styleable.DrawShadowFrameLayout_shadowVisible, true);
        setWillNotDraw(!mShadowVisible || mShadowDrawable == null);
        ta.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        if (mShadowDrawable != null) {
            mHeight = mShadowDrawable.getIntrinsicHeight() + 30;
        } else {
            mHeight = 50;
        }
        updateShadowBounds();
    }

    private void updateShadowBounds() {
        if (mShadowDrawable != null) {
            mShadowDrawable.setBounds(0, mShadowTopOffset, mWidth, mHeight);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (mShadowDrawable != null && mShadowVisible) {
            if (mShadowNinePatchDrawable != null) {
                mShadowNinePatchDrawable.getPaint().setAlpha((int) (255 * mAlpha));
            }
            mShadowDrawable.draw(canvas);
        }
    }

    public void setShadowTopOffset(int shadowTopOffset) {
        this.mShadowTopOffset = shadowTopOffset;
        updateShadowBounds();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public void setShadowVisible(boolean shadowVisible, boolean animate) {
        this.mShadowVisible = shadowVisible;
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator = null;
        }

        if (animate && mShadowDrawable != null) {
            mAnimator = ObjectAnimator.ofFloat(this, SHADOW_ALPHA,
                    shadowVisible ? 0f : 1f, shadowVisible ? 1f : 0f);
            mAnimator.setDuration(100);
            mAnimator.start();
        }

        ViewCompat.postInvalidateOnAnimation(this);
        setWillNotDraw(!mShadowVisible || mShadowDrawable == null);
    }

    private static Property<DrawShadowFrameLayout, Float> SHADOW_ALPHA
            = new Property<DrawShadowFrameLayout, Float>(Float.class, "shadowAlpha") {
        @Override
        public Float get(DrawShadowFrameLayout object) {
            return object.mAlpha;
        }

        @Override
        public void set(DrawShadowFrameLayout object, Float value) {
            object.mAlpha = value;
            ViewCompat.postInvalidateOnAnimation(object);
        }
    };
}
