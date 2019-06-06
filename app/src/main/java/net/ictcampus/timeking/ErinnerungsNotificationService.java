package net.ictcampus.timeking;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


public class ErinnerungsNotificationService extends IntentService {

        private int NOTIFICATION_ID=17;
        private String Text = "Du hast noch offene Absenzen";
        private NotificationManager notificationManager;
        private PendingIntent pendingIntent;
        private Notification notification;


    public ErinnerungsNotificationService(String name) {
        super(name);
    }


    @Override
        public IBinder onBind(Intent intent) {
            // TODO: Return the communication channel to the service.
            throw new UnsupportedOperationException("Not yet implemented");
        }

        protected void onHandleIntent(Intent intent){
            Context context = this.getApplicationContext();
            //neuer nofigactionMangeger mit Nofification Service
            notificationManager = (NotificationManager)  context.getSystemService(Context.NOTIFICATION_SERVICE);
            //Wo geht es hin wenn auf Benachrichtigung gedrückt wurde
            Intent mIntent = new Intent(this, MainActivity.class);
            //neues Bundle
            Bundle bundle = new Bundle();
            //zwei Test Strings reinlegen
            bundle.putString("test", "test");
            //Diese ins mIntent legen --> MainActivity
            mIntent.putExtras(bundle);
            //Referenz
            pendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            //Resource hollen
            Resources res = this.getResources();
            //Neue Nofification machen
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            //Typ alarm
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            //einzelheiten der Notification
            notification = new NotificationCompat.Builder(this)
                    .setContentIntent(pendingIntent)
                    .setTicker("ticker value")
                    .setAutoCancel(true)
                    .setPriority(8)
                    .setSound(soundUri)
                    .setContentTitle("Erinnerung")
                    .setContentText(Text).build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;
            notification.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
            notification.ledARGB = 0xFFFFA500;
            notification.ledOnMS = 800;
            notification.ledOffMS = 1000;
            notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, notification);
            Log.i("notif","Notifications sent.");
        }
}
