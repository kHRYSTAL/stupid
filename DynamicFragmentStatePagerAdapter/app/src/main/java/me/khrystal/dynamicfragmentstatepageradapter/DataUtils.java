package me.khrystal.dynamicfragmentstatepageradapter;

import java.util.ArrayList;
import java.util.List;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/6/5
 * update time:
 * email: 723526676@qq.com
 */
public class DataUtils {

    private static List<Subject> subjects = new ArrayList<>();

    static {
        Subject subject = new Subject();
        subject.setId(1);
        subject.setSort(1);
        subject.setName("数学");
        subjects.add(subject);

        Subject subject2 = new Subject();
        subject2.setId(2);
        subject2.setName("语文");
        subject.setSort(2);
        subjects.add(subject2);

        Subject subject3 = new Subject();
        subject3.setId(3);
        subject3.setName("音乐");
        subject3.setSort(3);
        subjects.add(subject3);

        Subject subject4 = new Subject();
        subject4.setId(4);
        subject4.setSort(4);
        subject4.setName("美术");
        subjects.add(subject4);
    }

    private static List<Subject> subjects2 = new ArrayList<>();

    static {
        Subject subject = new Subject();
        subject.setId(5);
        subject.setSort(5);
        subject.setName("化学");
        subjects2.add(subject);

        Subject subject2 = new Subject();
        subject2.setId(6);
        subject2.setSort(6);
        subject2.setName("生物");
        subjects2.add(subject2);
    }

    public static List<Subject> getVisible() {
        return subjects;
    }

    public static List<Subject> getInvisible() {
        return subjects2;
    }

    public static void setVisible(List<Subject> subjects) {
        DataUtils.subjects = subjects;
    }

    public static void setInvisible(List<Subject> subjects2) {
        DataUtils.subjects2 = subjects2;
    }
}
