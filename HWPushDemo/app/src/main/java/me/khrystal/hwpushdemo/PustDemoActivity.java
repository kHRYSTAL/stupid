package me.khrystal.hwpushdemo;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.android.pushagent.api.PushManager;
import com.huawei.pushtest.R;

public class PustDemoActivity extends Activity {

    public final static String TAG = "PushDemo";

    private GridView gridView;

    private static TextView textView;

    public static String[] type = null;

    // 接收Push消息
    public static final int RECEIVE_PUSH_MSG = 0x100;
    // 接收Push Token消息
    public static final int RECEIVE_TOKEN_MSG = 0x101;
    // 接收Push 自定义通知消息内容
    public static final int RECEIVE_NOTIFY_CLICK_MSG = 0x102;
    // 接收Push LBS 标签上报响应
    public static final int RECEIVE_TAG_LBS_MSG = 0x103;

    public static final String NORMAL_MSG_ENABLE = "normal_msg_enable";
    public static final String NOTIFY_MSG_ENABLE = "notify_msg_enable";
    
    private static final String GET_TOKEN = "获取Token";
    private static final String ENTER_LBS = "标签&LBS测试";
    
    private static final String OPEN_NORMAL = "开启-接收透传消息";
    private static final String CLOSE_NORMAL = "关闭-接收透传消息";
    private static final String OPEN_NOTIFY = "开启-接收自呈现消息";
    private static final String CLOSE_NOTIFY = "关闭-接收自呈现消息";
    private String normalStr = CLOSE_NORMAL;
    private String notifyStr = CLOSE_NOTIFY;
    /*
     * 处理提示消息，更新界面
     */
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case RECEIVE_PUSH_MSG:
                showMsg((String) msg.obj);
                break;
            case RECEIVE_TOKEN_MSG:
                showMsg((String) msg.obj);
                break;
            case RECEIVE_NOTIFY_CLICK_MSG:
                showMsg((String) msg.obj);
                break;
            case RECEIVE_TAG_LBS_MSG:
                showToast((String) msg.obj);
                break;
            default:
                break;
            }
        }
    };

    public Handler getHandler() {
        return mHandler;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "run into PustDemoActivity onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mian);

        MyApplication.instance().setMainActivity(this);

        textView = (TextView) findViewById(R.id.tv_msg);

        gridView = (GridView) findViewById(R.id.gridView2);

		type = new String[] { GET_TOKEN, ENTER_LBS , CLOSE_NORMAL, CLOSE_NOTIFY};
        gridView.setAdapter(new ImageAdapter(this, type));

        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                String clickText = ((TextView) v.findViewById(R.id.grid_item_label)).getText().toString();

                // 点击获取Token
                if (type[0].equals(clickText)) {
                    
//                    UpdateHelper.check(PustDemoActivity.this, new UpdateNotifyHelper(PustDemoActivity.this));

                    // 获取客户端AccessToken,获取之前请先确定该应用（包名）已经在开发者联盟上创建成功，并申请、审核通过Push权益
                    // 该测试应用已经注册过
                    PushManager.requestToken(PustDemoActivity.this);

                    Log.i(TAG, "try to get Token ,current packageName is " + PustDemoActivity.this.getPackageName());

                } else if (type[1].equals(clickText)) {
                    // 打开标签、LBS上报页面

                    startActivity(new Intent().setAction("com.huawei.push.action.test").setPackage(PustDemoActivity.this.getPackageName()));
                }  else if (OPEN_NORMAL.equals(clickText)) {
                    PushManager.enableReceiveNormalMsg(PustDemoActivity.this, true);
                    normalStr = CLOSE_NORMAL;
                    type = new String[] { GET_TOKEN, ENTER_LBS , normalStr, notifyStr};
                    gridView.setAdapter(new ImageAdapter(PustDemoActivity.this, type));
                } else if (CLOSE_NORMAL.equals(clickText)) {
                    PushManager.enableReceiveNormalMsg(PustDemoActivity.this, false);
                    normalStr = OPEN_NORMAL;
                    type = new String[] { GET_TOKEN, ENTER_LBS , normalStr, notifyStr};
                    gridView.setAdapter(new ImageAdapter(PustDemoActivity.this, type));
                } else if (OPEN_NOTIFY.equals(clickText)) {
                    PushManager.enableReceiveNotifyMsg(PustDemoActivity.this, true);
                    notifyStr = CLOSE_NOTIFY;
                    type = new String[] { GET_TOKEN, ENTER_LBS , normalStr, notifyStr};
                    gridView.setAdapter(new ImageAdapter(PustDemoActivity.this, type));
                } else if (CLOSE_NOTIFY.equals(clickText)) {
                    PushManager.enableReceiveNotifyMsg(PustDemoActivity.this, false);
                    notifyStr = OPEN_NOTIFY;
                    type = new String[] { GET_TOKEN, ENTER_LBS , normalStr, notifyStr};
                    gridView.setAdapter(new ImageAdapter(PustDemoActivity.this, type));
                }
            }
        });

    }

    /*
     * 显示接收到的Push消息，在页面上
     */
    public void showMsg(final String msg) {
        try {

            mHandler.post(new Runnable() {

                public void run() {
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                    Date curDate = new Date(System.currentTimeMillis());
                    String dateStr = formatter.format(curDate);
                    textView.postInvalidate();
                    String str = "接收时间：" + dateStr + " , 消息内容：" + msg;
                    textView.setText(str);
                    Log.d(TAG, "showMsg:" + str);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
     * 显示接收到的Push消息，弹出Toast
     */
    public void showToast(String msg) {
        Log.d(TAG, "showToast:" + msg);
        Toast.makeText(PustDemoActivity.this, msg, Toast.LENGTH_LONG).show();
    }

}