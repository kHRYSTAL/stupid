package me.khrystal.dynamicfragmentstatepageradapter;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/6/5
 * update time:
 * email: 723526676@qq.com
 */
public class TabsViewModel extends ViewModel {

    private MutableLiveData<List<Subject>> mTabsLiveData;

    public MutableLiveData<List<Subject>> getTabsLiveData() {
        if (mTabsLiveData == null) {
            mTabsLiveData = new MutableLiveData<>();
            mTabsLiveData.setValue(DataUtils.getVisible());
        }
        return mTabsLiveData;
    }

    public void setSubjects(List<Subject> subjects) {
        if (mTabsLiveData == null) {
            mTabsLiveData = new MutableLiveData<>();
        }
        mTabsLiveData.setValue(subjects);
    }
}
