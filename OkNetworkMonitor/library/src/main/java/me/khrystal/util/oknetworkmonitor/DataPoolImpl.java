package me.khrystal.util.oknetworkmonitor;

import java.util.HashMap;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/6/11
 * update time:
 * email: 723526676@qq.com
 */

public class DataPoolImpl implements IDataPoolHandle {

    private static DataPoolImpl sDataPoolImpl;
    private HashMap<String, NetworkFeedModel> mNetworkFeedMap;

    public static DataPoolImpl getInstance() {
        if (sDataPoolImpl == null) {
            sDataPoolImpl = new DataPoolImpl();
        }
        return sDataPoolImpl;
    }

    private DataPoolImpl() {
        initDataPool();
    }

    @Override
    public void initDataPool() {
        if (mNetworkFeedMap == null) {
            mNetworkFeedMap = new HashMap<>();
        }
        mNetworkFeedMap.clear();
    }

    @Override
    public void clearDataPool() {
        if (mNetworkFeedMap != null) {
            mNetworkFeedMap.clear();
        }
    }

    @Override
    public void addNetworkFeedData(String key, NetworkFeedModel networkFeedModel) {
        if (mNetworkFeedMap == null) {
            initDataPool();
        }
        mNetworkFeedMap.put(key, networkFeedModel);
    }

    @Override
    public void removeNetworkFeedData(String key) {
        if (mNetworkFeedMap == null) {
            initDataPool();
            return;
        }
        if (mNetworkFeedMap.containsKey(key)) {
            mNetworkFeedMap.remove(key);
        }
    }

    @Override
    public HashMap<String, NetworkFeedModel> getNetworkFeedMap() {
        return mNetworkFeedMap;
    }

    @Override
    public NetworkFeedModel getNetworkFeedModel(String requestId) {
        if (mNetworkFeedMap == null) {
            initDataPool();
        }
        NetworkFeedModel networkFeedModel = mNetworkFeedMap.get(requestId);
        if (networkFeedModel == null) {
            networkFeedModel = new NetworkFeedModel();
            networkFeedModel.setRequestId(requestId);
            networkFeedModel.setCreateTime(System.currentTimeMillis());
            mNetworkFeedMap.put(requestId, networkFeedModel);
        }
        return networkFeedModel;
    }
}
