package me.khrystal.anglepopupwindow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.khrystal.widget.dialog.AnglePopupWindow;

public class MainActivity extends AppCompatActivity implements AnglePopupWindow.OnMenuItemClickListener<String> {

    RecyclerView recyclerView;
    ArrayList<String> list;
    private ArrayList<String> arrayList;
    private AnglePopupWindow<String> popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("item:" + i);
        }
        A adapter = new A();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

    }

    class A extends RecyclerView.Adapter<VH> {

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            VH vh = new VH(LayoutInflater
                    .from(MainActivity.this).inflate(R.layout.sample_item, parent, false));
            return vh;
        }

        @Override
        public void onBindViewHolder(VH holder, final int position) {
            holder.content.setText(list.get(position));
            holder.pop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopup(v, list.get(position));
                }


            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private void showPopup(View view, String data) {
        arrayList = new ArrayList<>();
        arrayList.add("一");
        arrayList.add("二");
        arrayList.add("三");
        popupWindow = new AnglePopupWindow(this, arrayList, this, data);
        popupWindow.showPopupWindow(view);
    }

    class VH extends RecyclerView.ViewHolder {
        TextView content;
        TextView pop;

        public VH(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.item_content);
            pop = (TextView) itemView.findViewById(R.id.item_pop);
        }
    }

    @Override
    public void onMenuItemClick(View view, int menuPosition, String data) {
        Log.e("MainActivity", data);
        Toast.makeText(MainActivity.this, arrayList.get(menuPosition), Toast.LENGTH_SHORT).show();
    }
}
