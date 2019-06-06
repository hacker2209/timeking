package net.ictcampus.timeking;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;

public class AbsenzNotificationService extends Service {
    final int NOTIFICATION_ID = 16;
    public Location lastLocation;
    private LocationListener gibbList;
    LocationManager gibbMan;
    private Location gibbLoc;
    private double currentLong;
    private double currentLat;
    private static double GIBBLONG = 7.444840;
    private static double GIBBLAT = 46.954680;
    private float distanceGibb;
    boolean sendNot;


    private String title;
    private String content;

    public AbsenzNotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        gibbList = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Werte auslesen
                currentLong = location.getLongitude();
                currentLat = location.getLatitude();
                lastLocation = location;
                if (checkDistance() && sendNot == false) {
                    //Meldung die Ausgegeben wird
                    displayNotification("ALARM!!", "Du bist in der nähe der Gibb dreh dich um und renn weg!!");
                    sendNot = true;

                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }


        };
        gibbMan = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            stopSelf();
        }
        else {
            gibbMan.requestLocationUpdates(gibbMan.GPS_PROVIDER, 30000, 0, gibbList);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (gibbMan != null) {

            gibbMan.removeUpdates(gibbList);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Neuer LocationListener
        gibbList = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Werte auslesen
                currentLong = location.getLongitude();
                currentLat = location.getLatitude();
                lastLocation = location;
                if (checkDistance()) {
                    //Meldung die Ausgegeben wird
                    displayNotification("ALARM!!", "Du bist in der nähe der Gibb dreh dich um und renn weg!!");
                }
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

        return super.onStartCommand(intent, flags, startId);
    }
    private void displayNotification(String titel, String Text) {
        Intent openIntent = new Intent(this, MainActivity.class);
        PendingIntent openPendingIntent = PendingIntent.getActivity(this, 0, openIntent, 0);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                //Titel setzen
                .setContentTitle(titel)
                //Text setzen
                .setContentText(Text)
                //Icon setzen
                .setSmallIcon(R.drawable.ic_launcher_background)
                //Farbe wählen
                .setColor(getColor(R.color.colorAccent))
                .setVibrate(new long[]{0, 300, 300, 300})
                .setLights(Color.WHITE, 1000, 5000)
                .setContentIntent(openPendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Text));
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification.build());
    }

    private boolean checkDistance() {
        gibbLoc = new Location("Gibb");
        gibbLoc.setLatitude(GIBBLAT);
        gibbLoc.setLongitude(GIBBLONG);
        if (lastLocation != null) {
            distanceGibb = gibbLoc.distanceTo(lastLocation);
        }
        if (distanceGibb > 0.0) {
            if (distanceGibb < 200) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }




}
