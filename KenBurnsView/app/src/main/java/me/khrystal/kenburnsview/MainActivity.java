package me.khrystal.kenburnsview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private static final int POS_SINGLE_IMG = 0;
    private static final int POS_MULTI_IMG = 1;
    private static final int POS_FORM_URL = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.main_options, android.R.layout.simple_expandable_list_item_1);

        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onListItemClick(parent, view, position, id);
            }
        });
    }

    private void onListItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case POS_SINGLE_IMG:
                startActivity(new Intent(this, SingelImageActivity.class));
                break;
            case POS_MULTI_IMG:
                startActivity(new Intent(this, MultiImageActivity.class));
                break;
            case POS_FORM_URL:
                break;
        }
    }


}
