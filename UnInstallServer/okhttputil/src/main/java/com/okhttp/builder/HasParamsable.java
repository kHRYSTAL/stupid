package com.okhttp.builder;

import java.util.Map;

/**
 * Created by zhy on 16/3/1.
 */
public interface HasParamsable
{
    com.okhttp.builder.OkHttpRequestBuilder params(Map<String, String> params);
    com.okhttp.builder.OkHttpRequestBuilder addParams(String key, String val);
}
