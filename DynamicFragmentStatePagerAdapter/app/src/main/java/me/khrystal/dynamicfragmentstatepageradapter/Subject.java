package me.khrystal.dynamicfragmentstatepageradapter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/6/5
 * update time:
 * email: 723526676@qq.com
 */
public class Subject implements Parcelable {


    private int id;

    private String name;

    private String ename;

    private String icon;

    private int sort;

    private boolean visible = true;

    private int gradeId;

    // 辅助变量 不是实体属性
    private boolean isSelect = false;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEname() {
        return ename;
    }

    public String getIcon() {
        return icon;
    }

    public int getSort() {
        return sort;
    }

    public boolean isVisible() {
        return visible;
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public Subject() {
    }

    protected Subject(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.ename = in.readString();
        this.icon = in.readString();
        this.sort = in.readInt();
        this.visible = in.readByte() != 0;
        this.isSelect = in.readByte() != 0;
    }

    public static final Creator<Subject> CREATOR = new Creator<Subject>() {
        @Override
        public Subject createFromParcel(Parcel in) {
            return new Subject(in);
        }

        @Override
        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.ename);
        dest.writeString(this.icon);
        dest.writeInt(this.sort);
        dest.writeByte(this.visible ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Subject subject = new Subject();
        subject.setEname(ename);
        subject.setName(name);
        subject.setIcon(icon);
        subject.setId(id);
        subject.setSelect(isSelect);
        subject.setSort(sort);
        subject.setVisible(visible);
        return subject;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj){
            return true;
        }else {
            if (obj instanceof Subject){

                Subject value = (Subject)obj;

                if (value.getId() == id){
                    return true;
                }else {
                    return false;
                }
            }else {
                return false;
            }
        }
    }
}
