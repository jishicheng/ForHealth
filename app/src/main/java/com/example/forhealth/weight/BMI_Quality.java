package com.example.forhealth.weight;

public class BMI_Quality {

    private static String BMI1 = "偏瘦";
    private static String BMI2 = "正常";
    private static String BMI3 = "偏胖";
    private static String BMI4 = "肥胖";
    private static String BMI5 = "重度肥胖";

    public static String ComfirmQuality(double weight){
        if(weight<18.5){
            return BMI1;
        }else if(18.5 <= weight && weight<24){
            return BMI2;
        }else if(24<=weight && weight<27){
            return BMI3;
        }else if(27<=weight && weight<30){
            return BMI4;
        }else
            return BMI5;
        }
    }
