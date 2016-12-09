package com.example.nghia.coo;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by nghia on 17/11/2016.
 */

public class ScheduleAdapter extends BaseAdapter{
    Context context;
    int mLayout;
    List<ScheduleObject> arraySchedule;
    public ScheduleAdapter(Context context, int mLayout, List<ScheduleObject> arraySchedule) {
        this.context = context;
        this.mLayout = mLayout;
        this.arraySchedule = arraySchedule;
    }

    @Override
    public int getCount() {
        return arraySchedule.size();
    }

    @Override
    public Object getItem(int position) {
        return arraySchedule.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class ViewHolder {
        ImageView coverSchedule;
        TextView name,datetime;
        Button buttoncancel,viewrecipe;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowview = convertView;
        ScheduleAdapter.ViewHolder holder=new ScheduleAdapter.ViewHolder();
        if(rowview==null){
            rowview=inflater.inflate(mLayout,null);
            holder.coverSchedule=(ImageView) rowview.findViewById(R.id.imageSchedule);
            holder.name=(TextView) rowview.findViewById(R.id.textnamevent);
            holder.datetime=(TextView) rowview.findViewById(R.id.textdatetime);
            holder.buttoncancel=(Button) rowview.findViewById(R.id.buttoncancelalarm);
            holder.viewrecipe=(Button) rowview.findViewById(R.id.viewRecipe);
            holder.viewrecipe.setTag(position);
            holder.buttoncancel.setTag(position);
            rowview.setTag(holder);

        }else{
            holder =(ViewHolder) rowview.getTag();
        }
        holder.name.setText(arraySchedule.get(position).nameevent);
        holder.datetime.setText(arraySchedule.get(position).datetime);
        final int idCancel = arraySchedule.get(position).idevent;
        final String curuser=arraySchedule.get(position).userkey;
        final String curkey=arraySchedule.get(position).recipekey;
        Picasso.with(context).load(arraySchedule.get(position).imagecover).into(holder.coverSchedule);

      // LoadData(curuser,curkey);

        final SQLite db = new SQLite(context,"Shopping.sqlite",null,1);
        holder.buttoncancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                cancelNotification(idCancel);
                                db.QueryData("DELETE FROM Schedule WHERE Id="+idCancel);
                                PlannerFragment fragment = new PlannerFragment();
                                android.support.v4.app.FragmentTransaction fragmentTransaction =
                                        ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_container, fragment);
                                fragmentTransaction.commit();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }


                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(context.getString(R.string.confirmevent)).setPositiveButton(R.string.yes, dialogClickListener)
                        .setNegativeButton(R.string.no, dialogClickListener).show();
            }
        });
        holder.viewrecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("Recipes").child(curkey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Recipe rp=dataSnapshot.getValue(Recipe.class);
                        goViewRecipeScreen(rp,new UserKey(curuser,curkey));

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        return rowview;
    }

       private void cancelNotification(int id){
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, getNotification("cancel"));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.cancel(pendingIntent);
    }
    private void goViewRecipeScreen(Recipe currecipe,UserKey curuserkey) {
        Intent intent = new Intent(context, ViewRecipeActivity.class);
        intent.putExtra("TheRecipe",currecipe);
        intent.putExtra("UserKey",curuserkey);
        context.startActivity(intent);
    }
    private Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
        return builder.build();
    }
}
