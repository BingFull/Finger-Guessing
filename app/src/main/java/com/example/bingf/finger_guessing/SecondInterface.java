package com.example.bingf.finger_guessing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;

import static com.example.bingf.finger_guessing.R.id.users;

/**
 * Created by BingF on 2016/3/31.
 */
public class SecondInterface extends AppCompatActivity {
    TextView textview,tips;
    RadioGroup radiogroup;
    RadioButton radio1, radio2, radio3;
    Button confirm,login;
    EditText logtext;

    public final String STONE = "1";
    public final String SCISSORS = "2";
    public final String CLOTH = "3";
    public final int INIT=0;
    public final int FIRST=1;
    public final int SECOND=2;
    public String rst,orst;
    public String Username=new String();
    public String opponent;
    public int flag;


    KNetStream kNetStream;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        flag=0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        ifbHandler ifbHandler1=new ifbHandler();
        ifbHandler1.setMaster(this);
        kNetStream=new KNetStream(ifbHandler1);

        textview=(TextView)findViewById(R.id.result_second);
        radiogroup=(RadioGroup)findViewById(R.id.groupId_second);
        radio1=(RadioButton)findViewById(R.id.radio_second1);
        radio2=(RadioButton)findViewById(R.id.radio_second2);
        radio3=(RadioButton)findViewById(R.id.radio_second3);
        confirm=(Button)findViewById(R.id.confirm_second);
        login=(Button)findViewById(R.id.login);
        logtext=(EditText)findViewById(R.id.editText);
        tips=(TextView)findViewById(R.id.tips);

        login.setEnabled(false);
        confirm.setEnabled(false);

        logtext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText et = (EditText) v;
                if(!hasFocus)
                    et.setHint(et.getTag().toString());
                else{
                    String hint = et.getHint().toString();
                    textview.setTag(hint);
                    et.setHint(null);
                }
            }
        });


        logtext.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private final int charMaxNum = 8;
            private int editStart;
            private int editEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tips.setText("还能输入" + (charMaxNum - s.length()) + "字符");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0)
                    login.setEnabled(true);
                else
                    login.setEnabled(false);
                editStart = logtext.getSelectionStart();
                editEnd = logtext.getSelectionEnd();
                if (temp.length() > charMaxNum) {
                    DisplayToast("你输入的字符已超过了限制！");
                    s.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    logtext.setText(s);
                    logtext.setSelection(tempSelection);
                }
                Username = s.toString();
            }

        });

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                confirm.setEnabled(true);
                // TODO Auto-generated method stub
                if (checkedId == radio1.getId()) {
                    rst = STONE;
                } else if (checkedId == radio2.getId()) {
                    rst = SCISSORS;
                } else {
                    rst = CLOTH;
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kNetStream.open(Username);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kNetStream.send(opponent,rst);
                if(flag!=INIT){
                    if((orst.compareTo(STONE)==0  && rst.compareTo(STONE)==0 ) || (orst.compareTo(SCISSORS)==0 && rst.compareTo(SCISSORS)==0 ) || (orst.compareTo(CLOTH)==0  && rst.compareTo(CLOTH)==0 )){
                        textview.post(new Runnable() {
                            @Override
                            public void run() {
                                textview.setText("平局");
                            }
                        });
                    }else if((orst.compareTo(STONE)==0  && rst.compareTo(SCISSORS)==0 ) || (orst.compareTo(SCISSORS)==0  && rst.compareTo(CLOTH)==0 ) || (orst.compareTo(CLOTH)==0  && rst.compareTo(STONE)==0)){
                        textview.post(new Runnable() {
                            @Override
                            public void run() {
                                textview.setText("负");
                            }
                        });
                    }else{
                        textview.post(new Runnable() {
                            @Override
                            public void run() {
                                textview.setText("胜");
                            }
                        });
                    }

                    flag=INIT;
                }
                else{
                    flag=FIRST;
                }
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                kNetStream.close();
                SecondInterface.this.finish();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    public void displayUserName(final List<String> usersName){
        Iterator<String> ite=usersName.iterator();
        final CharSequence[] u=new CharSequence[usersName.size()];
        int i=0;
        while(ite.hasNext()){
            u[i++]=ite.next();
        }
        textview.post(new Runnable() {
            @Override
            public void run() {
                final AlertDialog.Builder builder = new AlertDialog.Builder(SecondInterface.this);
                builder.setTitle("请选择你的对手");
                builder.setItems(u, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        //  显示提示信息
                        opponent = u[arg1].toString();
                    }
                });
                builder.show();
            }
        });
    }


    public void dealMessage(String username,String msg){
        if(flag==INIT){
            this.opponent=username;
            this.orst=msg;
            flag=SECOND;
        }
        else{
            if(username.compareTo(opponent)==0){
                this.orst=msg;
                flag=INIT;
                if((orst.compareTo(STONE)==0  && rst.compareTo(STONE)==0 ) || (orst.compareTo(SCISSORS)==0 && rst.compareTo(SCISSORS)==0 ) || (orst.compareTo(CLOTH)==0  && rst.compareTo(CLOTH)==0 )){
                    textview.post(new Runnable() {
                        @Override
                        public void run() {
                            textview.setText("平局");
                        }
                    });
                }else if((orst.compareTo(STONE)==0  && rst.compareTo(SCISSORS)==0 ) || (orst.compareTo(SCISSORS)==0  && rst.compareTo(CLOTH)==0 ) || (orst.compareTo(CLOTH)==0  && rst.compareTo(STONE)==0)){
                    textview.post(new Runnable() {
                        @Override
                        public void run() {
                            textview.setText("负");
                        }
                    });
                }else{
                    textview.post(new Runnable() {
                        @Override
                        public void run() {
                            textview.setText("胜");
                        }
                    });
                }
            }
        }
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
        switch (id) {//得到被点击的item的itemId
            case R.id.action_settings://这里的Id就是布局文件中定义的Id，在用R.id.XXX的方法获取出来
                Toast.makeText(SecondInterface.this, "设置", Toast.LENGTH_LONG).show();
                break;
            case R.id.users:{
                kNetStream.getAllUserName();
                break;
            }
            case R.id.test:
                Toast.makeText(SecondInterface.this, "测试", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}

class ifbHandler implements Handler{
    SecondInterface master;
    @Override
    public void onAllUserName(List<String> userNames) {

        master.displayUserName(userNames);
    }

    @Override
    public void onReceive( String userName, String msg) {

        master.dealMessage(userName,msg);
    }

    @Override
    public void onResult(boolean result) {
        if(result){
            master.textview.post(new Runnable() {
                @Override
                public void run() {
                    master.DisplayToast("注册成功！");
                    master.login.setEnabled(false);
                }
            });
        }
    }
    public void setMaster(SecondInterface master){
        this.master=master;
    }
}