package net.ictcampus.timeking;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class AbsenzNotificationService extends Service {
    final int NOTIFICATION_ID=16;
    public Location lastLocation;
    private LocationListener ll;
    private Location gibbLoc;
    private double currentLong;
    private double currentLat;
    private static double GIBBLONG= 7.444840;
    private static double GIBBLAT=  46.954680;
    private float distanceGibb;



    private String title;
    private String content;

    public AbsenzNotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Neuer LocationListener
        ll = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Werte auslesen
                currentLong=location.getLongitude();
                currentLat=location.getLatitude();
                lastLocation=location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (checkDistance()){
            //Meldung die Ausgegeben wird
            displayNotification("ALARM!!","Du bist in der nähe der Gibb dreh dich um und renn weg!!");
        }
        return super.onStartCommand(intent, flags, startId);
    }
    private void displayNotification(String titel, String Text){
        Intent openIntent = new Intent(this, MainActivity.class);
        PendingIntent openPendingIntent = PendingIntent.getActivity(this,0,openIntent,0);
        NotificationCompat.Builder notification= new NotificationCompat.Builder(this)
                //Titel setzen
                .setContentTitle(titel)
                //Text setzen
                .setContentText(Text)
                //Icon setzen
                .setSmallIcon(R.drawable.ic_launcher_background)
                //Farbe wählen
                .setColor(getColor(R.color.colorAccent))
                .setVibrate(new long[]{0, 300,300,300})
                .setLights(Color.WHITE, 1000, 5000)
                .setContentIntent(openPendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Text));
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification.build());
    }
    private boolean checkDistance() {
        gibbLoc=new Location("Gibb");
        gibbLoc.setLatitude(GIBBLAT);
        gibbLoc.setLongitude(GIBBLONG);
        if (lastLocation!=null){
            distanceGibb=gibbLoc.distanceTo(lastLocation);
        }
        if (distanceGibb>0.0){
            if (distanceGibb<20){
                return true;
            }
        }

        else {
            return false;
        }
        return false;
    }
}
