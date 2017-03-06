package com.okhttp.builder;


import com.okhttp.OkHttpUtils;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public com.okhttp.request.RequestCall build()
    {
        return new com.okhttp.request.OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
