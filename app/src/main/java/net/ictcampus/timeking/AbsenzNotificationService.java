package net.ictcampus.timeking;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;

import java.util.Date;

public class AbsenzNotificationService extends Service {
    final int NOTIFICATION_ID = 16;
    Database_SQLite db;
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
    Activity activity;


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
        db = new Database_SQLite(this);
        super.onCreate();
        gibbList = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Werte auslesen
                currentLong = location.getLongitude();
                currentLat = location.getLatitude();
                lastLocation = location;
                if (checkDistance()&&checkAbsenz()&&!sendNot) {
                    //Meldung die Ausgegeben wird
                    displayNotification("Achtung", "Du hast noch Absenzen offen!!");
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
            displayNotification("ALARM!!", "Ich habe keine rechte!!");
        } else {
            gibbMan.requestLocationUpdates(gibbMan.GPS_PROVIDER, 30000, 0, gibbList);
        }
        stopSelf();


    }
private boolean checkAbsenz(){
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YY");
    String currentDate = sdf.format(new Date());
    Cursor openDat = db.get_Table_Open();
    int colDate = openDat.getColumnIndex("Datum");
    if (openDat.getCount() > 0) {
        while (openDat.moveToNext()) {
            String absenzDate = openDat.getString(colDate);
            if (absenzDate.equals(currentDate)) {
                return true;
            }
        }
    }
    return false;
}
    @Override
    public void onDestroy() {
        db.close();
        super.onDestroy();
        if (gibbMan != null) {
            gibbMan.removeUpdates(gibbList);
        }
        gibbMan.removeUpdates(gibbList);
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
                if (checkDistance()&&checkAbsenz()&&!sendNot) {
                    //Meldung die Ausgegeben wird
                        displayNotification("Achtung", "Du hast noch Absenzen offen!!!");
                        sendNot=true;


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
                startActivity(i);

            }


        };
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    private void displayNotification(String titel, String Text) {
        Intent openIntent = new Intent(this, MainActivity.class);
        PendingIntent openPendingIntent = PendingIntent.getActivity(this, 0, openIntent, 0);
       Uri absenzSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                //Titel setzen
                .setContentTitle(titel)
                //Text setzen
                .setContentText(Text)
                //Icon setzen
                .setSmallIcon(R.drawable.ic_logo)
                //Farbe wählen
                .setColor(getColor(R.color.colorAccent))
                .setVibrate(new long[]{0, 300, 300, 300})
                .setLights(Color.WHITE, 1000, 5000)
                .setSound(absenzSound)
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
