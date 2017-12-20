package me.khrystal.eyepetizer.mvp.model

import android.content.Context
import io.reactivex.Observable
import me.khrystal.eyepetizer.mvp.model.bean.HotBean
import me.khrystal.eyepetizer.network.ApiService
import me.khrystal.eyepetizer.network.RetrofitClient

/**
 *
 * usage:
 * author: kHRYSTAL
 * create time: 17/12/19
 * update time:
 * email: 723526676@qq.com
 */
class ResultModel {
    fun loadData(context: Context, query: String, start: Int): Observable<HotBean>? {
        val retrofitClient = RetrofitClient.getInstance(context, ApiService.BASE_URL)
        val apiService = retrofitClient.create(ApiService::class.java)
        return apiService?.getSearchData(10, query, start)

    }
}