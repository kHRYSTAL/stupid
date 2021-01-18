package me.khrystal.meituancalendarview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 2021/1/18
 * update time:
 * email: 723526676@qq.com
 */

public class MTCalendarView extends FrameLayout {

    private RecyclerView recyclerView;


    public MTCalendarView(@NonNull Context context) {
        super(context);
    }

    public MTCalendarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public static class CalendarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<MTDate> list;
        private OnItemClickListener listener;
        public CalendarAdapter(List<MTDate> list, OnItemClickListener listener) {
            this.list = list;
            this.listener = listener;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        public interface OnItemClickListener {
            void onItemClick(View v, int pos);
        }
    }
}
