package me.khrystal.util.mediamonitor;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/8/4
 * update time:
 * email: 723526676@qq.com
 */

public class Directory {
    private String mDirectory;
    private @Rule int mRule;

    public Directory(String directory) {
        this(directory, Rule.LIKE);
    }

    public Directory(String directory, @Rule int rule) {
        this.mDirectory = directory;
        this.mRule = rule;
    }

    public String getDirectory() {
        return mDirectory;
    }

    public void setDirectory(String directory) {
        this.mDirectory = directory;
    }

    public @Rule int getRule() {
        return mRule;
    }

    public void setRule(@Rule int rule) {
        this.mRule = rule;
    }

}
