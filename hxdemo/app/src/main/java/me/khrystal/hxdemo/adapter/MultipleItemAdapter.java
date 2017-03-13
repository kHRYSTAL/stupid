package me.khrystal.hxdemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.khrystal.hxdemo.holder.BaseViewHolder;
import me.khrystal.hxdemo.holder.LeftTextHolder;
import me.khrystal.hxdemo.holder.RightTextHolder;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/3/13
 * update time:
 * email: 723526676@qq.com
 */

public class MultipleItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int ITEM_LEFT_TEXT = 0;
    private final int ITEM_RIGET_TEXT = 1;

    // 时间间隔最小为十分钟
    private final long TIME_INTERVAL = 10 * 60 * 1000;

    private List<EMMessage> messageList = new ArrayList<>();

    public MultipleItemAdapter() {

    }

    public void setMessageList(List<EMMessage> messages) {
        messageList.clear();;
        if (null != messages) {
            messageList.addAll(messages);
        }
    }

    public void addMessageList(List<EMMessage> messages) {
        messageList.addAll(0, messages);
    }

    public void addMessage(EMMessage message) {
        messageList.addAll(Arrays.asList(message));
    }

    public EMMessage getFirstMessage() {
        if (null != messageList && messageList.size() > 0) {
            return messageList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_LEFT_TEXT)
            return new LeftTextHolder(parent.getContext(), parent);
        else if (viewType == ITEM_RIGET_TEXT)
            return new RightTextHolder(parent.getContext(), parent);
        else {
            // TODO: 17/3/13
            return null;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BaseViewHolder)holder).bindData(messageList.get(position));
        if (holder instanceof  LeftTextHolder) {
            ((LeftTextHolder) holder).showTimeView(shouldShowTime(position));
        } else if (holder instanceof  RightTextHolder) {
            ((RightTextHolder) holder).showTimeView(shouldShowTime(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        EMMessage message = messageList.get(position);
        if (message.getFrom().equals(EMClient.getInstance().getCurrentUser())) {
            return ITEM_RIGET_TEXT;
        } else {
            return ITEM_LEFT_TEXT;
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    private boolean shouldShowTime(int position) {
        if (position == 0) {
            return true;
        }
        long lastTime = messageList.get(position - 1).getMsgTime();
        long curTime = messageList.get(position).getMsgTime();
        return curTime - lastTime > TIME_INTERVAL;
    }
}
