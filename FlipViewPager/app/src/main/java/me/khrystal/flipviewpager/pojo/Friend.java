package me.khrystal.flipviewpager.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/29
 * update time:
 * email: 723526676@qq.com
 */
public class Friend implements Serializable {

    private int avatar;
    private String nickname;
    private int background;
    private List<String> interests = new ArrayList<>();

    public Friend(int avatar, String nickname, int background, String... interest) {
        this.avatar = avatar;
        this.nickname = nickname;
        this.background = background;
        interests.addAll(Arrays.asList(interest));
    }

    public int getAvatar() {
        return avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public int getBackground() {
        return background;
    }

    public List<String> getInterests() {
        return interests;
    }
}
