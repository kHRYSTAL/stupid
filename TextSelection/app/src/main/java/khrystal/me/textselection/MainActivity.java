package khrystal.me.textselection;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import khrystal.me.textselection.selection.CustomVG;
import khrystal.me.textselection.selection.SelectableLayoutManager;
import khrystal.me.textselection.selection.SelectableRecyclerView;
import khrystal.me.textselection.selection.SelectableTextView;
import khrystal.me.textselection.selection.SelectionCallback;
import khrystal.me.textselection.selection.SelectionController;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = ">>>>>";
    private String sampleText1;
    private String sampleText2;
    private String sampleCation;


    SelectableTextView text_view2;
    SelectableTextView text_view1;
    SelectableTextView caption_text_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_layout);
        sampleText1 = getString(R.string.sample_text1);
        sampleText2 = getString(R.string.sample_text2);
        sampleCation = getString(R.string.caption_text);

        CustomVG vg = findViewById(R.id.root);
        text_view2 = (SelectableTextView) findViewById(R.id.text_view2);
        text_view1 = (SelectableTextView) findViewById(R.id.text_view1);
        caption_text_view = (SelectableTextView) findViewById(R.id.caption_text_view);

        text_view1.setText(sampleText1);
        text_view1.setKey(" pos: " + 0 + sampleText1);
        text_view2.setText(sampleText2);
        text_view2.setKey(" pos: " + 0 + sampleText2);
        caption_text_view.setText(sampleCation);
        caption_text_view.setKey(" pos: " + 0 + sampleCation);

        vg.sh.addViewToSelectable(text_view1);
        vg.sh.addViewToSelectable(text_view2);
        vg.sh.addViewToSelectable(caption_text_view);

    }
}
