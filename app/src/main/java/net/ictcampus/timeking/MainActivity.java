package net.ictcampus.timeking;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    Database_SQLite db;
    TableLayout open;
    TextView fachText;
    TextView datumText;
    CheckBox betriebCheck;
    CheckBox lehrerCheck;
    TableRow newAbs;
    List<Integer> weckerZeit, weckerTag;
    Calendar alarmStartTime;
    LocationManager locationManager;
    double latitude;
    double longitude;
    Notification notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = new Database_SQLite(this);
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
        if (checkAbsenz()&&checkSchultag()){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            } else {
                Intent startLocIntent = new Intent(this, AbsenzNotificationService.class);
                this.startService(startLocIntent);
            }
        }



        //Wecker benachrichtigung
        //neuer AlarmManager
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        System.out.println("-------------------------------------------------------------------------------------------------------");
        System.out.println(Uri.parse("custom://" + System.currentTimeMillis()));
        alarmIntent.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        alarmManager.cancel(pendingIntent);

        alarmStartTime = Calendar.getInstance();
        Calendar now = Calendar.getInstance();

        if (now.after(alarmStartTime)) {
            Log.d("Hey", "Added a day");
            alarmStartTime.add(Calendar.DATE, 1);
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        Log.d("Alarm", "Alarms set for everyday 8 am.");


    }

    private boolean checkSchultag() {
        int todayID=0;
        Cursor schulDat=db.get_Table_Schultage();
        int colTid= schulDat.getColumnIndex("TagID");
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.MONDAY:
                todayID = 1;
                break;
            case Calendar.TUESDAY:
                todayID = 2;
                break;
            case Calendar.WEDNESDAY:
                todayID = 3;
                break;
            case Calendar.THURSDAY:
                todayID = 4;
                break;
            case Calendar.FRIDAY:
                todayID = 5;
                break;
        }
    while (schulDat.moveToNext()){
        int tagID=schulDat.getInt(colTid);
        if (tagID==todayID){
            return true;
        }
    }
        return false;
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
        } else {
            return false;
        }
        return false;
    }

    @Override
    protected void onResume() {
        if (open.getChildCount() > 0) {
            open.removeViewsInLayout(1, open.getChildCount() - 1);
        }
        super.onResume();
        takeData();
        weckerTag = new ArrayList<Integer>();
        weckerZeit = new ArrayList<Integer>();

        if (weckerTag.size() > 0 | weckerZeit.size() > 0) {
            weckerZeit.clear();
            weckerTag.clear();
        }
        setIDtoTag();
    }

    public void setIDtoTag() {
        Cursor cursorOpenStuff = db.get_Table_Open();
        Cursor cursorDay = db.get_Table_Wecker();
        Cursor cursorTime = db.get_Table_Wecker();
        int idDay = cursorDay.getColumnIndex("TageszeitID");
        int idTime = cursorTime.getColumnIndex("TagID");
        if (cursorOpenStuff.getCount() > 0) {
            if (cursorDay.getCount() > 0) {
                while (cursorDay.moveToNext() && cursorTime.moveToNext()) {
                    int day = cursorDay.getInt(idDay);
                    int time = cursorTime.getInt(idTime);
                    switch (day) {
                        case 1:
                            if (time == 1) {
                                alarmStartTime.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                                alarmStartTime.set(Calendar.HOUR_OF_DAY, 7);
                                alarmStartTime.set(Calendar.MINUTE, 30);
                                alarmStartTime.set(Calendar.SECOND, 0);
                            } else if (time == 2) {
                                alarmStartTime.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                                alarmStartTime.set(Calendar.HOUR_OF_DAY, 12);
                                alarmStartTime.set(Calendar.MINUTE, 0);
                                alarmStartTime.set(Calendar.SECOND, 0);
                            } else if (time == 3) {
                                alarmStartTime.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                                alarmStartTime.set(Calendar.HOUR_OF_DAY, 18);
                                alarmStartTime.set(Calendar.MINUTE, 0);
                                alarmStartTime.set(Calendar.SECOND, 0);
                            }
                            break;

                        case 2:
                            if (time == 1) {
                                alarmStartTime.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                                alarmStartTime.set(Calendar.HOUR_OF_DAY, 7);
                                alarmStartTime.set(Calendar.MINUTE, 30);
                                alarmStartTime.set(Calendar.SECOND, 0);
                            } else if (time == 2) {
                                alarmStartTime.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                                alarmStartTime.set(Calendar.HOUR_OF_DAY, 12);
                                alarmStartTime.set(Calendar.MINUTE, 0);
                                alarmStartTime.set(Calendar.SECOND, 0);
                            } else if (time == 3) {
                                alarmStartTime.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                                alarmStartTime.set(Calendar.HOUR_OF_DAY, 18);
                                alarmStartTime.set(Calendar.MINUTE, 0);
                                alarmStartTime.set(Calendar.SECOND, 0);
                            }
                            break;
                        case 3:
                            if (time == 1) {
                                alarmStartTime.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                                alarmStartTime.set(Calendar.HOUR_OF_DAY, 7);
                                alarmStartTime.set(Calendar.MINUTE, 30);
                                alarmStartTime.set(Calendar.SECOND, 0);
                            } else if (time == 2) {
                                alarmStartTime.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                                alarmStartTime.set(Calendar.HOUR_OF_DAY, 12);
                                alarmStartTime.set(Calendar.MINUTE, 0);
                                alarmStartTime.set(Calendar.SECOND, 0);
                            } else if (time == 3) {
                                alarmStartTime.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                                alarmStartTime.set(Calendar.HOUR_OF_DAY, 18);
                                alarmStartTime.set(Calendar.MINUTE, 0);
                                alarmStartTime.set(Calendar.SECOND, 0);
                            }
                            break;
                        case 4:
                            if (time == 1) {
                                alarmStartTime.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                                alarmStartTime.set(Calendar.HOUR_OF_DAY, 7);
                                alarmStartTime.set(Calendar.MINUTE, 30);
                                alarmStartTime.set(Calendar.SECOND, 0);
                            } else if (time == 2) {
                                alarmStartTime.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                                alarmStartTime.set(Calendar.HOUR_OF_DAY, 12);
                                alarmStartTime.set(Calendar.MINUTE, 0);
                                alarmStartTime.set(Calendar.SECOND, 0);
                            } else if (time == 3) {
                                alarmStartTime.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                                alarmStartTime.set(Calendar.HOUR_OF_DAY, 18);
                                alarmStartTime.set(Calendar.MINUTE, 0);
                                alarmStartTime.set(Calendar.SECOND, 0);
                            }
                            break;
                        case 5:
                            if (time == 1) {
                                alarmStartTime.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                                alarmStartTime.set(Calendar.HOUR_OF_DAY, 7);
                                alarmStartTime.set(Calendar.MINUTE, 30);
                                alarmStartTime.set(Calendar.SECOND, 0);
                            } else if (time == 2) {
                                alarmStartTime.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                                alarmStartTime.set(Calendar.HOUR_OF_DAY, 12);
                                alarmStartTime.set(Calendar.MINUTE, 0);
                                alarmStartTime.set(Calendar.SECOND, 0);
                            } else if (time == 3) {
                                alarmStartTime.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                                alarmStartTime.set(Calendar.HOUR_OF_DAY, 18);
                                alarmStartTime.set(Calendar.MINUTE, 0);
                                alarmStartTime.set(Calendar.SECOND, 0);
                            }
                            break;
                    }
                }
            }
        }
    }


    private void welcomeUser() {
        Cursor nameDat = db.get_Table_Name();
        int cName = nameDat.getColumnIndex("Name");
        while (nameDat.moveToNext()) {
            String name = nameDat.getString(cName);
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


    private void takeData() {
        Cursor cursor = db.get_Table_Open();
        int colID = cursor.getColumnIndex("ID");
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
                boolean leh = Boolean.parseBoolean(cursor.getString(colLehrer));
                boolean bet = Boolean.parseBoolean(cursor.getString(colBetrieb));
                //</ get Data from data_cursor >


                fachText = new TextView(this);
                datumText = new TextView(this);
                newAbs = new TableRow(this);
                betriebCheck = new CheckBox(this);
                betriebCheck.setTag(sID);
                lehrerCheck = new CheckBox(this);
                lehrerCheck.setTag(sID);
                fachText.setText(sTitle);
                if (leh) {
                    lehrerCheck.setChecked(true);
                }
                if (bet) {
                    betriebCheck.setChecked(true);
                }
                datumText.setText(sDate);
                fachText.setTextSize(20);
                datumText.setTextSize(20);
                newAbs.addView(fachText);
                newAbs.addView(datumText);
                newAbs.addView(betriebCheck);
                newAbs.addView(lehrerCheck);
                newAbs.setPadding(5, 5, 5, 5);

                // String date = new SimpleDateFormat("dd.MM.YY", Locale.getDefault()).format(new Date());
                //< create data as dataclass >
                DataModel_Absenz note = new DataModel_Absenz();
                note.Fach = sTitle;
                lehrerCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            db.update_status_lehrer(Integer.parseInt(buttonView.getTag().toString()), "True");
                        } else {
                            db.update_status_lehrer(Integer.parseInt(buttonView.getTag().toString()), "False");
                        }
                    }
                });
                betriebCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            db.update_status_betrieb(Integer.parseInt(buttonView.getTag().toString()), "True");
                        } else {
                            db.update_status_betrieb(Integer.parseInt(buttonView.getTag().toString()), "False");
                        }
                    }
                });
                open.addView(newAbs);
            }
        } else {
            fachText = new TextView(this);
            newAbs = new TableRow(this);
            fachText.setText("keine offenen Absenzen");
            newAbs.addView(fachText);
            open.addView(newAbs);

        }
    }
}
