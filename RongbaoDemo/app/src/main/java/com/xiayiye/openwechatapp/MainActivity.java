package com.xiayiye.openwechatapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.xiayiye.openwechatapp.token.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import com.xiayiye.openwechatapp.R;

/**
 * @author DELL
 * 2019年8月22日10:01:41
 */
public class MainActivity extends Activity {
    private EditText etAppId, etMerchantId, etMemberId, etLeger, etCredit, etOrderNo, etMoney;
    private static final String PAY_WX = "0";
    /**
     * 小程序的原始APPId
     */
    private String originalAppId = "gh_99fa76dc00e6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etAppId = (EditText) findViewById(R.id.et_app_id);
        etMerchantId = (EditText) findViewById(R.id.et_merchant_id);
        etMemberId = (EditText) findViewById(R.id.et_member_id);
        etLeger = (EditText) findViewById(R.id.et_leger);
        etCredit = (EditText) findViewById(R.id.et_credit);
        etOrderNo = (EditText) findViewById(R.id.et_order_no);
        etOrderNo.requestFocus();
        etMoney = (EditText) findViewById(R.id.et_money);
        CheckBox cbIsProd = findViewById(R.id.cb_isProd);
        etOrderNo.setText(new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.CHINA).format(new Date()));
        //默认生产环境
        cbIsProd.setChecked(true);
        cbIsProd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //生产环境
                    HttpUtils.TOKEN_URL = AppConstant.getTokenProdUrl;
                    etAppId.setText(AppConstant.smallAppIdTest);
                    etMerchantId.setText(AppConstant.merchantIdProd);
                    HttpUtils.MERCHANT_ID = AppConstant.merchantIdProd;
                    etLeger.setText(AppConstant.legerParamProd);
                    originalAppId = AppConstant.originalAppIdTest;
                } else {
                    //测试环境
                    HttpUtils.TOKEN_URL = AppConstant.getTokenTestUrl;
                    etAppId.setText(AppConstant.smallAppIdTest);
                    etMerchantId.setText(AppConstant.merchantIdTest);
                    HttpUtils.MERCHANT_ID = AppConstant.merchantIdTest;
                    etLeger.setText(AppConstant.legerParamTest);
                    originalAppId = AppConstant.originalAppIdTest;
                }
            }
        });

    }

    public void openApp(View view) {
        //准备支付参数
        String isCredit = etCredit.getText().toString();
        String orderNo = etOrderNo.getText().toString();
        if (TextUtils.isEmpty(orderNo)) {
            Toast.makeText(this, "订单号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(isCredit) || !("NO".equals(isCredit) || "YES".equals(isCredit))) {
            Toast.makeText(this, "请填写正确信用卡字段YES或者NO", Toast.LENGTH_SHORT).show();
            return;
        }

        final PreOrder preOrder = new PreOrder();
        preOrder.setSub_appid(etAppId.getText().toString());
        preOrder.setBody("商品");
        preOrder.setClientType("0");
        preOrder.setIs_credit(isCredit);
        preOrder.setNotify_url("http://wap.tenpay.com/tenpay.asp");
        preOrder.setMember_id(etMemberId.getText().toString());
        preOrder.setMerchant_id(etMerchantId.getText().toString());
        preOrder.setOut_trade_no(orderNo);
        preOrder.setSeller_email("demo@163.com");
        preOrder.setTitle("商品title");
        preOrder.setTranstime(new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date()));
        preOrder.setTotal_fee(etMoney.getText().toString());
        preOrder.setLedger_info(etLeger.getText().toString());
        preOrder.setMember_ip("127.0.0.1");
//        payType :0普通版，1：分账版
        preOrder.setPay_type("1");
        preOrder.setTerminal_type("mobile_android");
        preOrder.setTerminal_info("7XHSDHSH_hshs12");

        //获取token
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("获取token");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        //获取Token的方法
        startWxSmallApp(preOrder, progressDialog);
    }

    /**
     * 获取token参数打开微信小程序的方法
     *
     * @param preOrder       参数
     * @param progressDialog 弹框提示
     */
    private void startWxSmallApp(final PreOrder preOrder, final ProgressDialog progressDialog) {
        int corePoolSize = Runtime.getRuntime().availableProcessors() * 2 + 1;
        int maxNumPoolSize = corePoolSize + 1;
        long keepAliveTime = 1;
        TimeUnit unit = TimeUnit.HOURS;
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
                maxNumPoolSize,
                keepAliveTime,
                unit, new LinkedBlockingQueue<Runnable>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                String response = HttpUtils.getToken(preOrder);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String resultCode = jsonObject.getString("result_code");
                    final String resultMsg = jsonObject.getString("result_msg");
                    if ("0000".equals(resultCode)) {
                        final String token = jsonObject.getString("token");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                preOrder.setToken(token);
                                String merchantId = etMerchantId.getText().toString();
                                String merchantOrderNo = etOrderNo.getText().toString();
//                                String param = "pages/ucenter/orderDetail/orderDetail?"
                                String param = "pages/index/index?"
                                        + "merchant_id=" + merchantId
                                        + "&token=" + token
                                        + "&total_fee=" + etMoney.getText().toString().trim()
                                        + "&body=" + "商品"
                                        + "&merchant_order_no=" + merchantOrderNo;
                                Log.e("打印参数", param + "\nappId:" + AppConstant.appId + "\n原始appId:" + originalAppId);
                                //versionType打开小程序的版本类型,主要分为0:正式版,1:体验版,2:开发版
                                new PayTask(MainActivity.this, AppConstant.appId, originalAppId, param, 1).goPay();
                            }
                        });
                    } else {
                        //TODO 错误，自己处理
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, "" + resultMsg, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //TODO 错误，自己处理
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            System.out.println("错误");
                        }
                    });
                }
            }
        });
    }
}
