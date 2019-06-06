package net.ictcampus.timeking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
         //   AlarmManager alarmLoc = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
           Intent startLocIntent = new Intent(context, AbsenzNotificationService.class);
    // //       PendingIntent pendingStartLoc = PendingIntent.getService(context,0,startLocIntent,0);
     //       Calendar calLoc = Calendar.getInstance();
     //      calLoc.setTimeInMillis(System.currentTimeMillis() + 1000 * 60 * 1);
       //     alarmLoc.setRepeating(AlarmManager.RTC_WAKEUP, calLoc.getTimeInMillis(), 1000 * 60 * 1, pendingStartLoc);
        context.startService(startLocIntent);
        }
    }
}
