package me.khrystal.widget.menu.genericity;

import java.util.ArrayList;

/**
 * usage: 二级菜单泛型接口
 * author: kHRYSTAL
 * create time: 17/4/24
 * update time:
 * email: 723526676@qq.com
 */

public interface ChildAble<T> {
    // 一级列表对象获取二级列表
    public ArrayList<T> getChild();

    // 实现该接口时需要增加成员变量用于控件内部配置
    public void setIsParent(boolean isParent);
    public boolean isParent();

    // 输入到控件条目上的字符串
    public String toText();
}
