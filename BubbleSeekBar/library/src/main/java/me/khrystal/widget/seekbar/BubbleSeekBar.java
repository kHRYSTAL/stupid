package me.khrystal.widget.seekbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.math.BigDecimal;

import static me.khrystal.widget.seekbar.BubbleSeekBar.TextPosition.BELOW_SECTION_MARK;
import static me.khrystal.widget.seekbar.BubbleSeekBar.TextPosition.BOTTOM_SIDES;
import static me.khrystal.widget.seekbar.BubbleSeekBar.TextPosition.SIDES;
import static me.khrystal.widget.seekbar.BubbleUtils.dp2px;
import static me.khrystal.widget.seekbar.BubbleUtils.sp2px;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/6/19
 * update time:
 * email: 723526676@qq.com
 */

public class BubbleSeekBar extends View {

    

}
