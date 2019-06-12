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
import android.widget.Toast;

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
        super.onCreate();
        db = new Database_SQLite(this);
        //registerListener();
    }

    private boolean checkAbsenz() {
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
        Toast.makeText(getApplicationContext(), "onDestroy()",
                Toast.LENGTH_SHORT).show();
        super.onDestroy();
        if (gibbMan != null) {
            gibbMan.removeUpdates(gibbList);
        }

    }

    public void registerListener() {
        //Neuer LocationListener
        Toast.makeText(getApplicationContext(), "Startcommand",
                Toast.LENGTH_SHORT).show();
        gibbList = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Werte auslesen
                Toast.makeText(getApplicationContext(), "Ort geändert",
                        Toast.LENGTH_SHORT).show();
                currentLong = location.getLongitude();
                currentLat = location.getLatitude();
                lastLocation = location;
                if (checkDistance() && checkAbsenz()) {
                    //Meldung die Ausgegeben wird
                    displayNotification("Achtung", "Du hast noch Absenzen offen!!!");
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            displayNotification("ALARM!!", "Ich habe keine rechte!!");
        } else {
            gibbMan = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            gibbMan.requestLocationUpdates(gibbMan.GPS_PROVIDER, 30000, 0, gibbList);


        }

        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        registerListener();
        return super.onStartCommand(intent, flags, startId);
    }

    public void displayNotification(String titel, String Text) {
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

    //Prüft die Distanz
    private boolean checkDistance() {
        //Neue Location
        gibbLoc = new Location("Gibb");
        //Breiten und Längengraden
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
