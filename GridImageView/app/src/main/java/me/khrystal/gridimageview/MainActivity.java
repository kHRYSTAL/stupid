package me.khrystal.gridimageview;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Random;

import me.khrystal.widget.gridimageview.GridImageView;
import me.khrystal.widget.gridimageview.GridImageViewAdapter;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<ArrayList<String>> mData = new ArrayList<>();

    private ArrayList<String> mTemp = new ArrayList<>();

    public static final String IMAGE0 = "http://ac-QYgvX1CC.clouddn.com/233a5f70512befcc.jpg";
    public static final String IMAGE1 = "http://ac-QYgvX1CC.clouddn.com/36f0523ee1888a57.jpg";
    public static final String IMAGE2 = "http://ac-QYgvX1CC.clouddn.com/07915a0154ac4a64.jpg";
    public static final String IMAGE3 = "http://ac-QYgvX1CC.clouddn.com/9ec4bc44bfaf07ed.jpg";
    public static final String IMAGE4 = "http://ac-QYgvX1CC.clouddn.com/fa85037f97e8191f.jpg";
    public static final String IMAGE5 = "http://ac-QYgvX1CC.clouddn.com/de13315600ba1cff.jpg";
    public static final String IMAGE6 = "http://ac-QYgvX1CC.clouddn.com/15c5c50e941ba6b0.jpg";
    public static final String IMAGE7 = "http://ac-QYgvX1CC.clouddn.com/10762c593798466a.jpg";
    public static final String IMAGE8 = "http://ac-QYgvX1CC.clouddn.com/eaf1c9d55c5f9afd.jpg";
    public static final String IMAGE9 = "http://ac-QYgvX1CC.clouddn.com/ad99de83e1e3f7d4.jpg";
    private GridImgAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTemp();
        initData();
        A adapter = new A();
        imageAdapter = new GridImgAdapter();
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mRecyclerView.setAdapter(adapter);
    }

    private void initData() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            if (i % 5 == 0) { // 一张图片情况
                ArrayList<String> data = new ArrayList<>();
                data.add(mTemp.get(random.nextInt(10)));
                mData.add(data);
            } else if (i % 3 == 0) { // 两张图片清空
                ArrayList<String> data = new ArrayList<>();
                data.add(mTemp.get(random.nextInt(10)));
                data.add(mTemp.get(random.nextInt(10)));
                mData.add(data);
            } else { // 9宫格
                mData.add(mTemp);
            }
        }
    }

    private void initTemp() {
        mTemp.add(IMAGE0);
        mTemp.add(IMAGE1);
        mTemp.add(IMAGE2);
        mTemp.add(IMAGE3);
        mTemp.add(IMAGE4);
        mTemp.add(IMAGE5);
        mTemp.add(IMAGE6);
        mTemp.add(IMAGE7);
        mTemp.add(IMAGE8);
        mTemp.add(IMAGE9);
    }


    private class A extends RecyclerView.Adapter<VH> {

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            VH holder = new VH(LayoutInflater.from(MainActivity.this).inflate(R.layout.sample_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(VH holder, final int position) {
            holder.gridImageView.setAdapter(imageAdapter);
            holder.gridImageView.setImagesData(mData.get(position));
            if (mData.get(position).size() == 2) {
                holder.gridImageView.setShowStyle(GridImageView.STYLE_FILL);
            } else {
                holder.gridImageView.setShowStyle(GridImageView.STYLE_GRID);
            }
            if (mData.get(position).size() == 1) {
                holder.gridImageView.setSingleImageSize(600, 400);
            } else
                holder.gridImageView.setSingleImageSize(-1, -1);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    private class VH extends RecyclerView.ViewHolder {

        GridImageView<String> gridImageView;

        public VH(View itemView) {
            super(itemView);
            gridImageView = (GridImageView) itemView.findViewById(R.id.grid_iv);
        }
    }

    private class GridImgAdapter extends GridImageViewAdapter<String> {

        @Override
        protected void onDisplayImage(final Context context, ImageView imageView, final String s) {
            Log.e("URL", s);
            // 加载图片
            Glide.with(context).load(s)
                    .asBitmap() // 点击变暗效果必须增加该方法
                    .into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
