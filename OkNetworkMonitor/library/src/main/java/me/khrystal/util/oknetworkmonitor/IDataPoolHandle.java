package me.khrystal.util.oknetworkmonitor;

import java.util.HashMap;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/6/11
 * update time:
 * email: 723526676@qq.com
 */

public interface IDataPoolHandle {
    void initDataPool();
    void clearDataPool();
    void addNetworkFeedData(String key, NetworkFeedModel networkFeedModel);
    void removeNetworkFeedData(String key);
    HashMap<String, NetworkFeedModel> getNetworkFeedMap();
    NetworkFeedModel getNetworkFeedModel(String requestId);
}
