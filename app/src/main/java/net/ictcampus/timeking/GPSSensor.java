package net.ictcampus.timeking;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class GPSSensor extends AppCompatActivity{
    public Location lastLocation;
    private LocationListener ll;
    private Location gibbLoc;
    private double currentLong;
    private double currentLat;
    private static double GIBBLONG= 7.444840;
    private static double GIBBLAT=  46.954680;
    private float distanceGibb;



    public void startLoc(){

        ll= new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLong=location.getLongitude();
                currentLat=location.getLatitude();
                lastLocation=location;
                checkDistance();
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
    }
private void checkDistance() {
        gibbLoc=new Location("Gibb");
        gibbLoc.setLatitude(GIBBLAT);
        gibbLoc.setLongitude(GIBBLONG);
        distanceGibb=gibbLoc.distanceTo(lastLocation);
        if (distanceGibb<20){

        }
    }

}
