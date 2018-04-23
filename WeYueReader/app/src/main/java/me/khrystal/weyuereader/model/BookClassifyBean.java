package me.khrystal.weyuereader.model;

import java.io.Serializable;
import java.util.List;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/23
 * update time:
 * email: 723526676@qq.com
 */

public class BookClassifyBean implements Serializable {

    private List<ClassifyBean> press;
    private List<ClassifyBean> picture;
    private List<ClassifyBean> female;
    private List<ClassifyBean> male;

    public List<ClassifyBean> getPress() {
        return press;
    }

    public List<ClassifyBean> getPicture() {
        return picture;
    }

    public List<ClassifyBean> getFemale() {
        return female;
    }

    public List<ClassifyBean> getMale() {
        return male;
    }

    public void setPress(List<ClassifyBean> press) {
        this.press = press;
    }

    public void setPicture(List<ClassifyBean> picture) {
        this.picture = picture;
    }

    public void setFemale(List<ClassifyBean> female) {
        this.female = female;
    }

    public void setMale(List<ClassifyBean> male) {
        this.male = male;
    }

    public static class ClassifyBean implements Serializable {
        private String name;
        private String bookCount;
        private String monthlyCount;
        private String icon;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBookCount() {
            return bookCount;
        }

        public void setBookCount(String bookCount) {
            this.bookCount = bookCount;
        }

        public String getMonthlyCount() {
            return monthlyCount;
        }

        public void setMonthlyCount(String monthlyCount) {
            this.monthlyCount = monthlyCount;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
