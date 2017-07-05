package me.khrystal.animationplayground;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mwajeeh.animations.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String ARGS_BG = "bg";
    private GridLayoutManager layoutManager;
    private RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setPageMargin((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()));
        pager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Bundle args = new Bundle();
                switch (position) {
                    case 0:
                        args.putInt(ARGS_BG, R.drawable.bg_red);
                        break;
                    case 1:
                        args.putInt(ARGS_BG, R.drawable.bg_blue);
                        break;
                    case 2:
                        args.putInt(ARGS_BG, R.drawable.bg_yellow);
                        break;
                }
                return Page.instantiate(MainActivity.this, Page.class.getName(), args);
            }

            @Override
            public int getCount() {
                return 3;
            }
        });

        list = (RecyclerView) findViewById(R.id.list);
        layoutManager = new GridLayoutManager(this, 3);
        list.setLayoutManager(layoutManager);
        list.setAdapter(new Adapter(LayoutInflater.from(this), Categories.getCategories()));
    }

    public class Adapter extends RecyclerView.Adapter<ViewHolder> {

        private LayoutInflater inflater;
        private final List<Categories.Category> items;

        public Adapter(LayoutInflater inflater, List<Categories.Category> items) {
            this.inflater = inflater;
            this.items = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(inflater.inflate(R.layout.list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.title.setText(items.get(position).title);
            holder.image.setImageResource(items.get(position).image);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition == RecyclerView.NO_POSITION)
                        return;
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("position", adapterPosition);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

                        List<Pair<View, String>> pairs = new ArrayList<Pair<View, String>>();
                        for (int i = firstVisibleItemPosition; i <= lastVisibleItemPosition ; i++) {
                            ViewHolder holderForAdapterPosition = (ViewHolder) list.findViewHolderForAdapterPosition(i);
                            View itemView = holderForAdapterPosition.image;
                            pairs.add(Pair.create(itemView, "tab_" + i));
                        }
                        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, pairs.toArray(new Pair[]{})).toBundle();
                        context.startActivity(intent, bundle);
                    } else {
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    public static class Page extends Fragment {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.page, container, false);
            Drawable bg = ContextCompat.getDrawable(getContext(), getArguments().getInt("bg"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(bg);
            } else {
                view.setBackgroundDrawable(bg);
            }
            return view;
        }
    }
}
