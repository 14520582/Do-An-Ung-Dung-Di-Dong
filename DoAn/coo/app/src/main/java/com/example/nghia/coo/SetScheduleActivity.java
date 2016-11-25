package com.example.nghia.coo;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;

public class SetScheduleActivity extends AppCompatActivity {
    ImageView coverSchedule;
    UserKey curUserKey=new UserKey();
    String photoCover;
    EditText nameEvent;
    Button setbutton,setdate,settime;

    int h,m,ye,mo,da;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_schedule);
        Mapping();
        final SQLite db = new SQLite(this,"Shopping.sqlite",null,1);
        Picasso.with(this).load(photoCover).into(coverSchedule);
        Calendar currentTime = Calendar.getInstance();
        h = currentTime.get(Calendar.HOUR_OF_DAY);
        m = currentTime.get(Calendar.MINUTE);
        currentTime.add(Calendar.DAY_OF_MONTH,1);
        ye= currentTime.get(Calendar.YEAR);
        mo= currentTime.get(Calendar.MONTH);
        mo+=1;
        da= currentTime.get(Calendar.DAY_OF_MONTH);
        settime.setText( h + ":" + m);
        setdate.setText(da+"/"+mo+"/"+ye);
        settime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(SetScheduleActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        settime.setText( selectedHour + ":" + selectedMinute);
                        h=selectedHour;
                        m=selectedMinute;
                    }
                }, h, m, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        setdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog;
                datePickerDialog =new DatePickerDialog(SetScheduleActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month+=1;
                        setdate.setText(dayOfMonth+"/"+month+"/"+year);
                        ye=year;
                        mo=month;
                        da=dayOfMonth;
                    }
                },ye,mo-1,da);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();
            }
        });
        setbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(nameEvent.getText().toString().trim().length()!=0){
                int idalarm;
                String datetime="";
                datetime=h+":"+m+" "+da+"/"+mo+"/"+ye;
                db.QueryData("CREATE TABLE IF NOT EXISTS Schedule(Id INTEGER PRIMARY KEY AUTOINCREMENT,Name VARCHAR,DateTime VARCHAR,Image TEXT,User VARCHAR,Key VARCHAR)");
                db.QueryData("INSERT INTO Schedule VALUES(null,'"+nameEvent.getText().toString()+"','"+datetime+"','"+photoCover+"','"+curUserKey.user+"','"+curUserKey.key+"')");
                Cursor cur =db.GetData("SELECT Id FROM Schedule WHERE Id=(SELECT MAX(Id)  FROM Schedule)");
                while(cur.moveToNext()) {
                    idalarm=cur.getInt(0);
                    setNotification(nameEvent.getText().toString(),idalarm);
                    Toast.makeText(SetScheduleActivity.this,getString(R.string.addontoschedule),Toast.LENGTH_LONG).show();
                }}
                else
                   Toast.makeText(SetScheduleActivity.this,getString(R.string.fillnameevent),Toast.LENGTH_LONG).show();
            }
        });
    }
    private Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(getString(R.string.notification));
        builder.setContentText(content);
        builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
        return builder.build();
    }
    private void setNotification(String message,int id){
        AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        // int id=Integer.parseInt(editText.getText().toString());
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, getNotification(message));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Date dat  = new Date();//initializes to now
        Calendar cal_alarm = Calendar.getInstance();
        Calendar cal_now = Calendar.getInstance();
        cal_now.setTime(dat);
        cal_alarm.setTime(dat);
        cal_alarm.set(Calendar.MONTH,mo-1);
        cal_alarm.set(Calendar.YEAR,ye);
        cal_alarm.set(Calendar.DAY_OF_MONTH,da);
        cal_alarm.set(Calendar.HOUR_OF_DAY,h);//set the alarm time
        cal_alarm.set(Calendar.MINUTE, m);
        cal_alarm.set(Calendar.SECOND,0);
        if(cal_alarm.before(cal_now)){//if its in the past increment
            cal_alarm.add(Calendar.DATE,1);
        }

        alarmMgr.set(AlarmManager.RTC, cal_alarm.getTimeInMillis(),pendingIntent);

    }
  /*  private void cancelNotification(){
        AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, getNotification("cancel"));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, Integer.parseInt(editText.getText().toString()), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.cancel(pendingIntent);
    }*/
    private void Mapping(){
        settime=(Button) findViewById(R.id.buttonsettime);
        setbutton=(Button) findViewById(R.id.buttonaddplanner);
        setdate=(Button) findViewById(R.id.buttonsetdate);
        nameEvent=(EditText) findViewById(R.id.nameeventText);
        coverSchedule=(ImageView) findViewById(R.id.imageSchedule);
        curUserKey=(UserKey)getIntent().getSerializableExtra("UserKey");
        photoCover=(String) getIntent().getSerializableExtra("Image");
    }
}
