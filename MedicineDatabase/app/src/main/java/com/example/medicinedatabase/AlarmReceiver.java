package com.example.medicinedatabase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {

    private MediaPlayer mp;

    @Override
    public void onReceive(Context context, Intent intent) {

        // extracting bundle from intent
        Bundle bundle = intent.getExtras();
        String medName = bundle.getString("med");

        // playing a ringtone
        mp = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
        mp.setLooping(true);
        mp.start();
        Toast.makeText(context, "ALARM RINGING", Toast.LENGTH_LONG).show();

        // stopping after 5 seconds
        Handler handler = new Handler();
        handler.postDelayed(() -> mp.stop(),25000);

        // Creating Notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,"sid");
        // setting title, description, icon and priority of notification
        mBuilder.setContentTitle("Time to take " + medName + " medicine!")
                .setContentText("Please Take " + medName + " now.")
                .setSmallIcon(R.drawable.clock)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123,mBuilder.build());
    }
}
