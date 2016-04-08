package com.example.bingf.finger_guessing;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by BingF on 2016/3/31.
 */
public class FirstInterface extends AppCompatActivity {
    TextView textview1, textview2;
    RadioGroup radiogroup1, radiogroup2;
    RadioButton radio1, radio2, radio3, radio4, radio5, radio6;
    Button confirm;
    public static final int STONE = 1;
    public static final int SCISSORS = 2;
    public static final int CLOTH = 3;
    public int rst1,rst2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);


        textview1 = (TextView) findViewById(R.id.result_first1);
        textview2 = (TextView) findViewById(R.id.result_first2);
        radiogroup1 = (RadioGroup) findViewById(R.id.groupId_first1);
        radiogroup2 = (RadioGroup) findViewById(R.id.groupId_first2);
        radio1 = (RadioButton) findViewById(R.id.radio_first1);
        radio2 = (RadioButton) findViewById(R.id.radio_first2);
        radio3 = (RadioButton) findViewById(R.id.radio_first3);
        radio4 = (RadioButton) findViewById(R.id.radio_first4);
        radio5 = (RadioButton) findViewById(R.id.radio_first5);
        radio6 = (RadioButton) findViewById(R.id.radio_first6);
        confirm = (Button) findViewById(R.id.confirm_first);


        radiogroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if (checkedId == radio1.getId()) {
                    rst1 = STONE;
                } else if (checkedId == radio2.getId()) {
                    rst1 = SCISSORS;
                } else {
                    rst1 = CLOTH;
                }
            }
        });

        radiogroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if (checkedId == radio4.getId()) {
                    rst2 = STONE;
                } else if (checkedId == radio5.getId()) {
                    rst2 = SCISSORS;
                } else {
                    rst2 = CLOTH;
                }
            }
        });

        confirm.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                if((rst1 == STONE && rst2 == STONE) || (rst1 == SCISSORS && rst2 == SCISSORS) || (rst1 == CLOTH && rst2 == CLOTH)){
                    textview1.setText("平局");
                    textview2.setText("平局");
                    DisplayToast("hehehehe!");
                }else if((rst1 == STONE && rst2 == SCISSORS) || (rst1 == SCISSORS && rst2 == CLOTH) || (rst1 == CLOTH && rst2 == STONE)){
                    textview1.setText("胜");
                    textview2.setText("负");
                    DisplayToast("hihihihi!");
                }else{
                    textview1.setText("负");
                    textview2.setText("胜");
                    DisplayToast("hahahaha!");
                }
            }
        });
    }

    public void DisplayToast(String str) {
        Toast toast = Toast.makeText(this, str, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 1220);
        toast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

