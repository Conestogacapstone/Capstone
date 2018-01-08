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

    AlarmManager alarmManager;
    private PendingIntent intent_pending;

    private TimePicker reminderTime;
    private TextView reminder;

    ActivityTwo inst;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);


        // Add back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        this.context = this;

        reminder = (TextView) findViewById(R.id.reminder);

        final Intent myIntent = new Intent(this.context, ReminderReceiver.class);

        // Get the alarm manager service
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // set the alarm to the time that you picked
        final Calendar calendar = Calendar.getInstance();

        reminderTime = (TimePicker) findViewById(R.id.alarmTimePicker);


        Button start_alarm = (Button) findViewById(R.id.start_alarm);
        start_alarm.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)

            @Override
            public void onClick(View v) {

                calendar.add(Calendar.SECOND, 3);

                final int hour = reminderTime.getCurrentHour();
                final int minute = reminderTime.getCurrentMinute();
                ;


                calendar.set(Calendar.HOUR_OF_DAY, reminderTime.getCurrentHour());
                calendar.set(Calendar.MINUTE, reminderTime.getCurrentMinute());

                myIntent.putExtra("extra", "yes");
                intent_pending = PendingIntent.getBroadcast(ActivityTwo.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intent_pending);


                // Setting alarm text
                setAlarmText("Reminder Set to " + hour + ":" + minute);
            }

        });

        Button stop_alarm = (Button) findViewById(R.id.stop_alarm);
        stop_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myIntent.putExtra("extra", "no");
                sendBroadcast(myIntent);

                setAlarmText("Reminder Reset Is Done");

            }
        });

    }

    public void setAlarmText(String alarmText) {
        reminder.setText(alarmText);
    }


    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }


}
