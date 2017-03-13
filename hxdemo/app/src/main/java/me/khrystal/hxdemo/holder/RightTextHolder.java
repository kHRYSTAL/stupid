package me.khrystal.hxdemo.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import me.khrystal.hxdemo.R;
import me.khrystal.hxdemo.event.ImTypeMessageResendEvent;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/3/13
 * update time:
 * email: 723526676@qq.com
 */

public class RightTextHolder extends BaseViewHolder<EMMessage> {

    @Bind(R.id.chat_right_text_tv_time)
    TextView timeTextView;
    @Bind(R.id.chat_right_text_progressbar)
    ProgressBar loadingBar;
    @Bind(R.id.chat_right_text_tv_error)
    ImageView errorView;
    @Bind(R.id.chat_right_text_layout_status)
    FrameLayout statusView;
    @Bind(R.id.chat_right_text_tv_content)
    TextView contentView;
    @Bind(R.id.chat_right_text_tv_name)
    TextView nameView;

    private EMMessage message;

    public RightTextHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.chat_right_text_view);
    }

    @OnClick(R.id.chat_right_text_tv_error)
    public void onErrorClick(View view) {
        ImTypeMessageResendEvent event = new ImTypeMessageResendEvent();
        event.message = message;
        EventBus.getDefault().post(event);
    }

    @Override
    public void bindData(EMMessage message) {
        this.message = message;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time = dateFormat.format(message.getMsgTime());
        String content = "暂不支持此消息类型";
        if (message.getBody() instanceof EMTextMessageBody) {
            content = ((EMTextMessageBody) message.getBody()).getMessage();
        }
        contentView.setText(content);
        timeTextView.setText(time);
        nameView.setText(message.getFrom());

        if ((EMMessage.Status.FAIL).equals(message.status())) {
            errorView.setVisibility(View.VISIBLE);
            loadingBar.setVisibility(View.GONE);
            statusView.setVisibility(View.VISIBLE);
        } else if (EMMessage.Status.INPROGRESS.equals(message.status())) {
            errorView.setVisibility(View.GONE);
            loadingBar.setVisibility(View.VISIBLE);
            statusView.setVisibility(View.VISIBLE);
        } else {
            statusView.setVisibility(View.GONE);
        }
    }

    public void showTimeView(boolean isShow) {
        timeTextView.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
}
