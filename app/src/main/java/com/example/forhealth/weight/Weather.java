package com.example.forhealth.weight;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.support.v7.app.AppCompatActivity;


public class Weather  {
    String weather_text ;
    Context context;

    public Weather(String weather_text,Context context) {
        this.weather_text = weather_text;
        this.context = context;
    }



    public int exchange(){
        ApplicationInfo appInfo = context.getApplicationInfo();
       switch (weather_text){
           case "晴" :
              return context.getResources().getIdentifier("qingtian_pressed", "mipmap", appInfo.packageName);

           case "阴" :
               return context.getResources().getIdentifier("duoyun_pressed", "mipmap", appInfo.packageName);

           case "雨" :
               return context.getResources().getIdentifier("yu_pressed", "mipmap", appInfo.packageName);

           case "雪" :
               return context.getResources().getIdentifier("xue_pressed", "mipmap", appInfo.packageName);

               default:
                   return 0;
       }
    }
}
