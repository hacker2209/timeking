package net.ictcampus.timeking;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.sql.Date;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    Database_SQLite db;
    TableLayout open;
    TextView fachText;
    TextView datumText;
    CheckBox betriebCheck;
    CheckBox lehrerCheck;
    //</ create data as dataclass >
    TableRow newAbs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<DataModel_Absenz> data_with_Notes = new ArrayList<DataModel_Absenz>();
        open = (TableLayout) findViewById(R.id.open);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomMenu);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addButton);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddActivity.class));
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        fab.setOnClickListener(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        db = new Database_SQLite(this);
        Cursor cursor = db.get_Table();
        int colID = cursor.getColumnIndex("IDAbsenz");
        int colFach = cursor.getColumnIndex("Fach");
        int colDate = cursor.getColumnIndex("Datum");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                //< get Data from data_cursor >
                String sTitle = cursor.getString(colFach);
                String sDate = cursor.getString(colDate);
                //</ get Data from data_cursor >


                fachText=new TextView(this);
                datumText=new TextView(this);
                newAbs=new TableRow(this);
                betriebCheck=new CheckBox(this);
                lehrerCheck=new CheckBox(this);
                fachText.setText(sTitle);
                datumText.setText(sDate);
                newAbs.addView(fachText);
                newAbs.addView(datumText);
                newAbs.addView(betriebCheck);
                newAbs.addView(lehrerCheck);
                open.addView(newAbs);
                //< create data as dataclass >
                DataModel_Absenz note = new DataModel_Absenz();
                note.Fach = sTitle;
                //       note.Datum= Date.valueOf(sDate);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(MainActivity.this, AddActivity.class));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_All) {
            startActivity(new Intent(MainActivity.this, AllActivity.class));
            return true;
        } else if (item.getItemId() == R.id.action_Open) {
            startActivity(new Intent(MainActivity.this, MainActivity.class));
            return true;
        } else if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SetActivity.class));
            return true;
        }
        return false;
    }
}
