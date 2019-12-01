package com.example.forhealth;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.forhealth.Fragment.DairyFragment;
import com.example.forhealth.weight.BaseApplication;
import com.example.forhealth.weight.Dairy;
import com.example.forhealth.weight.MyDatabaseHelper;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import rx.schedulers.Schedulers;

import static com.example.forhealth.LandingActivity.landing_name;

public class AddDairy extends AppCompatActivity {

    private Button cancel;
    private Button save;
    private RadioGroup weather;
    private EditText exercise;
    private EditText today_weight;
    private EditText feeling;
    private EditText remarks;
    private RadioButton weather_selected;

    private MyDatabaseHelper dbHelper;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initViews();
    }

    private void initViews() {
        cancel = (Button)findViewById(R.id.cancel);
        save = (Button)findViewById(R.id.save);
        weather = (RadioGroup)findViewById(R.id.weather);
        exercise = (EditText)findViewById(R.id.exercise);
        today_weight = (EditText)findViewById(R.id.today_weight);
        feeling = (EditText)findViewById(R.id.feeling);
        remarks = (EditText)findViewById(R.id.remarks);

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

    private void saveData() {
        Dairy dairy = new Dairy();
        dairy.weather = weather_selected.getText().toString();
        dairy.exercise = exercise.getText().toString();
        dairy.today_weight = today_weight.getText().toString();
        dairy.remarks = remarks.getText().toString();
        dairy.feeling = feeling.getText().toString();
        Calendar calendar = Calendar.getInstance();
        dairy.date = DateFormat.format("yyyy-MM-dd",calendar.getTime()).toString();
        String weightPattern = "^[0-9]+(.[0-9]{1})?$";

        if(dairy.today_weight.matches(weightPattern)){
            SharedPreferences.Editor editor = getSharedPreferences(landing_name,MODE_PRIVATE).edit();
            editor.putString("TodayWeight",dairy.today_weight);
            editor.apply();

            dbHelper = new MyDatabaseHelper(this,landing_name,null,1);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("weather",dairy.weather);
            values.put("exercise", dairy.exercise);
            values.put("date", dairy.date);
            values.put("weight", dairy.today_weight);
            values.put("feeling",dairy.feeling);
            values.put("remarks", dairy.remarks);
            db.insert(" Dairy",null,values);
            values.clear();
            Cursor cursor = db.query("Dairy", null,"date = ?", new String[]{dairy.date},null,null,null);
            int count = cursor.getCount();
            Log.e("数目", String.valueOf(count));
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

    public static void actionStart(Context context) {
        Intent intent = new Intent(context,AddDairy.class);
        context.startActivity(intent);
    }

}
