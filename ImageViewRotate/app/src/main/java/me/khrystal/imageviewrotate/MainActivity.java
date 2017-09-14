package me.khrystal.imageviewrotate;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.iv);
        imageView.setImageResource(R.mipmap.test_big);
        Matrix matrix = new Matrix();
        matrix.postRotate(15f);

        imageView.setImageMatrix(matrix);
        Bitmap image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        Bitmap resizeBitmap = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
        resizeBitmap.setHasAlpha(true);
        imageView.setImageBitmap(resizeBitmap);
    }
}
