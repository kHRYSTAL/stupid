package me.khrystal.widget.toast;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.StyleRes;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import static me.khrystal.widget.toast.Utils.getTypedValueInDP;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/2/13
 * update time:
 * email: 723526676@qq.com
 */

public class StyleableToast implements  OnToastFinished {

    private static final String TAG = "StyleableToast";
    private static final String DEFAULT_CONDENSED_FONT = "sans-serif-condensed";
    private static final int DEFAULT_BACKGROUND = Color.parseColor("#555555");
    private static final int DEFAULT_TEXT_COLOR = Color.WHITE;

    private static final int DEFAULT_TEXT_SIZE = 16;
    private static final int DEFAULT_CORNER_RADIUS = 25;
    private static final int DEFAULT_HORIZONTAL_PADDING = 25;
    private static final int DEFAULT_VERTICAL_PADDING = 11;
    private static final int DEFAULT_ALPHA = 230;
    private static int MAX_ALPHA = 255;

    private final Context context;
    private TextView textView;
    private Typeface font;

    private float strokeWidth;
    private int duration, style, alpha, drawable;
    private int backgroundColor, textColor, strokeColor;
    private int cornerRadius = -1;
    private boolean isBold;
    private boolean isAnimation;
    private String toastMsg;
    private float shapeCornerRadius;

    public StyleableToast(Context context) {
        this.context = context.getApplicationContext();
    }

    public StyleableToast(Context context, String toastMsg, int duration) {
        this.context = context.getApplicationContext();
        this.toastMsg = toastMsg;
        this.duration = duration;
    }

    public StyleableToast(Context context, String toastMsg, int duration, @StyleRes int styleId) {
        this.context = context.getApplicationContext();
        this.toastMsg = toastMsg;
        this.duration = duration;
        this.style = styleId;
    }

    public void setStyle(@StyleRes int style) {
        this.style = style;
    }

    public void setToastMsg(String toastMsg) {
        this.toastMsg = toastMsg;
    }

    public void setBoldText() {
        isBold = true;
    }

    public void setTextFont(Typeface typeface) {
        this.font = typeface;
    }

    public void setTextColor(@ColorInt int textColor) {
        this.textColor = textColor;
    }

    /**
     * todo
     * @param isBold
     * @param font
     */
    public void setTextStyle(boolean isBold, Typeface font) {
        this.isBold = isBold;
        this.font = font;
    }

    public void setTextStyle(@ColorInt int textColor, Typeface font) {
        this.textColor = textColor;
        this.font = font;
    }

    public void setTextStyle(@ColorInt int textColor, boolean isBold, Typeface font) {
        this.textColor = textColor;
        this.isBold = isBold;
        this.font = font;
    }
    
    public StyleableToast spinIcon() {
        isAnimation = true;
        return this;
    }
    
    public void setBackgroundColor(int duration) {
        this.duration = duration;
    }
    
    public void setToastStroke(int strokeWidth, @ColorInt int strokeColor) {
        this.strokeWidth = strokeWidth;
        this.strokeColor = strokeColor;
    }
    
    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
    }
    
    public void setMaxAlpha() {
        this.alpha = MAX_ALPHA;
    }
    
    public void setIcon (@DrawableRes int drawableRes) {
        this.drawable = drawableRes;
    }
    
    private Toast buildToast() {
        Toast toast = new Toast(context);
        toast.setDuration(duration);
        toast.setView(getToastLayout());
        return toast;
    }

    public View getToastLayout() {
        getImageViewStyleAttr();
        int hoarzontalPadding = (int) getTypedValueInDP(context, DEFAULT_HORIZONTAL_PADDING);
        int verticalPadding = (int) getTypedValueInDP(context, DEFAULT_VERTICAL_PADDING);
        RelativeLayout toastLayout = new RelativeLayout(context);
        toastLayout.setPadding(hoarzontalPadding, verticalPadding, hoarzontalPadding, verticalPadding);
        toastLayout.setBackground(getToastShape());
        toastLayout.addView(getTextView());

        if (drawable > 0) {
            toastLayout.addView(getIcon());
            toastLayout.setPadding(0, verticalPadding, 0, verticalPadding);
        }
        return toastLayout;
    }
    
    private TextView getTextView() {
        getTextStylesAttr();
        textView = new TextView(context);
        textView.setText(toastMsg);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE);
        textView.setTextColor(getTextColor());
        textView.setTypeface(getTypeface());
        textView.setMaxLines(2);

        if (drawable > 0) {

            //previous: 18x2  + 8
            int leftPadding = (int) getTypedValueInDP(context, 18 * 2 + 5);
            int rightPadding = (int) getTypedValueInDP(context, 22);

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            textView.setLayoutParams(layoutParams);

            //Make space between icon and textview / textview and right edge of the toast
            textView.setPadding(leftPadding, 0, rightPadding, 0);
        }

        return textView;
    }


    private void getImageViewStyleAttr() {
        if (style > 0) {
            int[] drawableAttrSet = {android.R.attr.icon};
            TypedArray drawableId = context.obtainStyledAttributes(style, drawableAttrSet);
            drawable = drawableId.getResourceId(0, 0);
            drawableId.recycle();
        }
    }

    private GradientDrawable getToastShape() {
        getToastShapeAttrs();
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(getShapeCornerRadius());
        gradientDrawable.setStroke((int) getStrokeWidth(), getStrokeColor());
        gradientDrawable.setColor(getBackgroundColor());
        gradientDrawable.setAlpha(getShapeAlpha());
        return gradientDrawable;
    }

    @SuppressWarnings("ResourceType")
    private void getToastShapeAttrs() {
        if (style > 0) {
            int[] colorAttrs = {android.R.attr.colorBackground, android.R.attr.strokeColor};
            int[] floatAttrs = {android.R.attr.alpha, android.R.attr.strokeWidth};
            int[] dimenAttrs = {android.R.attr.radius};

            TypedArray colors = context.obtainStyledAttributes(style, colorAttrs);
            TypedArray floats = context.obtainStyledAttributes(style, floatAttrs);
            TypedArray dimens = context.obtainStyledAttributes(style, dimenAttrs);

            backgroundColor = colors.getColor(0, DEFAULT_BACKGROUND);
            cornerRadius = (int) dimens.getDimension(0, DEFAULT_CORNER_RADIUS);
            alpha = (int) floats.getFloat(0, DEFAULT_ALPHA);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                strokeWidth = floats.getFloat(1, 0);
                strokeColor = colors.getColor(1, Color.TRANSPARENT);
            }

            colors.recycle();
            floats.recycle();
            dimens.recycle();
        }
    }

    private float getShapeCornerRadius() {
        if (cornerRadius > 0) {
            return getTypedValueInDP(context, cornerRadius);
        } else {
            return  getTypedValueInDP(context, DEFAULT_CORNER_RADIUS);
        }
    }

    private float getStrokeWidth() {
        return getTypedValueInDP(context, strokeWidth);
    }

    private int getStrokeColor() {
        return strokeColor;
    }


    private int getBackgroundColor() {
        if (backgroundColor == 0) {
            return DEFAULT_BACKGROUND;
        } else {
            return backgroundColor;
        }
    }

    private int getShapeAlpha() {
        if (alpha == 0) {
            return DEFAULT_ALPHA;
        } else {
            return alpha;
        }
    }

    private Typeface getTypeface() {
        if (isBold && font == null) {
            return Typeface.create(DEFAULT_CONDENSED_FONT, Typeface.BOLD);
        } else if (isBold && font != null) {
            return Typeface.create(font, Typeface.BOLD);
        } else if (font != null) {
            return Typeface.create(font, Typeface.NORMAL);
        } else {
            return Typeface.create(DEFAULT_CONDENSED_FONT, Typeface.NORMAL);
        }
    }

    private int getTextColor() {
        if (textColor == 0 && style <= 0) {
            return DEFAULT_TEXT_COLOR;
        } else {
            return textColor;
        }
    }

    private void getTextStylesAttr() {
        if (style > 0) {
            int[] colorAttrs = {android.R.attr.textColor};
            int[] stringAttrs = {android.R.attr.fontFamily};
            int[] intsAttrs = {android.R.attr.textStyle};

            TypedArray colors = context.obtainStyledAttributes(style, colorAttrs);
            TypedArray strings = context.obtainStyledAttributes(style, stringAttrs);
            TypedArray ints = context.obtainStyledAttributes(style, intsAttrs);

            textColor = colors.getColor(0, DEFAULT_TEXT_COLOR);
            String passedFont = strings.getString(0);

            if (passedFont != null && !passedFont.isEmpty()) {
                try {
                    if (passedFont.contains("fonts")) {
                        font = Typeface.createFromAsset(context.getAssets(), passedFont);
                    } else {
                        font = Typeface.create(passedFont, Typeface.NORMAL);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "font file not found");
                }
            }
            if (ints.getInt(0, 0) == 1) {
                isBold = true;
            } else {
                isBold = false;
            }

            colors.recycle();
            strings.recycle();
            ints.recycle();
        }
    }

    public View getIcon() {
        if (drawable > 0) {

            //previous 18:
            int marginLeft = (int) getTypedValueInDP(context, 15);
            int maxHeightVal = (int) getTypedValueInDP(context, 20);
            int maxWidthVal = (int) getTypedValueInDP(context, 20);

            ImageView imageView = new ImageView(context);
            imageView.setImageDrawable(context.getResources().getDrawable(drawable));
            imageView.setAnimation(getAnimation());
            imageView.setMaxWidth(marginLeft + maxWidthVal);
            imageView.setMaxHeight(maxHeightVal);
            imageView.setAdjustViewBounds(true);


            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);

            //Push the icon 15dp from the left edge of the shape
            layoutParams.setMargins(marginLeft, 0, 0, 0);

            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            imageView.setLayoutParams(layoutParams);
            return imageView;
        }
        return null;
    }

    private Animation getAnimation() {
        if (isAnimation) {
            RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setInterpolator(new LinearInterpolator());
            anim.setRepeatCount(Animation.INFINITE);
            anim.setDuration(1000);
            return anim;
        }

        return null;
    }


    @Override
    public void onToastFinished() {
        getAnimation().cancel();
        getAnimation().reset();
    }

    public static StyleableToast makeText(Context context, CharSequence text, int duration, int style) {
        StyleableToast styableToast = new StyleableToast(context, text.toString(), duration, style);
        return styableToast;
    }

    public void show() {
        buildToast().show();
        if (isAnimation) {
            ToastDurationWatcher durationWatcher = new ToastDurationWatcher(buildToast().getDuration(), this);
        }
    }
}
