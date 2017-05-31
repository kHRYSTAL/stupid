package me.khrystal.widget.rotatingtextview.model;

import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Typeface;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/5/31
 * update time:
 * email: 723526676@qq.com
 */

public class Rotatable {
    private int color = Color.BLACK;
    private String[] text;
    private int updateDuration = 2000;
    private int animationDuration = 1000;
    private int currentWordNumber;

    private float size = 24.0f;
    private int strokeWidth = -1;

    private Path pathIn, pathOut;

    private Interpolator interpolator = new LinearInterpolator();

    private Typeface typeface;

    private boolean isCenter = false;

    private boolean isUpdated = false;

    public Rotatable(int updateDuration, String... text) {
        this.updateDuration = updateDuration;
        this.text = text;
        currentWordNumber = -1;
    }

    public Rotatable(int color, int updateDuration, String... text) {
        this.color = color;
        this.updateDuration = updateDuration;
        this.text = text;
        currentWordNumber = -1;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        setUpdated(true);
    }

    public String[] getText() {
        return text;
    }

    public void setText(String... text) {
        this.text = text;
    }

    public int getUpdateDuration() {
        return updateDuration;
    }

    public void setUpdateDuration(int updateDuration) {
        this.updateDuration = updateDuration;
        setUpdated(true);
    }

    /**
     * get next string index of the array
     */
    public int getNextWordNumber() {
        currentWordNumber = (currentWordNumber + 1) % text.length;
        return currentWordNumber;
    }

    public String peekNextWord() {
        return text[(currentWordNumber + 1) % text.length];
    }

    public String getNextWord() {
        return text[getNextWordNumber()];
    }

    public String getPerviousWord() {
        if (currentWordNumber <= 0)
            return text[(text.length - 1)];
        else
            return text[currentWordNumber - 1];
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
        setUpdated(true);
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public int getAnimationDuration() {
        return animationDuration;
    }

    public void setAnimationDuration(int animationDuration) {
        this.animationDuration = animationDuration;
        setUpdated(true);
    }

    public Path getPathIn() {
        return pathIn;
    }

    public void setPathIn(Path pathIn) {
        this.pathIn = pathIn;
    }

    public Path getPathOut() {
        return pathOut;
    }

    public void setPathOut(Path pathOut) {
        this.pathOut = pathOut;
    }

    public Interpolator getInterpolator() {
        return interpolator;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
        setUpdated(true);
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
        setUpdated(true);
    }

    /**
     * get the largest word of the text array
     */
    public String getLargestWord() {
        String largest = "";
        for (String s : text) {
            if (s.length() > largest.length()) {
                largest = s;
            }
        }
        return largest + " ";
    }

    public boolean isCenter() {
        return isCenter;
    }

    public void setCenter(boolean center) {
        isCenter = center;
        setUpdated(true);
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setUpdated(boolean isUpdated) {
        this.isUpdated = isUpdated;
    }

}
