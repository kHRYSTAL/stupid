package com.xiayiye.openwechatapp.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xiayiye.openwechatapp.AppConstant;


import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static String TAG = "MicroMsg.WXEntryActivity";

    private IWXAPI api;
    private MyHandler handler;

    private static class MyHandler extends Handler {
        private final WeakReference<WXEntryActivity> wxEntryActivityWeakReference;

        public MyHandler(WXEntryActivity wxEntryActivity) {
            wxEntryActivityWeakReference = new WeakReference<WXEntryActivity>(wxEntryActivity);
        }

        @Override
        public void handleMessage(Message msg) {

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, AppConstant.appId, false);
        handler = new MyHandler(this);

        try {
            Intent intent = getIntent();
            api.handleIntent(intent, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) {
            String msg = "";
            WXLaunchMiniProgram.Resp launchMiniProResp = (WXLaunchMiniProgram.Resp) resp;
            //对应小程序组件 <button open-type="launchApp"> 中的 app-parameter 属性
            String extraData = launchMiniProResp.extMsg;
            Toast.makeText(this, "小程序返回参数：" + extraData, Toast.LENGTH_LONG).show();
            Log.e("打印参数", extraData);
            try {
                JSONObject jsonObject = new JSONObject(extraData);
                if (extraData.contains("msg")) {
                    msg = jsonObject.optString("msg");
                }
                if (extraData.contains("status")) {
                    String status = jsonObject.optString("status");
                    switch (status) {
                        case "0":
                            //支付成功
                            Toast.makeText(this, "支付成功", Toast.LENGTH_LONG).show();
                            break;
                        case "-1":
                            //支付失败
                            Toast.makeText(this, "支付失败:" + msg, Toast.LENGTH_LONG).show();
                            break;
                        case "-2":
                            //取消支付
                            Toast.makeText(this, "取消支付", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        finish();
    }
}