package me.khrystal.supertextview.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import me.khrystal.supertextview.R;
import me.khrystal.widget.supertextview.SuperTextView;

public class ThirdActivity extends AppCompatActivity {

  private SuperTextView stv_1;
  private SuperTextView stv_2;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_third);
    stv_1 = (SuperTextView) findViewById(R.id.stv_1);
    stv_2 = (SuperTextView) findViewById(R.id.stv_2);


    String url_imag =
      "http://ogemdlrap.bkt.clouddn.com/revanger_4.jpeg";
    String url_avatar =
        "http://ogemdlrap.bkt.clouddn.com/timg-2.jpeg";


    stv_1.setUrlImage(url_imag, false);
    stv_2.setUrlImage(url_avatar);

    stv_2.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(ThirdActivity.this, ListActivity.class));
      }
    });
  }

}