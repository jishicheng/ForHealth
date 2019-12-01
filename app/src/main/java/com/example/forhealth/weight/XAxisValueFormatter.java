package com.example.forhealth.weight;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

public class XAxisValueFormatter  implements IAxisValueFormatter {

    private String[] mLabels;

    public XAxisValueFormatter(String[] labels){
        mLabels = labels;
    }
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        try{
            return mLabels[(int) value];
        }catch (Exception e){
            e.printStackTrace();
            return mLabels[0];
        }
    }
}
