package me.khrystal.hxdemo.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import me.khrystal.hxdemo.R;
import me.khrystal.hxdemo.event.LeftChatItemClickEvent;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/3/13
 * update time:
 * email: 723526676@qq.com
 */

public class LeftTextHolder extends BaseViewHolder<EMMessage> {


    @Bind(R.id.chat_left_text_tv_time)
    TextView timeTextView;
    @Bind(R.id.chat_left_text_tv_name)
    TextView nameView;
    @Bind(R.id.chat_left_text_tv_content)
    TextView contentView;

    public LeftTextHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.chat_left_text_view);
    }

    @OnClick({R.id.chat_left_text_tv_content, R.id.chat_left_text_tv_name})
    public void onNameClick(View view) {
        LeftChatItemClickEvent clickEvent = new LeftChatItemClickEvent();
        clickEvent.userId = nameView.getText().toString();
        EventBus.getDefault().post(clickEvent);
    }

    @Override
    public void bindData(EMMessage data) {
        EMMessage message = data;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time = dateFormat.format(message.getMsgTime());

        String content = "暂不支持此类型消息";
        if (message.getBody() instanceof EMTextMessageBody) {
            content = ((EMTextMessageBody) message.getBody()).getMessage();
        }
        contentView.setText(content);
        timeTextView.setText(time);
        nameView.setText(message.getFrom());
    }

    public void showTimeView(boolean isShow) {
        timeTextView.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
}
