package net.ictcampus.timeking;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    Database_SQLite db;
    TableLayout open;
    TextView fachText;
    TextView datumText;
    CheckBox betriebCheck;
    CheckBox lehrerCheck;
    TableRow newAbs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = new Database_SQLite(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AlarmManager locListen = (AlarmManager) MainActivity.this.getSystemService(MainActivity.this.ALARM_SERVICE);
        Intent startLoc = new Intent(MainActivity.this,AbsenzNotificationService.class);
        PendingIntent startLocPending= PendingIntent.getService(MainActivity.this,0,startLoc,0);
        locListen.setRepeating(AlarmManager.RTC_WAKEUP,0,1000*60*5, startLocPending);
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
        if (open.getChildCount()>0){
            open.removeViewsInLayout(1, open.getChildCount()-1);
        }

        super.onResume();
        takeData();


    }
    private void welcomeUser(){
        Cursor nameDat= db.get_Table_Name();
        int cName= nameDat.getColumnIndex("Name");
        while (nameDat.moveToNext()){
            String name= nameDat.getString(cName);
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        db.close();
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


    private void takeData(){
        Cursor cursor = db.get_Table_Open();
        int  colID= cursor.getColumnIndex("ID");
        int colFach = cursor.getColumnIndex("Fach");
        int colDate = cursor.getColumnIndex("Datum");
        int colLehrer = cursor.getColumnIndex("Lehrer");
        int colBetrieb = cursor.getColumnIndex("Betrieb");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                //< get Data from data_cursor >
                int sID = cursor.getInt(colID);
                String sTitle = cursor.getString(colFach);
                String sDate = cursor.getString(colDate);
                boolean leh= Boolean.parseBoolean(cursor.getString(colLehrer));
                boolean bet= Boolean.parseBoolean(cursor.getString(colBetrieb));
                //</ get Data from data_cursor >


                fachText=new TextView(this);
                datumText=new TextView(this);
                newAbs=new TableRow(this);
                betriebCheck=new CheckBox(this);
                betriebCheck.setTag(sID);
                lehrerCheck=new CheckBox(this);
                 lehrerCheck.setTag(sID);
                fachText.setText(sTitle);
                if (leh){
                    lehrerCheck.setChecked(true);
                }
                if(bet){
                    betriebCheck.setChecked(true);
                }
                datumText.setText(sDate);
                fachText.setTextSize(20);
                datumText.setTextSize(20);
                newAbs.addView(fachText);
                newAbs.addView(datumText);
                newAbs.addView(betriebCheck);
                newAbs.addView(lehrerCheck);
                newAbs.setPadding(5,5,5,5);

                // String date = new SimpleDateFormat("dd.MM.YY", Locale.getDefault()).format(new Date());
                //< create data as dataclass >
                DataModel_Absenz note = new DataModel_Absenz();
                note.Fach = sTitle;
                lehrerCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            db.update_status_lehrer(Integer.parseInt(buttonView.getTag().toString()),"True");
                        }
                        else {
                            db.update_status_lehrer(Integer.parseInt(buttonView.getTag().toString()),"False");
                        }
                    }
                });
                betriebCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            db.update_status_betrieb(Integer.parseInt(buttonView.getTag().toString()),"True");
                        }
                        else {
                            db.update_status_betrieb(Integer.parseInt(buttonView.getTag().toString()),"False");
                        }
                    }
                });
                open.addView(newAbs);
            }
        }
        else {
            fachText= new TextView(this);
            newAbs=new TableRow(this);
            fachText.setText("keine offenen Absenzen");
            newAbs.addView(fachText);
            open.addView(newAbs);

        }
    }
}
