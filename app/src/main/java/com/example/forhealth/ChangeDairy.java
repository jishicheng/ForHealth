package com.example.forhealth;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.example.forhealth.weight.Dairy;
import com.example.forhealth.weight.MyDatabaseHelper;
import java.util.Calendar;

import static com.example.forhealth.LandingActivity.landing_name;

public class ChangeDairy extends AppCompatActivity {
    private Button cancel;
    private Button save;
    private RadioGroup weather;
    private EditText exercise;
    private EditText today_weight;
    private EditText feeling;
    private EditText remarks;
    private RadioButton weather_selected;

    private MyDatabaseHelper dbHelper;
    private String choose_dairy,choose_weather,choose_exercise,choose_date,choose_weight,choose_feeling,choose_remarks,change_weather,change_exercise,change_today_weight,change_remarks,change_feeling;

    public void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        choose_dairy = intent.getStringExtra("choose_dairy");
        Log.e("打开","ChangeDairy");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        initViews();
    }

    private void initViews() {
        cancel = (Button)findViewById(R.id.cancel_change);
        save = (Button)findViewById(R.id.save_change);
        weather = (RadioGroup)findViewById(R.id.weather_change);
        exercise = (EditText)findViewById(R.id.exercise_change);
        today_weight = (EditText)findViewById(R.id.today_weight_change);
        feeling = (EditText)findViewById(R.id.feeling_change);
        remarks = (EditText)findViewById(R.id.remarks_change);
        Log.e("选中",choose_dairy);
        dbHelper = new MyDatabaseHelper(this,landing_name,null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Dairy", null,"date = ?", new String[]{choose_dairy},null,null,null);
        if(cursor.moveToFirst()){
            do{
                choose_weather = cursor.getString(cursor.getColumnIndex("weather"));
                choose_exercise = cursor.getString(cursor.getColumnIndex("exercise"));
                choose_date = cursor.getString(cursor.getColumnIndex("date"));
                choose_weight = cursor.getString(cursor.getColumnIndex("weight"));
                choose_feeling = cursor.getString(cursor.getColumnIndex("feeling"));
                choose_remarks = cursor.getString(cursor.getColumnIndex("remarks"));
                Log.e("运动",choose_exercise);
                weather.check(getweatherid());
                exercise.setText(choose_exercise);
                today_weight.setText(choose_weight);
                remarks.setText(choose_remarks);
                feeling.setText(choose_feeling);
            }while (cursor.moveToNext());
        }
        cursor.close();


        Log.e("体重",choose_weight);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        weather.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                weather_selected = (RadioButton)findViewById(weather.getCheckedRadioButtonId());
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();

            }
        });
    }

    private int getweatherid() {
        switch (choose_weather)
        {
            case "晴":
               return R.id.sunny_change;
            case "阴":
               return R.id.cloudy_change;
            case "雨":
               return R.id.rainy_change;
            case "雪":
               return R.id.snowy_change;
            default:
                return R.id.sunny_change;
        }
    }

    private void saveData() {
        weather_selected = (RadioButton)findViewById(weather.getCheckedRadioButtonId());
        change_weather = "".equals(weather_selected.getText().toString())? choose_weather:weather_selected.getText().toString();
        change_exercise = exercise.getText().toString();
        change_today_weight = today_weight.getText().toString();
        change_remarks = remarks.getText().toString();
        change_feeling = feeling.getText().toString();

        String weightPattern = "^[0-9]+(.[0-9]{1})?$";

        if(change_today_weight.matches(weightPattern)){
            dbHelper = new MyDatabaseHelper(this,landing_name,null,1);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("weather",change_weather);
            values.put("exercise", change_exercise);
            values.put("date", choose_dairy);
            values.put("weight", change_today_weight);
            values.put("feeling",change_feeling);
            values.put("remarks", change_remarks);
            db.update(" Dairy",values,"date = ?", new String[]{choose_date});
            values.clear();
            finish();
        }else{
            final AlertDialog.Builder attention_dialog = new AlertDialog.Builder(this);
            attention_dialog.setTitle("注意").setMessage("请填写正确格式").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            }).show();
        }

    }


}
