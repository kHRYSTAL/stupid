package khrystal.me.circularlayout.widget;

import java.util.LinkedList;

import me.khrystal.widget.circularlayout.CircularLayoutAdapter;
import me.khrystal.widget.circularlayout.CircularLayoutItem;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/2/26
 * update time:
 * email: 723526676@qq.com
 */
public class SampleCircleLayoutAdapter extends CircularLayoutAdapter<Integer> {

    private LinkedList<Integer> adapter = new LinkedList<>();
    private int startingIndex = 0;
    private boolean isStartingIdSetten = false;

    public boolean setStartIndex(int startingIndex) {
        if (!isStartingIdSetten) {
            isStartingIdSetten = true;
            this.startingIndex = startingIndex;
            return true;
        }
        return false;
    }

    @Override
    public void add(Integer item) {
        adapter.add(item);
    }

    @Override
    public CircularLayoutItem get(int index) {
        Integer drawable = adapter.get(getRoundedIndex((getRoundedIndex(index) - startingIndex)));
        SampleCircleLayoutItem circleLayoutItem = new SampleCircleLayoutItem(context);
        circleLayoutItem.setBackground(context.getResources().getDrawable(drawable));
        circleLayoutItem.setIndex(index);
        return circleLayoutItem;
    }

    public int getSize() {
        return adapter.size();
    }

    @Override
    public Integer getRoundedIndex(Integer index) {
        try {
            int newIndex = ((-1 * index) % adapter.size() + adapter.size()) % adapter.size();
            return newIndex;
        } catch (Exception e) {
            return -1;
        }
    }

    public Integer getCurrentInteger() {
        return adapter.get(getRoundedIndex(getRoundedIndex(parent.getCurrentStep()) - startingIndex));
    }
}
