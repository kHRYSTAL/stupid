package me.khrystal.widget;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import static me.khrystal.widget.Utils.dp2px;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/6/1
 * update time:
 * email: 723526676@qq.com
 */

public class BarItem extends RelativeLayout {

    public static final String DEFAULT_UNSELECTED_COLOR = "#E0E0E0";
    public static final String DEFAULT_SELECTED_COLOR = "#E53935";
    public static final String DEFAULT_UNSELECTED_ICON_COLOR = "#000000";
    public static final String DEFAULT_SELECTED_ICON_COLOR = "#FFFFFF";
    private static final int DEFAULT_RADIUS = (int) dp2px(25);

    private final Context context;
    private ImageView imageView;
    private boolean selected;

    private int selectedColor;
    private int unSelectedColor;

    private int selectedIconColor;
    private int unSelectedIconColor;

    private int diameter;

    public BarItem(Context context) {
        super(context);
        this.context = context;
        init(null);
    }

    public BarItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public BarItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BarItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init(attrs);
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            makeSelected();
        } else {
            makeUnSelected();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // get radius
        getLayoutParams().height = diameter;
        getLayoutParams().width = diameter;
    }

    private void init(AttributeSet attrs) {
        // Get Radius
        diameter = getRadius(attrs) * 2;
        // Get Selected Status
        selected = getSelectedStatus(attrs);
        // Get icon from attributes
        Drawable icon = getIcon(attrs);
        // Get selected / unselected color
        unSelectedColor = getUnSelectedColor(attrs);
        selectedColor = getSelectedColor(attrs);

        // get selected/unselected color for icon
        unSelectedIconColor = getUnSelectedIconColor(attrs);
        selectedIconColor = getSelectedIconColor(attrs);
        // add background to item
        setBackgroundResource(R.drawable.round_background);
        // add imageView
        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT); // a position in layout
        imageView = new ImageView(context);
        imageView.setLayoutParams(layoutParams);
        if (icon != null) {
            imageView.setImageDrawable(icon);
        }
        addView(imageView);
        if (selected) {
            makeSelected();
        }
        setInitialColors();
    }

    /**
     * set initial color of the barItem according to the attributes
     */
    private void setInitialColors() {
        if (selected) {
            getBackground().setColorFilter(selectedColor, PorterDuff.Mode.SRC_IN);
            imageView.setColorFilter(selectedIconColor);
        } else {
            getBackground().setColorFilter(unSelectedColor, PorterDuff.Mode.SRC_IN);
            imageView.setColorFilter(unSelectedIconColor);
        }
    }

    /**
     * set initial state of the barItem according to the attributes
     *
     * @param attrs
     * @return
     */
    private boolean getSelectedStatus(AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BarItem, 0, 0);
        try {
            return ta.getBoolean(R.styleable.BarItem_selected, false);
        } catch (Exception e) {
            return false;
        } finally {
            ta.recycle();
        }
    }

    /**
     * get radius from the attributes
     *
     * @param attrs
     * @return
     */
    private int getRadius(AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BarItem, 0, 0);
        try {
            return (int) ta.getDimension(R.styleable.BarItem_radius, DEFAULT_RADIUS);
        } catch (Exception e) {
            return DEFAULT_RADIUS;
        } finally {
            ta.recycle();
        }
    }

    /**
     * get unselected color for barItem from the attribute
     *
     * @param attrs
     * @return
     */
    private int getUnSelectedColor(AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BarItem, 0, 0);
        try {
            return ta.getColor(R.styleable.BarItem_unSelectedColor, Color.parseColor(DEFAULT_UNSELECTED_COLOR));
        } catch (Exception e) {
            return Color.parseColor(DEFAULT_UNSELECTED_COLOR);
        } finally {
            ta.recycle();
        }
    }

    /**
     * get unselected color for icon from the attribute
     *
     * @param attrs
     * @return
     */
    private int getUnSelectedIconColor(AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BarItem, 0, 0);
        try {
            return ta.getColor(R.styleable.BarItem_unSelectedIconColor, Color.parseColor(DEFAULT_UNSELECTED_ICON_COLOR));
        } catch (Exception e) {
            return Color.parseColor(DEFAULT_UNSELECTED_ICON_COLOR);
        } finally {
            ta.recycle();
        }
    }

    /**
     * get selected color for barItem from the attribute
     *
     * @param attrs
     * @return
     */
    private int getSelectedColor(AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BarItem, 0, 0);
        try {
            return ta.getColor(R.styleable.BarItem_selectedColor, Color.parseColor(DEFAULT_SELECTED_COLOR));
        } catch (Exception e) {
            return Color.parseColor(DEFAULT_SELECTED_COLOR);
        } finally {
            ta.recycle();
        }
    }

    /**
     * get selected color for icon from the attribute
     * @param attrs
     * @return
     */
    private int getSelectedIconColor(AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BarItem, 0, 0);
        try {
            return ta.getColor(R.styleable.BarItem_selectedIconColor, Color.parseColor(DEFAULT_SELECTED_ICON_COLOR));
        } catch (Exception e) {
            return Color.parseColor(DEFAULT_SELECTED_ICON_COLOR);
        } finally {
            ta.recycle();
        }
    }

    /**
     * get icon from attributes
     * @param attrs
     * @return
     */
    private Drawable getIcon(AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BarItem, 0, 0);
        try {
            return ta.getDrawable(R.styleable.BarItem_icon);
        } catch (Exception e) {
            return null;
        } finally {
            ta.recycle();
        }
    }

    /**
     * make barItem selected
     */
    private void makeSelected() {
        ResizeWidthAnimation animation = new ResizeWidthAnimation(this, (diameter + (diameter * 40) / 100));
        animation.setDuration(250);
        animation.setInterpolator(new BounceInterpolator(1, 1));
        startAnimation(animation);
        animateColor(this, unSelectedColor, selectedColor);
        animateColor(imageView, unSelectedIconColor, selectedIconColor);
    }

    /**
     * make barItem unSelected
     */
    private void makeUnSelected() {
        ResizeWidthAnimation animation = new ResizeWidthAnimation(this, diameter);
        animation.setDuration(250);
        animation.setInterpolator(new BounceInterpolator(1, 1));
        startAnimation(animation);
        animateColor(this, selectedColor, unSelectedColor);
        animateColor(imageView, selectedIconColor, unSelectedIconColor);
    }

    /**
     * animate color on the view
     * @param view
     * @param fromColor
     * @param toColor
     */
    public void animateColor(final View view, int fromColor, int toColor) {
        ValueAnimator valueAnimator;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            valueAnimator = ValueAnimator.ofArgb(fromColor, toColor);
        } else {
            valueAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
        }
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (view instanceof ImageView) {
                    ((ImageView) view).setColorFilter((Integer) valueAnimator.getAnimatedValue());
                } else {
                    getBackground().setColorFilter((Integer) valueAnimator.getAnimatedValue(), PorterDuff.Mode.SRC_IN);
                }
            }
        });
        valueAnimator.setDuration(300);
        valueAnimator.start();
    }
}
