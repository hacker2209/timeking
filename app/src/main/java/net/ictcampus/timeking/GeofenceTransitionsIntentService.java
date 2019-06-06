package net.ictcampus.timeking;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;


public class GeofenceTransitionsIntentService extends Service {
    private static final String GEOFENCE_REQ_ID = "";
    private GeofencingClient geofencingClient;
    private Object Duration;


    @Override
    public void onCreate() {
        super.onCreate();
        Geofence geofence = new Geofence.Builder()
                .setRequestId(GEOFENCE_REQ_ID)
                .setCircularRegion( 46.954680,7.444840,20)
                .setExpirationDuration((Long) Duration)
                .build();
        GeofencingRequest request = new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
