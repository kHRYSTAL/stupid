package me.khrystal.keyboardvisibilityevent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import me.khrystal.util.keyboard.KeyboardVisibilityEvent;
import me.khrystal.util.keyboard.KeyboardVisibilityEventListener;
import me.khrystal.util.keyboard.Unregistrar;

public class MainActivity extends AppCompatActivity {

    TextView mKeyboardStatus;
    EditText mTextField;
    Button mButtomUnregister;
    Unregistrar mUnregistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mKeyboardStatus = (TextView) findViewById(R.id.keyboard_status);
        mTextField = (EditText) findViewById(R.id.text_field);
        mButtomUnregister = (Button) findViewById(R.id.btn_unregister);

        mUnregistrar = KeyboardVisibilityEvent.registerEventListener(this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                updateKeyboardStatusText(isOpen);
            }
        });

        updateKeyboardStatusText(KeyboardVisibilityEvent.isKeyboardVisible(this));
        mButtomUnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unregister();
            }
        });
    }


    private void updateKeyboardStatusText(boolean isOpen) {
        mKeyboardStatus.setText(String.format("keyboard is %s", (isOpen ? "visible" : "hidden")));
    }

    void unregister() {
        mUnregistrar.unregister();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnregistrar.unregister();
    }
}
