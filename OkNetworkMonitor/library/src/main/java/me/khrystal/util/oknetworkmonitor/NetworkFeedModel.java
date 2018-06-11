package me.khrystal.util.oknetworkmonitor;

import java.util.Map;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/6/11
 * update time:
 * email: 723526676@qq.com
 */

public class NetworkFeedModel {
    private String mRequestId;
    private String mUrl;
    private String mHost;
    private String mMethod;
    private Map<String, String> mRequestHeaderMap;

    private String mName;
    private int mStatus;
    private int mSize;
    private long mCostTime;
    private String mContentType;
    private String mBody;
    private Map<String, String> mResponseHeaderMap;

    private long mCreateTime;

    public String getRequestId() {
        return mRequestId;
    }

    public void setRequestId(String requestId) {
        this.mRequestId = requestId;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public void setHost(String host) {
        this.mHost = host;
    }

    public void setMethod(String method) {
        this.mMethod = method;
    }

    public void setRequestHeaderMap(Map<String, String> requestHeaderMap) {
        this.mRequestHeaderMap = requestHeaderMap;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setStatus(int status) {
        this.mStatus = status;
    }

    public void setSize(int size) {
        this.mSize = size;
    }

    public void setCostTime(long costTime) {
        this.mCostTime = costTime;
    }

    public void setContentType(String contentType) {
        this.mContentType = contentType;
    }

    public void setBody(String body) {
        this.mBody = body;
    }

    public void setResponseHeaderMap(Map<String, String> responseHeaderMap) {
        this.mResponseHeaderMap = responseHeaderMap;
    }

    public void setCreateTime(long createTime) {
        this.mCreateTime = createTime;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getHost() {
        return mHost;
    }

    public String getMethod() {
        return mMethod;
    }

    public Map<String, String> getRequestHeaderMap() {
        return mRequestHeaderMap;
    }

    public String getName() {
        return mName;
    }

    public int getStatus() {
        return mStatus;
    }

    public int getSize() {
        return mSize;
    }

    public long getCostTime() {
        return mCostTime;
    }

    public String getContentType() {
        return mContentType;
    }

    public String getBody() {
        return mBody;
    }

    public Map<String, String> getResponseHeaderMap() {
        return mResponseHeaderMap;
    }

    public long getCreateTime() {
        return mCreateTime;
    }
}
