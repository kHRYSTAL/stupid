package me.khrystal.suspensionwindow.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/10/26
 * update time:
 * email: 723526676@qq.com
 */
public class ArticleBean implements Parcelable {

    private int id;
    private String imageUrl;
    private String title;
    private String time;
    private String jumpUrl;

    public ArticleBean(int id, String imageUrl, String title, String time, String jumpUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.time = time;
        this.jumpUrl = jumpUrl;
    }

    protected ArticleBean(Parcel souce) {
        this(souce.readInt(), souce.readString(), souce.readString(), souce.readString(),
                souce.readString());
    }

    public static final Creator<ArticleBean> CREATOR = new Creator<ArticleBean>() {
        @Override
        public ArticleBean createFromParcel(Parcel in) {
            return new ArticleBean(in);
        }

        @Override
        public ArticleBean[] newArray(int size) {
            return new ArticleBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(imageUrl);
        parcel.writeString(time);
        parcel.writeString(title);
        parcel.writeString(jumpUrl);
    }
}
