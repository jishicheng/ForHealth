package com.example.forhealth;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import jp.wasabeef.blurry.Blurry;

public class LandingActivity extends AppCompatActivity {

    private TextView register,logining;
    private EditText name_in,password_in;
    private RelativeLayout landing;
    private ImageView background;
    public static String landing_name;



    protected void onCreate (Bundle savedInstances) {
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_information);
        register = (TextView)findViewById(R.id.register);
        logining = (TextView)findViewById(R.id.logining);
        name_in = (EditText)findViewById(R.id.name_in);
        password_in = (EditText)findViewById(R.id.password_in);
        landing = (RelativeLayout)findViewById(R.id.landing);
        background = (ImageView)findViewById(R.id.background);
        Register();
        Login();
    }

    private void Register() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.actionStart(LandingActivity.this);
            }
        });
    }

    private void Login() {
        logining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                landing_name = name_in.getText().toString();
                if(name_in.getText() == null||password_in.getText() == null){
                    final AlertDialog.Builder attention_dialog = new AlertDialog.Builder(LandingActivity.this);
                    attention_dialog.setTitle("注意").setMessage("请填写完整全部数据").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                }else{
                    SharedPreferences pref = getSharedPreferences(landing_name,MODE_PRIVATE);
                    Log.e("名称","111111");
                    Log.e("名称",landing_name);
                    String name = pref.getString("name","");
                    String password = pref.getString("password","");
                    if(name.equals(name_in.getText().toString()) && password.equals(password_in.getText().toString())){
                        MainActivity.actionStart(LandingActivity.this);
                    }else{
                        final AlertDialog.Builder attention_dialog = new AlertDialog.Builder(LandingActivity.this);
                        attention_dialog.setTitle("注意").setMessage("您输入的名称或密码有误").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                    }
                }
            }
        });
    }


    public static void actionStart(Context context) {
        Intent intent = new Intent(context,LandingActivity.class);
        context.startActivity(intent);
    }

    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            Blurry.with(LandingActivity.this).radius(8).sampling(2).async().capture(background).into(background);
        } else {
        }
    }
}
