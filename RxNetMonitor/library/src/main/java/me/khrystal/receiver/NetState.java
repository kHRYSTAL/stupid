package me.khrystal.receiver;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/5/11
 * update time:
 * email: 723526676@qq.com
 */

public class NetState {

    private @NetType.NetTypeChecker
    int netState;
    private boolean isNetConnect;

    public NetState(int netState, boolean isNetConnect) {
        this.netState = netState;
        this.isNetConnect = isNetConnect;
    }

    public int getNetState() {
        return netState;
    }

    public boolean isNetConnect() {
        return isNetConnect;
    }

    public void setNetState(int netState) {
        this.netState = netState;
    }

    public void setNetConnect(boolean netConnect) {
        isNetConnect = netConnect;
    }

    public String getNetStateName() {
        switch (this.netState) {
            case NetType.NONE:
                return "none";
            case NetType.CMNET:
                return "cmnet";
            case NetType.CMWAP:
                return "cmwap";
            case NetType.WIFI:
                return "wifi";
            default:
                return "none";
        }
    }
}
