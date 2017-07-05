package me.khrystal.searchdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PopupActivity extends AppCompatActivity {
    RecyclerView rv;
    RVAdapter adapter;
    EditText et;
    PopupWindow popupWindow;
    List<String> strs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        View popupView = getLayoutInflater().inflate(R.layout.popupwindow, null);
        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
            }
        });

        //region view config
        rv = (RecyclerView) popupView.findViewById(R.id.rv);
        et = (EditText) findViewById(R.id.et);

        strs = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            strs.add("我是第" + i + "个item");
        }

        adapter = new RVAdapter(strs);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        //endregion

        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!popupWindow.isShowing())
                    popupWindow.showAsDropDown(et);
            }
        });
    }

    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        boolean isTouchEditText = false;
        if (x >= et.getLeft() && x <= et.getRight()
                && y >= et.getTop() && y <= et.getBottom())
            isTouchEditText = true;

        if (popupWindow != null && popupWindow.isShowing() && !isTouchEditText) {
            popupWindow.dismiss();
        }
        return super.onTouchEvent(event);
    }

    class RVAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private List<String> mList;

        public RVAdapter(List<String> list) {
            mList = list != null ? list : new ArrayList<String>();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv.setText(mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public void updateData(List<String> list) {
            if (list != null) {
                mList.clear();
                mList.addAll(list);
                notifyDataSetChanged();
            }
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }
    }
}
