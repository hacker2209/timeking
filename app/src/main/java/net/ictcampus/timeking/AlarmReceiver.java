package net.ictcampus.timeking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, MainActivity.class);
        System.out.println("-------------------------------------------------------------------------------------------------------");
        System.out.println(Uri.parse("custom://"+System.currentTimeMillis()));
        service.setData((Uri.parse("custom://"+System.currentTimeMillis())));
        context.startService(service);
    }
}
