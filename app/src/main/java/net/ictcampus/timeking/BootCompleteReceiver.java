package net.ictcampus.timeking;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            AlarmManager alarmLocation = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            Intent startLocIntent = new Intent(context, AbsenzNotificationService.class);
            PendingIntent sender = PendingIntent.getService(context, 0, startLocIntent, 0);
            alarmLocation.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 30, sender); // Millisec * Second * Minute


        }
    }
}
