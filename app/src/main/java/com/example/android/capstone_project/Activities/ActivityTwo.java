package com.example.android.capstone_project.Activities;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.android.capstone_project.R;

import java.util.Calendar;

public class ActivityTwo extends AppCompatActivity {

    AlarmManager Manageralarm;
    private PendingIntent pending;
    private TimePicker Time;
    private TextView mreminder;
    ActivityTwo activity;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);


        // Add back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        this.context = this;
        mreminder = (TextView) findViewById(R.id.reminder);//getting mreminder text view
        final Intent myIntent = new Intent(this.context, ReminderReceiver.class);

        // Alarm manager service
        Manageralarm = (AlarmManager) getSystemService(ALARM_SERVICE);

        // This will set the picked time by the user
        final Calendar mcal = Calendar.getInstance();

        Time = (TimePicker) findViewById(R.id.Time);

        //This is the set button to reminder
        Button start_alarm = (Button) findViewById(R.id.start);//Getting start alarm button here
        start_alarm.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)

            @Override
            public void onClick(View v) {

                mcal.add(Calendar.SECOND, 3);
                final int hour = Time.getCurrentHour();
                final int minute = Time.getCurrentMinute();
                mcal.set(Calendar.HOUR_OF_DAY, Time.getCurrentHour());
                mcal.set(Calendar.MINUTE, Time.getCurrentMinute());
                myIntent.putExtra("extra", "yes");
                pending = PendingIntent.getBroadcast(ActivityTwo.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                Manageralarm.set(AlarmManager.RTC_WAKEUP, mcal.getTimeInMillis(), pending);

                // Setting alarm text
                setAlarmText("Reminder Set to " + hour + ":" + minute);
            }
        });

        //This is the stop button to reminder
        Button stop_alarm = (Button) findViewById(R.id.stop);
        stop_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myIntent.putExtra("extra", "no");
                sendBroadcast(myIntent);
                setAlarmText("Reminder Reset Is Done");
            }
        });
    }

    //Here the alarm is been set to text field
    public void setAlarmText(String alarmText) {
        mreminder.setText(alarmText);
    }


    @Override
    public void onStart() {
        super.onStart();
        activity = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }


}
