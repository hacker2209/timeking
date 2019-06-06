package net.ictcampus.timeking;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import static android.content.Context.ALARM_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, ErinnerungsNotificationService.class);
        service.setData((Uri.parse("custom://"+System.currentTimeMillis())));
        context.startService(service);
    }
}
