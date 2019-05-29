package net.ictcampus.timeking;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            AlarmManager locListen = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            Intent startLoc = new Intent(context,AbsenzNotificationService.class);
            PendingIntent startLocPending= PendingIntent.getService(context,0,startLoc,0);
            locListen.setRepeating(AlarmManager.RTC_WAKEUP,0,1000*60*5, startLocPending);
        }
    }
}
