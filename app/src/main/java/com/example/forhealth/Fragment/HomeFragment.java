package com.example.forhealth.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.forhealth.LandingActivity;
import com.example.forhealth.R;
import com.example.forhealth.RegisterActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;
import static com.example.forhealth.LandingActivity.landing_name;

public class HomeFragment extends Fragment {

    private TextView aim_weight,start_time_word;
    private View view;
    private String start_time;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
         view = inflater.inflate(R.layout.home_fragment,container,false);
        initViews();
        return view;
    }

    private void initViews() {
        aim_weight = (TextView)view.findViewById(R.id.aim_weight);
        start_time_word = (TextView)view.findViewById(R.id.start_time_word);
        SharedPreferences pref = getActivity().getSharedPreferences(landing_name,MODE_PRIVATE);
        aim_weight.setText(pref.getString("Weight_aim",""));
        start_time = pref.getString("StartDate",null);
        if(start_time != null){
            start_time_word.setText(start_time);
            }else{
            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");//转型
            start_time = format.format(Calendar.getInstance().getTime());
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(landing_name,MODE_PRIVATE).edit();
            editor.putString("StartDate", start_time );
            editor.apply();
            start_time_word.setText(start_time);
        }
    }

}
