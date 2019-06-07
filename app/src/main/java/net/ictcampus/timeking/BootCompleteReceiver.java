package net.ictcampus.timeking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
           Intent startLocIntent = new Intent(context, AbsenzNotificationService.class);
        context.startService(startLocIntent);
        }
    }
}
