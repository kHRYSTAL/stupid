package me.khrystal.weyuereader.model;

public class ReadBgBean {
    private int bgColor;
    private boolean isSelect;


    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }
}