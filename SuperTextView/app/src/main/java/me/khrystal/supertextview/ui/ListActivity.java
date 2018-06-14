package me.khrystal.supertextview.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.khrystal.supertextview.R;
import me.khrystal.widget.supertextview.SuperTextView;

public class ListActivity extends AppCompatActivity {

  private RecyclerView rv;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);

    rv = (RecyclerView) findViewById(R.id.rv);

    final List<String> datas = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      datas.add("http://ogemdlrap.bkt.clouddn.com/revanger_4.jpeg");
      datas.add("http://ogemdlrap.bkt.clouddn.com/timg-2.jpeg");
      datas.add("http://ogemdlrap.bkt.clouddn.com/rebanger_3.jpg");
      datas.add("http://ogemdlrap.bkt.clouddn.com/revanger_2.jpeg");
      datas.add("http://ogemdlrap.bkt.clouddn.com/revanger_1.jpg");
    }
    rv.setLayoutManager(new LinearLayoutManager(this));
    rv.setItemAnimator(new DefaultItemAnimator());
    rv.setAdapter(new RecyclerView.Adapter() {
      @Override
      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerView.ViewHolder(
            LayoutInflater.from(ListActivity.this).inflate(R.layout.item_layout, parent, false)) {

        };
      }

      @Override
      public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SuperTextView stv = (SuperTextView) (holder.itemView.findViewById(R.id.stv_1));
        stv.setDrawable(ListActivity.this.getResources().getDrawable(R.drawable.loading_1));
        stv.setUrlImage(datas.get(position), false);
      }

      @Override
      public int getItemCount() {
        return datas.size();
      }
    });
  }
}