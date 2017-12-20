package me.khrystal.eyepetizer.ui

import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.gyf.barlibrary.ImmersionBar
import kotlinx.android.synthetic.main.activity_watch.*
import me.khrystal.eyepetizer.R
import me.khrystal.eyepetizer.adapter.WatchAdapter
import me.khrystal.eyepetizer.mvp.model.bean.VideoBean
import me.khrystal.eyepetizer.utils.ObjectSaveUtils
import me.khrystal.eyepetizer.utils.SPUtils

/**
 *
 * usage:
 * author: kHRYSTAL
 * create time: 17/12/20
 * update time:
 * email: 723526676@qq.com
 */
class WatchActivity: AppCompatActivity() {
    var mList = ArrayList<VideoBean>()
    lateinit var mAdapter: WatchAdapter
    var mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            var list = msg?.data?.getParcelableArrayList<VideoBean>("beans")
            if(list?.size?.compareTo(0) == 0){
                tv_hint.visibility = View.VISIBLE
            }else{
                tv_hint.visibility = View.GONE
                if(mList.size>0){
                    mList.clear()
                }
                list?.let { mList.addAll(it) }
                mAdapter.notifyDataSetChanged()
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this).transparentBar().barAlpha(0.3f).fitsSystemWindows(true).init()
        setContentView(R.layout.activity_watch)
        setToolbar()
        DataAsyncTask(mHandler,this).execute()
        recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = WatchAdapter(this, mList)
        recyclerView.adapter = mAdapter
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        var bar = supportActionBar
        bar?.title = "观看记录"
        bar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private class DataAsyncTask(handler: Handler, activity: WatchActivity) : AsyncTask<Void, Void, ArrayList<VideoBean>>() {
        var activity: WatchActivity = activity
        var handler = handler
        override fun doInBackground(vararg params: Void?): ArrayList<VideoBean>? {
            var list = ArrayList<VideoBean>()
            var count: Int = SPUtils.getInstance(activity, "beans").getInt("count")
            var i = 1
            while (i.compareTo(count) <= 0) {
                var bean :VideoBean= ObjectSaveUtils.getValue(activity, "bean$i") as VideoBean
                list.add(bean)
                i++
            }
            return list
        }

        override fun onPostExecute(result: ArrayList<VideoBean>?) {
            super.onPostExecute(result)
            var message = handler.obtainMessage()
            var bundle = Bundle()
            bundle.putParcelableArrayList("beans",result)
            message.data = bundle
            handler.sendMessage(message)
        }

    }
}