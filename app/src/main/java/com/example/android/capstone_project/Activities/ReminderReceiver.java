package com.example.android.capstone_project.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getExtras().getString("extra");

        Intent intent1 = new Intent(context, start_ringtone.class);
        intent1.putExtra("extra", state);

        context.startService(intent1);
    }
}
