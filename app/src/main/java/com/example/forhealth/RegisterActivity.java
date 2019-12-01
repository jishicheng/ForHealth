package com.example.forhealth;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    private static final int CHOOSE_PHOTO = 2;
    private EditText name_word,password;
    private ImageView choice_sex,choice_birthday,choice_height,choice_weight,choice_weight_aim;
    private CircleImageView icon;
    private TextView sex_word,birthday_word,height_word,weight_word,weight_aim_word;
    private String[] sex_choices = {"男","女"};
    private Calendar mCalendar;
    private String birthday,height,weight,sex,weight_aim;
    private TextView information_done;
    public static String name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
    }

    private void initViews() {
        icon = (CircleImageView) findViewById(R.id.icon);
        name_word = (EditText)findViewById(R.id.name_word);
        password = (EditText)findViewById(R.id.password);
        choice_sex = (ImageView)findViewById(R.id.choice_sex);
        sex_word = (TextView)findViewById(R.id.sex_word);
        choice_birthday = (ImageView)findViewById(R.id.choice_birthday);
        birthday_word = (TextView)findViewById(R.id.birthday_word);
        choice_height = (ImageView)findViewById(R.id.choice_height);
        height_word = (TextView)findViewById(R.id.height_word);
        choice_weight = (ImageView)findViewById(R.id.choice_weight);
        weight_word = (TextView)findViewById(R.id.weight_word);
        choice_weight_aim = (ImageView) findViewById(R.id.choice_weight_aim);
        weight_aim_word = (TextView)findViewById(R.id.weight_aim_word);
        information_done = (TextView)findViewById(R.id.information_done);

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(ContextCompat.checkSelfPermission(RegisterActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(RegisterActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                    }else{
                        openAlbum();
                    }
            }
        });
        choice_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseSex();
            }
        });
        choice_birthday.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 ChooseBirthday();
                 }
             }
        );
        choice_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseHeight();
            }
        });
        choice_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseWeight();
            }
        });
        choice_weight_aim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseAimWeight();
            }
        });
              information_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = name_word.getText().toString();
                if(name_word.getText() == null||password.getText() == null||sex == null||birthday == null||height == null||weight == null){
                  final AlertDialog.Builder attention_dialog = new AlertDialog.Builder(RegisterActivity.this);
                         attention_dialog.setTitle("注意").setMessage("请填写完整全部数据").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }else{
                    SharedPreferences.Editor editor = getSharedPreferences(name,MODE_PRIVATE).edit();
                    //将图片转化成64位字节流，存储到本地
                    ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                    ((BitmapDrawable)icon.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
                    String headPicBase64=new String(Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT));
                    editor.putString("headPic",headPicBase64);
                    editor.putString("name", name_word.getText().toString());
                    editor.putString("password", password.getText().toString());
                    editor.putString("Sex",sex);
                    editor.putString("Birthday",birthday);
                    editor.putString("Height",height);
                    editor.putString("Weight",weight);
                    editor.putString("Weight_aim",weight_aim);
                    editor.apply();
                    RegisterActivity.this.finish();
                }
            }
        });
    }

    private void openAlbum() {
        Log.e("打开","相册2");
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch(requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else {
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
                default:
        }
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch(requestCode){
            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK){
                    Log.e("打开","相册3");
                    handleImageOnKitKat(data);
                }
                break;
             default:
                 break;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            imagePath = getImagePath(uri,null);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
        }


    private void displayImage(String imagePath) {
        if(imagePath !=null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            icon.setImageBitmap(bitmap);
        }
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor !=null){
            if(cursor.moveToNext()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
       return path;
    }


    private void ChooseSex() {
        new AlertDialog.Builder(RegisterActivity.this).setItems(sex_choices,  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sex = sex_choices[which];
                sex_word.setText(sex);
                dialog.dismiss();
            }
        }).show();
    }

    private void ChooseBirthday() {
        mCalendar = Calendar.getInstance();
        DatePickerDialog pickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker arg0, int year, int month, int day) {
                mCalendar.set(year, month, day);//将点击获得的年月日获取到calendar中。
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");//转型
                birthday = format.format(mCalendar.getTime());
                birthday_word.setText(birthday);
            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        pickerDialog.show();
    }

    private void ChooseHeight(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.height,null);
        WheelPicker height_picker = (WheelPicker) v.findViewById(R.id.height_picker);
        final List<Integer> data = new ArrayList<>();
        for (int i = 150; i < 201; i++){
            data.add(i);
        }
        height_picker.setData(data);
        height_picker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                height = String.valueOf(data);
                height_word.setText(height);
            }
        });
        new AlertDialog.Builder(this).setView(v).show();
    }

    private void ChooseWeight() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.weight,null);
        WheelPicker height_picker = (WheelPicker) v.findViewById(R.id.weight_picker);
        final List<Integer> data = new ArrayList<>();
        for (int i = 45; i < 101; i++){
            data.add(i);
        }
        height_picker.setData(data);
        height_picker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                weight = String.valueOf(data);
                weight_word.setText(weight);
            }
        });
        new AlertDialog.Builder(this).setView(v).show();
    }

    private void ChooseAimWeight() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.weight,null);
        WheelPicker height_picker = (WheelPicker) v.findViewById(R.id.weight_picker);
        final List<Integer> data = new ArrayList<>();
        for (int i = 45; i < 101; i++){
            data.add(i);
        }
        height_picker.setData(data);
        height_picker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                weight_aim = String.valueOf(data);
                weight_aim_word.setText(weight_aim);
            }
        });
        new AlertDialog.Builder(this).setView(v).show();
    }

    public static void actionStart (Context context) {
        Intent intent = new Intent(context,RegisterActivity.class);
        context.startActivity(intent);
    }
}
