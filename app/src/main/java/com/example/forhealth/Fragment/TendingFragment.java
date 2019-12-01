package com.example.forhealth.Fragment;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

import com.example.forhealth.LandingActivity;
import com.example.forhealth.R;
import com.example.forhealth.RegisterActivity;
import com.example.forhealth.weight.BMI_Quality;
import com.example.forhealth.weight.BaseApplication;
import com.example.forhealth.weight.Dairy;
import com.example.forhealth.weight.FragmentState;
import com.example.forhealth.weight.MyDatabaseHelper;
import com.example.forhealth.weight.XAxisValueFormatter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.forhealth.LandingActivity.landing_name;
import static java.lang.Integer.parseInt;

public class TendingFragment extends Fragment implements FragmentState {

    private LineChart chart;
    private TextView BMI_word,quality_word;
    private double BMI;
    private View view;
    private String height_m;
    private List<Dairy> mdairy ;
    private MyDatabaseHelper dbHelper;
    private boolean isGetData;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstances){
        view = inflater.inflate(R.layout.tending_fragment,container,false);
        initViews();
        setLineChart();
        return  view;
    }

    private void initViews() {
        chart = (LineChart)view.findViewById(R.id.chart);
        BMI_word = (TextView)view.findViewById(R.id.BMI);
        quality_word = (TextView)view.findViewById(R.id.quality);
        SharedPreferences pref = getActivity().getSharedPreferences(landing_name,MODE_PRIVATE);
        String weight = pref.getString("Weight","1");
        String today_weight = pref.getString("TodayWeight",weight);
        String height = pref.getString("Height","1");
        NumberFormat formatter = new DecimalFormat("0.00");
        height_m = formatter.format((float)Double.parseDouble(height)/100);
        Log.e("身高",height_m);
        BMI = Double.parseDouble(today_weight)/(Double.parseDouble(height_m)*Double.parseDouble(height_m));
        String BMIString = formatter.format(BMI);
        Log.e("BMI",BMIString);
        BMI_word.setText(BMIString);
        String quality = BMI_Quality.ComfirmQuality(BMI);
        quality_word.setText(quality);
    }

    private void setLineChart() {
        mdairy = GetDairy();
        List<Entry> entries = new ArrayList<>();
        String[] str = new String[mdairy.size()];
        for(int i = 0;i<mdairy.size();i++){
            str[i] = mdairy.get(i).getDate().split("-",2)[1];
        }
        for (int i = 0;i<mdairy.size();i++) {
            entries.add(new Entry(i, (float) Double.parseDouble(mdairy.get(i).today_weight)));
        }
        XAxis xAxis = chart.getXAxis();
        XAxisValueFormatter labelFormatter = new XAxisValueFormatter(str);
        xAxis.setValueFormatter(labelFormatter);

        //一个LineDataSet就是一条线
        LineDataSet lineDataSet = new LineDataSet(entries, "体重");
        lineDataSet.setColor(Color.parseColor("#8B6914"));
        lineDataSet.setCircleColor(Color.parseColor("#8B6914"));
        lineDataSet.setCircleRadius(5f);
        lineDataSet.setLineWidth(2f);

        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.NONE);
        legend.setTextColor(Color.WHITE);

        Description description = new Description();
        description.setEnabled(false);
        chart.setDescription(description);


        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setStartAtZero(false);
        leftAxis.setAxisMinValue(60);
        leftAxis.setAxisMaxValue(80);
        SharedPreferences pref = getActivity().getSharedPreferences(landing_name,MODE_PRIVATE);
        LimitLine limitLine = new LimitLine(parseInt(pref.getString("Weight_aim","60")),"目标体重");
        limitLine.setLineColor(Color.GRAY );
        limitLine.setTextSize(12f);
        limitLine.setTextColor(Color.TRANSPARENT);
        leftAxis.addLimitLine(limitLine);

        xAxis.setTextColor(Color.parseColor("#333333"));
        xAxis.setTextSize(8f);
        xAxis.setAxisMinimum(0f);
        xAxis.setDrawAxisLine(true);//是否绘制轴线
        xAxis.setDrawGridLines(false);//设置x轴上每个点对应的线
        xAxis.setDrawLabels(true);//绘制标签  指x轴上的对应数值
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置
        xAxis.setGranularity(1f);//禁止放大后x轴标签重绘

        LineData data = new LineData(lineDataSet);
        chart.setData(data);
    }

    private List<Dairy> GetDairy() {

        mdairy = new ArrayList<>();
        //数据库读取dairy
        dbHelper = new MyDatabaseHelper(getActivity(),landing_name,null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Dairy",null,null,null,null,null,null);
        if(cursor.moveToNext()){
            do{
                String weather = cursor.getString(cursor.getColumnIndex("weather"));
                String exercise = cursor.getString(cursor.getColumnIndex("exercise"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String weight = cursor.getString(cursor.getColumnIndex("weight"));
                String feeling = cursor.getString(cursor.getColumnIndex("feeling"));
                String remarks = cursor.getString(cursor.getColumnIndex("remarks"));
                Dairy dairy = new Dairy();
                dairy.weather = weather;
                dairy.exercise = exercise;
                dairy.today_weight = weight;
                dairy.remarks = remarks;
                dairy.feeling = feeling;
                dairy.date = date;
                mdairy.add(dairy);
//                Collections.sort(mdairy,new DairyFragment.DairyComparator());
            }while (cursor.moveToNext());
        }
        cursor.close();
        return  mdairy;
    }


    @Override
    public void fragmentVisible() {
        setLineChart();
        chart.notifyDataSetChanged(); // let the chart know it's data changed
        chart.invalidate(); // refresh
    }

}
