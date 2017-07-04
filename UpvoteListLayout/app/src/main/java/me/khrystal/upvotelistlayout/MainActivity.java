package me.khrystal.upvotelistlayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import me.khrystal.widget.UpvoteListLayout;

public class MainActivity extends AppCompatActivity {

    private UpvoteListLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = (UpvoteListLayout) findViewById(R.id.uvLayout);
        layout.setMaxSize(5);
        //region init data
        List<String> list = new ArrayList<>();
        list.add("http://img2.imgtn.bdimg.com/it/u=1939271907,257307689&fm=21&gp=0.jpg");
        list.add("http://img0.imgtn.bdimg.com/it/u=2263418180,3668836868&fm=206&gp=0.jpg");
        list.add("http://img0.imgtn.bdimg.com/it/u=2263418180,3668836868&fm=206&gp=0.jpg");
        list.add("http://47.93.226.38:8080/img3.jpg");
        //endregion
        layout.setOnLoadImageListener(new UpvoteListLayout.OnLoadImageListener() {
            @Override
            public void onLoadImage(ImageView view, String url) {
                Glide.with(MainActivity.this)
                        .load(url)
                        .bitmapTransform(new CropCircleTransformation(MainActivity.this))
                        .into(view);
            }
        });

        layout.append(list);

    }

    public void onAddClick(View view) {
        layout.append("http://47.93.226.38:8080/img3.jpg");
    }

    public void onRemoveClick(View view) {
        layout.delete("http://47.93.226.38:8080/img3.jpg");
    }
}
