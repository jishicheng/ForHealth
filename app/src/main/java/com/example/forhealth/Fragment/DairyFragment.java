package com.example.forhealth.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.forhealth.AddDairy;
import com.example.forhealth.R;
import com.example.forhealth.weight.Dairy;
import com.example.forhealth.weight.DairyAdapter;
import com.example.forhealth.weight.HttpUtil;
import com.example.forhealth.weight.MyDatabaseHelper;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import okhttp3.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.example.forhealth.LandingActivity.landing_name;


public class DairyFragment extends Fragment {

    private Button addButton;
    private RecyclerView dairy_recyclerview;
    private DairyAdapter dairyAdapter;
    private TextView WeightTextView;
    private ImageView bing_pic_img;
    private List<Dairy> mdairy;
    private View view;
    private LinearLayout recycler_linearlayout;
    private String date;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstances){

        view = inflater.inflate(R.layout.dairy_fragment,container,false);
        addButton = (Button)view.findViewById(R.id.add);
        dairy_recyclerview = (RecyclerView)view.findViewById(R.id.dairy_recyclerView);
        recycler_linearlayout = (LinearLayout)view.findViewById(R.id.recycler_linearlayout);

        WeightTextView = (TextView)view.findViewById(R.id.weight);
        bing_pic_img = (ImageView)view.findViewById(R.id.bing_pic_img);

        Calendar calendar = Calendar.getInstance();
        date = DateFormat.format("yyyy-MM-dd",calendar.getTime()).toString();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper dbHelper = new MyDatabaseHelper(getActivity(),landing_name,null,1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor = db.query("Dairy", null,"date = ?", new String[]{date},null,null,null);
                int count = cursor.getCount();
                Log.e("数目", String.valueOf(count));
                if(count > 0){
                    final AlertDialog.Builder attention_dialog = new AlertDialog.Builder(getActivity());
                    attention_dialog.setTitle("注意").setMessage("您今日已有记录").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
                }else{
                    AddDairy.actionStart(getContext());
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        dairy_recyclerview.setLayoutManager(layoutManager);
        dairyAdapter = new DairyAdapter(GetDairy(),getActivity());
        Log.e("长按2","菜单");
        dairy_recyclerview.setAdapter(dairyAdapter);
        dairyAdapter.notifyDataSetChanged();

            loadBingPic();
        return view;
    }
        private List<Dairy> GetDairy() {

        mdairy = new ArrayList<>();
        //数据库读取dairy
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(getActivity(),landing_name,null,1);
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
//                Collections.sort(mdairy,new DairyComparator());
            }while (cursor.moveToNext());
        }
        cursor.close();
        return  mdairy;
    }

    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
            final String bingPic = response.body().string();
//            SharedPreferences pref = getActivity().getSharedPreferences(landing_name,Context.MODE_PRIVATE);
//            String bingPic_stored = pref.getString("bing_pic","");
//                SharedPreferences.Editor editor = getActivity().getSharedPreferences(landing_name,Context.MODE_PRIVATE).edit();
//                editor.putString("bing_pic", bingPic);
//                editor.apply();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(DairyFragment.this).load(bingPic).into(bing_pic_img);
                    }
                });
            }
        });
    }

    static class DairyComparator implements Comparator<Dairy> {
        @Override
        public int compare(Dairy dairy1, Dairy dairy2) {
            return dairy2.date.compareTo(dairy1.date);
        }
    }


    public void onResume() {
        super.onResume();
        dairyAdapter = new DairyAdapter(GetDairy(),getActivity());
        dairy_recyclerview.setAdapter(dairyAdapter);
        dairyAdapter.notifyDataSetChanged();
        SharedPreferences pref = getActivity().getSharedPreferences(landing_name,Context.MODE_PRIVATE);
        String today_weight = pref.getString("TodayWeight","0");
        WeightTextView.setText(today_weight);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {//展示
            dairyAdapter = new DairyAdapter(GetDairy(),getActivity());
            dairy_recyclerview.setAdapter(dairyAdapter);
            dairyAdapter.notifyDataSetChanged();
        }
    }

}
