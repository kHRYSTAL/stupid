package me.khrystal.client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import khrystal.me.pipe.IMessagePIPE;
import khrystal.me.pipe.Message;
import me.khrystal.server.R;
import me.khrystal.util.ipc.VCore;
import me.khrystal.util.ipc.cb.BaseCallback;

public class MainActivity extends AppCompatActivity {

    public static final String EVENT_KEY = "event_key";

    EditText editText;
    Button sendBtn;
    TextView receiveTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        sendBtn = findViewById(R.id.button);
        receiveTextView = findViewById(R.id.textView);

        IMessagePIPE pipe = (IMessagePIPE) VCore.getCore().getService(IMessagePIPE.class);

        sendBtn.setOnClickListener(v -> {
            if (pipe != null && !TextUtils.isEmpty(editText.getText().toString())) {
                Message message = new Message();
                message.setContent(editText.getText().toString());
                pipe.clientSend(message, new BaseCallback() {
                    @Override
                    public void onSucceed(Bundle result) {
                        receiveTextView.setText(result.getString("response"));
                        VCore.getCore().post(EVENT_KEY, result);
                    }

                    @Override
                    public void onFailed(String reason) {

                    }
                });
            }
        });


        VCore.getCore().subscribe(EVENT_KEY, event -> {
            String resp = event.getString("response");
            Toast.makeText(MainActivity.this, resp, Toast.LENGTH_SHORT).show();
        });
    }
}
