package me.khrystal.weyuereader.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;

import me.khrystal.weyuereader.model.BookBean;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/23
 * update time:
 * email: 723526676@qq.com
 */
@Entity
public class UserBean {
    @Id
    public String name;
    public String password;
    public String icon;
    public String brief;
    public String token;
    public String nickName;
    @Transient
    public List<BookBean> likebooks;

    @Generated(hash = 1872744908)
    public UserBean(String name, String password, String icon, String brief, String token, String nickName) {
        this.name = name;
        this.password = password;
        this.icon = icon;
        this.brief = brief;
        this.token = token;
        this.nickName = nickName;
    }

    @Generated(hash = 1203313951)
    public UserBean() {
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getIcon() {
        return icon;
    }

    public String getBrief() {
        return brief;
    }

    public String getToken() {
        return token;
    }

    public String getNickName() {
        return nickName;
    }

    public List<BookBean> getLikebooks() {
        return likebooks;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setLikebooks(List<BookBean> likebooks) {
        this.likebooks = likebooks;
    }
}
