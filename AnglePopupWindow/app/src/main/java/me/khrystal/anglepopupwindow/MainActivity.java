package me.khrystal.anglepopupwindow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.khrystal.widget.dialog.AnglePopupWindow;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> list;
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
        public void onBindViewHolder(VH holder, int position) {
            holder.content.setText(list.get(position));
            holder.pop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopup(v);
                }


            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private void showPopup(View view) {
        ArrayList<String> list = new ArrayList<>();
        list.add("一");
        list.add("二");
        list.add("三");
        AnglePopupWindow popupWindow = new AnglePopupWindow(this, list, null);
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
}
