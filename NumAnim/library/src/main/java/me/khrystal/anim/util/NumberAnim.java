package me.khrystal.anim.util;

import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.Random;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/3/7
 * update time:
 * email: 723526676@qq.com
 */

public class NumberAnim {

    // 每秒刷新多少次
    private static final int COUNTPERS = 100;

    public static void startAnim(TextView textView, float number) {
        startAnim(textView, number, 500);
    }

    /**
     * @param timeMillis 从0至最终数字变换的时长
     */
    public static void startAnim(TextView textView, float number, long timeMillis) {
        if (number == 0) {
            textView.setText(NumberFormat(number, 2));
            return;
        }
        // 生成每次刷新变换的数字
        Float[] nums = splitNum(number, (int) ((timeMillis / 1000f) * COUNTPERS));
        Counter counter = new Counter(textView, nums, timeMillis);
        textView.removeCallbacks(counter);
        textView.post(counter);
    }

    public static String NumberFormat(float f,int m){
        return String.format("%."+m+"f",f);
    }

    public static float NumberFormatFloat(float f,int m){
        String strfloat = NumberFormat(f,m);
        return Float.parseFloat(strfloat);
    }

    private static Float[] splitNum(float number, int count) {
        Random random = new Random();
        float numberTemp = number;
        float sum = 0;
        LinkedList<Float> nums = new LinkedList<>();
        nums.add(0f);
        while (true) {
            float nextFloat = NumberFormatFloat((random.nextFloat() * number * 2f) / (float) count, 2);
            if (numberTemp - nextFloat >= 0) {
                sum = NumberFormatFloat(sum + nextFloat, 2);
                nums.add(sum);
                numberTemp -= nextFloat;
            } else {
                nums.add(number);
                return nums.toArray(new Float[0]);
            }
        }
    }

    static class Counter implements Runnable {

        private WeakReference<TextView> mRef;
        private Float[] nums;
        private long perTime;
        private int i;

        Counter(TextView textView, Float[] nums, long timeMillis) {
            mRef = new WeakReference<TextView>(textView);
            this.nums = nums;
            this.perTime = timeMillis / nums.length;
        }

        @Override
        public void run() {
            TextView textView = mRef.get();
            if (textView != null) {
                if (i > nums.length - 1) {
                    textView.removeCallbacks(Counter.this);
                    return;
                }
                textView.setText(NumberFormat(nums[i++], 2));
                textView.removeCallbacks(Counter.this);
                textView.postDelayed(Counter.this, perTime);
            }

        }
    }
}
