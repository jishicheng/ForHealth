# 简介
## ForHealth
一个用于记录每日体重的健康日志，可显示体重趋势及健康指数，让您更好地管理自己。<br>
A Health log for daily weight.The more disciplined, The more free.
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
### 图片模糊效果
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
## 二、主界面（MainActivity）
### 底部导航栏 BottomNavigationView
1.在res文件夹下的menu新建navigation.xml菜单
```java
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">

    <item
        android:id="@+id/navigation_home"
        android:icon="@drawable/ic_home_black_24dp"
        android:title="@string/title_home" />

    <item
        android:id="@+id/navigation_dashboard"
        android:icon="@drawable/ic_dashboard_black_24dp"
        android:title="@string/title_dashboard" />

    <item
        android:id="@+id/navigation_tending"
        android:icon="@drawable/qushi"
        android:title = "@string/title_tending" />

    <item
        android:id="@+id/navigation_notifications"
        android:icon="@drawable/ic_notifications_black_24dp"
        android:title="@string/title_notifications" />

</menu>
```
2.设置监听
```java  
private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_tending:
                    mViewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_notifications:
                    mViewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };
```
```java
   bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
   bottomNavigationView.setSelectedItemId(R.id.navigation_home);
```
3.绑定Viewpager
MainActivity继承ViewPager.OnPageChangeListener，并重写onPageSelected（）方法
```java
    @Override
    public void onPageSelected(int i) {
        menuItem = bottomNavigationView.getMenu().getItem(i);
        menuItem.setChecked(true);
    }
```
# 三、体重趋势界面（TendingFragment）
## 折线图（LineChart）
1.添加第三方库
```java
dependiences{
    implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0-alpha' // android图表控件
}
```
2.在布局文件中加入LineChart控件
```java
  <com.github.mikephil.charting.charts.LineChart
            android:id="@+id/chart"
            android:layout_width="match_parent"
            android:layout_height="350dp"/>
```
3.初始化表格
```java
List<Entry> entries = new ArrayList<>();
//填充数据
        for (int i = 0;i<mdairy.size();i++) {
            entries.add(new Entry(i, (float) Double.parseDouble(mdairy.get(i).today_weight)));
        }
//设置X轴为字符串类型
String[] str = new String[mdairy.size()];
        for(int i = 0;i<mdairy.size();i++){
            str[i] = mdairy.get(i).getDate().split("-",2)[1];
        }
     
        XAxis xAxis = chart.getXAxis();
        XAxisValueFormatter labelFormatter = new XAxisValueFormatter(str);
        xAxis.setValueFormatter(labelFormatter);
//导入数据 
        LineDataSet lineDataSet = new LineDataSet(entries, "体重");
        LineData data = new LineData(lineDataSet);
        chart.setData(data);
```
# Screenshot
![登录界面](https://github.com/jishicheng/ForHealth/blob/master/Screenshot1.jpg)
![功能界面1](https://github.com/jishicheng/ForHealth/blob/master/Screenshot2.jpg)
![功能界面1](https://github.com/jishicheng/ForHealth/blob/master/Screenshot3.jpg)
![功能界面1](https://github.com/jishicheng/ForHealth/blob/master/Screenshot4.jpg)
![功能界面1](https://github.com/jishicheng/ForHealth/blob/master/Screenshot5.jpg)
