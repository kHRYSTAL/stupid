package me.khrystal.dropdownmenuplus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.khrystal.widget.menu.MenuFilter;
import me.khrystal.widget.menu.interfaces.OnFilterDoneListener;

public class MainActivity extends AppCompatActivity implements OnFilterDoneListener {

    @Bind(R.id.dropDownMenu)
    MenuFilter dropDownMenu;

    @Bind(R.id.mFilterContentView)
    TextView mFilterContentView;
    private String[] titleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);

        initFilterDropDownView();
    }

    private void initFilterDropDownView() {
        titleList = new String[]{"第一个", "第二个", "第三个", "第四个"};
        dropDownMenu.setMenuAdapter(new DropMenuAdapter(this, titleList, this));
    }

    @Override
    public void onFilterDone(int position, String positionTitle, boolean isCheck) {
        dropDownMenu.close();
        Log.e("TAG", positionTitle + isCheck);
        if (!isCheck) {
            dropDownMenu.getFixedTabIndicator().resetPos(position);
            dropDownMenu.getFixedTabIndicator().setCurrentText(titleList[position]);
        } else {
            dropDownMenu.getFixedTabIndicator().highLightPos(position);
            dropDownMenu.getFixedTabIndicator().setCurrentText(positionTitle);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
