package me.khrystal.widget.autoscroll;

import android.content.Context;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/7/24
 * update time:
 * email: 723526676@qq.com
 */
public class SmoothScrollLayout extends FrameLayout {

    private ScrollHandler handler;
    private int itemLayoutId;

    private Adapter adapter;
    private RecyclerView recyclerView;

    public SmoothScrollLayout(@NonNull Context context) {
        this(context, null);
    }

    public SmoothScrollLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmoothScrollLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        handler = new ScrollHandler(this);
        adapter = new Adapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return false;
    }

    public void setData(List<String> data) {
        adapter.setList(data);
        if (data != null && data.size() > 0) {
            handler.sendEmptyMessageDelayed(0, 100);
        }
    }

    public void smoothScroll() {
        recyclerView.smoothScrollBy(0, 5);
        handler.sendEmptyMessageDelayed(0, 100);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacksAndMessages(null);
    }

    public void setItemLayoutId(int layoutId) {
        itemLayoutId = layoutId;
    }

    private static class ScrollHandler extends android.os.Handler {
        private WeakReference<SmoothScrollLayout> view;

        public ScrollHandler(SmoothScrollLayout view) {
            this.view = new WeakReference<>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            if (view.get() != null) {
                view.get().smoothScroll();
            }
        }
    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder> {

        private List<String> list;

        public Adapter() {
            list = new ArrayList<>();
        }

        public void setList(List<String> list) {
            this.list.clear();
            this.list.addAll(list);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(SmoothScrollLayout.this.getContext())
                    .inflate(itemLayoutId, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.bind(list.get(i % list.size()));
        }

        @Override
        public int getItemCount() {
            return list.size() > 0 ? Integer.MAX_VALUE : 0;
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView contentView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contentView = itemView.findViewById(R.id.content);
        }

        public void bind(String content) {
            contentView.setText(content);
        }
    }
}
