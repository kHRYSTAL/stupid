package me.khrystal.verifycodeedittext;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.khrystal.widget.verifycodeedittext.VerifyCodeEditText;

public class MainActivity extends AppCompatActivity {

    private VerifyCodeEditText verificationCodeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verificationCodeEditText = (VerifyCodeEditText) findViewById(R.id.am_et);
        verificationCodeEditText.setOnVerificationCodeChangedListener(new VerifyCodeEditText
                .OnVerificationCodeChangedListener() {

            @Override
            public void onVerCodeChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void onInputCompleted(CharSequence s) {

            }
        });
    }
}
