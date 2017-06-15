package me.khrystal.hwpushdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/*
 * 测试Demo主页，图标设置类
 */
public class ImageAdapter extends BaseAdapter {
    private Context context;
    private final String[] itemValues;

    public ImageAdapter(Context context, String[] itemValues) {
        this.context = context;
        this.itemValues = itemValues;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            gridView = inflater.inflate(R.layout.item, null);

            TextView textView = (TextView) gridView.findViewById(R.id.grid_item_label);
            textView.setText(itemValues[position]);

            ImageView imageView = (ImageView) gridView.findViewById(R.id.grid_item_image);
            String item = itemValues[position];
            if (item.equals(PustDemoActivity.type[0])) {
                imageView.setImageResource(R.drawable.gettoken);
            } else if (item.equals(PustDemoActivity.type[1])) {
                imageView.setImageResource(R.drawable.upload);
            } else {
                imageView.setImageResource(R.drawable.ic_launcher);
            }

        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return itemValues.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
