package me.khrystal.server;

import android.content.Intent;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import khrystal.me.pipe.IMessagePIPE;
import khrystal.me.pipe.Message;
import me.khrystal.util.ipc.IPCCallback;
import me.khrystal.util.ipc.VCore;

public class MainActivity extends AppCompatActivity implements IMessagePIPE {

    private static final String KIPC_SERVER = "KIPC_SERVER";

    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.goAnotherProcess);
        VCore.getCore().registerService(IMessagePIPE.class, this);
        button.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, TestActivity.class));
        });
    }

    @Override
    public void clientSend(Message message, final IPCCallback callback) {
        Log.e(KIPC_SERVER, "onReceiveClientSend");
        Log.e(KIPC_SERVER, message.getContent());
        new Thread(() -> {
            SystemClock.sleep(2000);
            Bundle bundle = new Bundle();
            bundle.putString("response", "this is server response");
            try {
                callback.onSuccess(bundle);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
