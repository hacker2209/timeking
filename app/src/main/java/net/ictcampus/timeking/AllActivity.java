package net.ictcampus.timeking;

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

public class AllActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    BottomNavigationView bottomNavigationView;
    Database_SQLite db;
    TextView datumText;
    TextView fachText;
    TableRow newAbs;
    TableLayout all;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomMenu);
        navigation.setOnNavigationItemSelectedListener(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addButton);
        all = (TableLayout) findViewById(R.id.all);
        fab.setOnClickListener(this);
        db = new Database_SQLite(this);
        Cursor cursor = db.get_Table_All();
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
                datumText = new TextView(this);
                newAbs = new TableRow(this);
                fachText.setTextSize(20);
                datumText.setTextSize(20);
                fachText.setText(sTitle);
                datumText.setText(sDate);
                newAbs.addView(fachText);
                newAbs.addView(datumText);
                newAbs.setPadding(5,5,5,5);
                all.addView(newAbs);
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
