package net.ictcampus.timeking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, AbsenzNotificationService.class);
        service.setData((Uri.parse("custom://"+System.currentTimeMillis())));
        context.startService(service);
    }
}
