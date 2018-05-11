package me.khrystal.rxnetmonitor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import me.khrystal.receiver.RxNetStateReceiver;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.tv);
        disposable = RxNetStateReceiver.getReceiver()
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(netState -> {
                    Log.e("TAG", netState.getNetStateName());
                    mTextView.setText(netState.getNetStateName());
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
