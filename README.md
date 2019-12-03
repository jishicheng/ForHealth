# 简介
## ForHealth
一个用于记录每日体重的健康日志，可显示体重趋势及健康指数，让您更好地管理自己。The more disciplined, The more free
# 需要导入的第三方库
在app下的build.gradle下添加dependenices
```java
dependencies{
    implementation 'com.android.support:cardview-v7:28.0.0'
    implementation 'com.android.support:recyclerview-v7:28.0.0'
    implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0-alpha' // android图表控件
    implementation 'jp.wasabeef:blurry:3.0.0' //模糊效果
    implementation 'com.github.addappcn:android-pickers:1.0.3'
    implementation 'com.contrarywind:Android-PickerView:4.1.8'
    implementation 'cn.aigestudio.wheelpicker:WheelPicker:1.1.3'
    implementation 'com.github.bumptech.glide:glide:4.5.0'
    implementation("com.squareup.okhttp3:okhttp:4.2.1")
    implementation 'de.hdodenhof:circleimageview:3.0.1'
    implementation 'uk.co.chrisjenx:calligraphy:2.3.0'
    implementation 'com.squareup.sqlbrite:sqlbrite:1.1.1'}
```
# 各界面主要代码
## 一、登录界面（LandingActivity）
图片模糊效果
在activity中重写onWindowFocusChanged()方法
```java
 public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {         
        Blurry.with(LandingActivity.this)
        .radius(8) //值越大越模糊
        .sampling(2) //对原图像抽样
        .async() //异步
        .capture(background) //对background做高斯模糊
        .into(background); //把结果写入background
        } else {
        }
    }
```


# Screenshot
![登录界面](https://github.com/jishicheng/ForHealth/blob/master/Screenshot1.jpg)
![功能界面1](https://github.com/jishicheng/ForHealth/blob/master/Screenshot2.jpg)
![功能界面1](https://github.com/jishicheng/ForHealth/blob/master/Screenshot3.jpg)
![功能界面1](https://github.com/jishicheng/ForHealth/blob/master/Screenshot4.jpg)
![功能界面1](https://github.com/jishicheng/ForHealth/blob/master/Screenshot5.jpg)
