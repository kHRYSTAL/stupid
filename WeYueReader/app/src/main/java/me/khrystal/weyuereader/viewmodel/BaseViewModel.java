package me.khrystal.weyuereader.viewmodel;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import me.khrystal.weyuereader.utils.SharedPreUtils;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/24
 * update time:
 * email: 723526676@qq.com
 */

public class BaseViewModel {
    protected Context mContext;
    private List<Disposable> disposables = new ArrayList<>();

    public BaseViewModel(Context mContext) {
        this.mContext = mContext;
    }

    public void addDisposable(Disposable disposable) {
        disposables.add(disposable);
    }

    public Map tokenMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("access-token", SharedPreUtils.getInstance().getString("token", "weyue"));
        map.put("app-type", "Android");
        return map;
    }

    public void onDestory() {
        if (disposables.size() > 0) {
            for (Disposable disposable: disposables) {
                disposable.dispose();
            }
            disposables.clear();
        }
    }
}
