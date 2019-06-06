package net.ictcampus.timeking;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.hardware.SensorEventListener;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Calendar;

public class SetActivity extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_All) {
            startActivity(new Intent(SetActivity.this, AllActivity.class));
            return true;
        } else if (item.getItemId() == R.id.action_Open) {
            startActivity(new Intent(SetActivity.this, MainActivity.class));
            return true;
        } else if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(SetActivity.this, SetActivity.class));
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {

    }

}
