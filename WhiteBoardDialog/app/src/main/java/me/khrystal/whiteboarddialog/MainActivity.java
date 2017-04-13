package me.khrystal.whiteboarddialog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import me.khrystal.widget.dialog.WhiteBoardDialog;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Bean> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
    }

    private void initData() {
        dataList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Bean bean = new Bean();
            bean.key = i;
            bean.value = String.format("value:%d", i);
            dataList.add(bean);
        }
    }

    public void showDialog(View view) {
        WhiteBoardDialog.getInstance(0)
                .setClipDistance(600)
                .setWhiteBoardListener(new WhiteBoardDialog.WhiteBoardListener() {
                    @Override
                    public void onCloseClick(int type) {
                        Toast.makeText(MainActivity.this, "close", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemClick(int type, int position) {
                        Toast.makeText(MainActivity.this, dataList.get(position).value, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public int getItemCount() {
                        return dataList.size();
                    }

                    @Override
                    public String fillText(int position) {
                        return dataList.get(position).value;
                    }
                }).show(getFragmentManager(), "");
    }
}
