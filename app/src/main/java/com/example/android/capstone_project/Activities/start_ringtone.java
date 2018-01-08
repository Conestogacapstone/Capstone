package com.example.android.capstone_project.Activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.android.capstone_project.R;

public class start_ringtone extends Service {

    private boolean isRunning;
    MediaPlayer mMediaPlayer;
    private int startId;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

        //This method is to trigger Notification in application
        final NotificationManager mNM = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        //Intent used from one activity to another
        Intent intent1 = new Intent(this.getApplicationContext(), ActivityTwo.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent1, 0);

        Notification mNotify  = new Notification.Builder(this)
                .setContentTitle("Cure Plants - Reminder")
                .setContentText("Click me!")
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();

        String state = intent.getExtras().getString("extra");


        assert state != null;
        switch (state) {
            case "no":
                startId = 0;
                break;
            case "yes":
                startId = 1;
                break;
            default:
                startId = 0;
                break;
        }



        // This method will start the alarm sound
        if(!this.isRunning && startId == 1) {


            mMediaPlayer = MediaPlayer.create(this, R.raw.morning_alarm);

            mMediaPlayer.start();


            mNM.notify(0, mNotify);

            this.isRunning = true;
            this.startId = 0;

        }
        else if (!this.isRunning && startId == 0){

            this.isRunning = false;
            this.startId = 0;

        }

        else if (this.isRunning && startId == 1){

            this.isRunning = true;
            this.startId = 0;

        }
        else {

            mMediaPlayer.stop();
            mMediaPlayer.reset();

            this.isRunning = false;
            this.startId = 0;
        }


        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        this.isRunning = false;
    }

}
