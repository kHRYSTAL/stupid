package me.khrystal.widget.popupwindow;

/**
 * usage: 职位POJO对象
 * author: kHRYSTAL
 * create time: 18/9/4
 * update time:
 * email: 723526676@qq.com
 */
public class JobItem {
    // 职位名词
    private String jobText;
    // 职位id
    private long jobId;
    // 职位权重
    private int jobWeight;
    // local属性 是否被选中
    private boolean selected;
    // local属性 是否吸附
    private boolean sticky;

    public JobItem(String jobText, long jobId) {
        this.jobText = jobText;
        this.jobId = jobId;
    }

    public void setJobText(String jobText) {
        this.jobText = jobText;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public void setJobWeight(int jobWeight) {
        this.jobWeight = jobWeight;
    }

    /**
     * set selected flag
     * @param selected selected flag to indicate the item is selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * @param sticky true for sticky, pop up sends events but not disappear
     */
    public void setSticky(boolean sticky) {
        this.sticky = sticky;
    }

    public String getJobText() {
        return jobText;
    }

    public long getJobId() {
        return jobId;
    }

    public int getJobWeight() {
        return jobWeight;
    }

    /**
     * check if item is selected
     * @return
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @return true if item is sticky, menu stays visible after press
     */
    public boolean isSticky() {
        return sticky;
    }
}
