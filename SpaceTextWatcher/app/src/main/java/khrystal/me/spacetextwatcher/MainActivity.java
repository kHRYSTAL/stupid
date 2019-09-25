package khrystal.me.spacetextwatcher;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TableLayout;

import khrystal.me.widget.edittext.AddSpaceTextWatcher;

public class MainActivity extends AppCompatActivity {
    private AddSpaceTextWatcher[] asEditTexts = new AddSpaceTextWatcher[3];
    private EditText[] editTexts = new EditText[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTexts[0]=(EditText)findViewById(R.id.editText);//银行卡
        editTexts[1]=(EditText)findViewById(R.id.editText2);//手机号
        editTexts[2]=(EditText)findViewById(R.id.editText3);//身份证
        asEditTexts[0]=new AddSpaceTextWatcher(editTexts[0],48);
        asEditTexts[0].setSpaceType(AddSpaceTextWatcher.SpaceType.bankCardNumberType);
        asEditTexts[1]=new AddSpaceTextWatcher(editTexts[1],13);
        asEditTexts[1].setSpaceType(AddSpaceTextWatcher.SpaceType.mobilePhoneNumberType);
        asEditTexts[2]=new AddSpaceTextWatcher(editTexts[2],21);
        asEditTexts[2].setSpaceType(AddSpaceTextWatcher.SpaceType.IDCardNumberType);
    }
}
