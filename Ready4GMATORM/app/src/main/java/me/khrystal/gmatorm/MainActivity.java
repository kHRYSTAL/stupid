package me.khrystal.gmatorm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.greendao.AbstractDaoSession;

import me.khrystal.gmatorm.greendao.DaoManager;
import me.khrystal.gmatorm.greendao.VocabularyCategoryDao;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.tvHello);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(MainActivity.class.getSimpleName(), "debug");
                DaoManager.getInstance().getDaoSession().getVocabularyCategoryDao().queryBuilder().where(VocabularyCategoryDao.Properties.Title.notEq("")).limit(10).build().list();
            }
        });
    }
}
