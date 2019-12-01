package com.example.forhealth.weight;

import android.app.Application;
import android.graphics.Typeface;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BaseApplication extends Application {

    public void onCreate() {
        super.onCreate();
        Typeface mTypeface = Typeface.createFromAsset(getAssets(), "fonts/FZWBJ.TTF");
        Field field = null;
        try {
            field = Typeface.class.getDeclaredField("MONOSPACE");
            field.setAccessible(true);
            field.set(null,mTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}