package com.okhttp.builder;


import okhttp3.MediaType;

/**
 * Created by zhy on 15/12/14.
 */
public class PostStringBuilder extends OkHttpRequestBuilder<PostStringBuilder>
{
    private String content;
    private MediaType mediaType;


    public PostStringBuilder content(String content)
    {
        this.content = content;
        return this;
    }

    public PostStringBuilder mediaType(MediaType mediaType)
    {
        this.mediaType = mediaType;
        return this;
    }

    @Override
    public com.okhttp.request.RequestCall build()
    {
        return new com.okhttp.request.PostStringRequest(url, tag, params, headers, content, mediaType,id).build();
    }


}
