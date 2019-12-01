package com.example.forhealth.weight;

import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.forhealth.AddDairy;
import com.example.forhealth.ChangeDairy;
import com.example.forhealth.R;

import java.lang.reflect.Field;
import java.util.List;

import static com.example.forhealth.LandingActivity.landing_name;

public class DairyAdapter extends RecyclerView.Adapter <DairyAdapter.ViewHolder> {

    private List<Dairy> mdairy;
    Context context;
    private OnLongClickListener mLongClickListener;
    private RecyclerView recyclerView;
    private View view;
    private MyDatabaseHelper dbHelper;

    public void setLongClickListener(OnLongClickListener longClickListener){
       mLongClickListener =  longClickListener;
    }

    public interface OnLongClickListener{
       boolean onLongClick(int position);
    }

    public DairyAdapter(List<Dairy> dairyList, Context context) {
        mdairy = dairyList;
        this.context = context;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView weatherImage;
        TextView exercise;
        TextView date;
        TextView today_weight;

        public ViewHolder(View itemView) {
            super(itemView);
            weatherImage = (ImageView) itemView.findViewById(R.id.WeatherImage);
            exercise = (TextView) itemView.findViewById(R.id.exercise);
            date = (TextView) itemView.findViewById(R.id.date);
            today_weight = (TextView) itemView.findViewById(R.id.today_weight);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dairy_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        Dairy dairy = mdairy.get(i);
        Weather mWeather = new Weather(dairy.weather, context);
        viewHolder.weatherImage.setImageResource(mWeather.exchange());
        viewHolder.exercise.setText(dairy.getExercise());
        viewHolder.date.setText(dairy.getDate());
        viewHolder.today_weight.setText(dairy.getToday_weight());
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context,viewHolder.today_weight);
                popupMenu.getMenuInflater().inflate(R.menu.menu2,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.delete){
                            if(mdairy.size() == 1){
                                final AlertDialog.Builder attention_dialog = new AlertDialog.Builder(context);
                                attention_dialog.setTitle("注意").setMessage("数据只剩一条了哦，您可修改数据").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
                            }else {
                                String date = mdairy.get(i).getDate();
                                Log.e("日期",date);
                                Log.e("选中", String.valueOf(i));
                                dbHelper = new MyDatabaseHelper(context,landing_name,null,1);
                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                db.delete("Dairy","date = ?", new String[]{date});
                                mdairy.remove(i);
                                notifyItemRemoved(i);//刷新被删除的地方
                                notifyItemRangeChanged(i,getItemCount());
                            }
                        }else if(item.getItemId() == R.id.changedata){
                            String date = mdairy.get(i).getDate();
                            Intent intent = new Intent(context,ChangeDairy.class);
                            intent.putExtra("choose_dairy",date);
                            context.startActivity(intent);
                        }
                        return false;
                    }
                });
                popupMenu.show();

                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mdairy.size();
    }


}