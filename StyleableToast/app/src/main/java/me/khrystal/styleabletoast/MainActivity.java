package me.khrystal.styleabletoast;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import me.khrystal.widget.toast.StyleableToast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void Toaster(View v) {
        StyleableToast st = null;
        switch (v.getId()) {
            case R.id.button1:
                st = new StyleableToast(this, "Updating profile", Toast.LENGTH_LONG);
                st.setBackgroundColor(Color.parseColor("#ff5a5f"));
                st.setTextColor(Color.WHITE);
                st.setIcon(R.drawable.ic_autorenew_black_24dp);
                st.spinIcon();
                st.setMaxAlpha();
                break;
            case R.id.button2:
                st = new StyleableToast(this, "Turn off fly mode", Toast.LENGTH_LONG);
                st.setBackgroundColor(Color.parseColor("#865aff"));
                st.setIcon(R.drawable.ic_airplanemode_inactive_black_24dp);
                break;
            case R.id.button3:
                st = new StyleableToast(this, "Profile saved", Toast.LENGTH_LONG);
                st.setMaxAlpha();
                break;
            case R.id.button4:
                st = new StyleableToast(this, "PHONE IS OVERHEATING!", Toast.LENGTH_LONG);
                st.setCornerRadius(5);
                st.setBackgroundColor(Color.BLACK);
                st.setTextColor(Color.RED);
                st.setBoldText();
                break;
            case R.id.button5:
                st = StyleableToast.makeText(this, "Picture saved in gallery", Toast.LENGTH_LONG, R.style.StyleableToast);
                break;
            case R.id.button6:
                st = new StyleableToast(this, "Wrong password/username", Toast.LENGTH_LONG);
                st.setBackgroundColor(Color.parseColor("#2187c6"));
                st.setBoldText();
                st.setCornerRadius(Color.WHITE);
                break;
        }
        st.show();
    }
}
