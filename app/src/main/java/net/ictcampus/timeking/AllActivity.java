package net.ictcampus.timeking;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class AllActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomMenu);
        navigation.setOnNavigationItemSelectedListener(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addButton);
        fab.setOnClickListener(this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_All:
                startActivity(new Intent(AllActivity.this, AllActivity.class));
                return true;
            case R.id.action_Open:
                startActivity(new Intent(AllActivity.this, MainActivity.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(AllActivity.this, SetActivity.class));
                return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(AllActivity.this, AddActivity.class);
        startActivity(intent);
    }
}
