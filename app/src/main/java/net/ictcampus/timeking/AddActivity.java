package net.ictcampus.timeking;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Date;

public class AddActivity extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    Database_SQLite db;
    private Button button;
    private BottomNavigationView navi;
    private Window w;
    private EditText fachIn;
    private DatePicker dateIn;
    private TimePicker timeIn;
    private String fach;
    String date;
    private String time;
    private TextView test;
    private TextView fachText;
    private TextView dateText;
    TableLayout absenzen;
    TableRow newAbsenz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        w = getWindow();
        button = (Button) findViewById(R.id.btnCreate);
        fachIn = (EditText) findViewById(R.id.fach);
        dateIn = (DatePicker) findViewById(R.id.date);
        timeIn = (TimePicker) findViewById(R.id.time);
        test = (TextView) findViewById(R.id.test);
        absenzen = (TableLayout) findViewById(R.id.tableLayout1);
        dateText= new TextView(this);
        fachText= new TextView(this);
        newAbsenz= new TableRow(this);
        navi = (BottomNavigationView) findViewById(R.id.bottomMenu);
        navi.setOnNavigationItemSelectedListener(this);
        db=new Database_SQLite(this);
        button.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YY");
        fach = fachIn.getText().toString();
        int day = dateIn.getDayOfMonth();
        int month = dateIn.getMonth();
        int year = dateIn.getYear();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        int hour = timeIn.getHour();
        int minute = timeIn.getMinute();
        date = year+"-"+(month+1)+"-"+day;
       db.add_Note(fach, Date.valueOf(date));
    //    startActivity(new Intent(AddActivity.this, MainActivity.class));


    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_All) {
            startActivity(new Intent(AddActivity.this, AllActivity.class));
            return true;
        }
        else if(item.getItemId()==R.id.action_Open){
            startActivity(new Intent(AddActivity.this, MainActivity.class));
            return true;
        }
        else if(item.getItemId()==R.id.action_settings){
            startActivity(new Intent(AddActivity.this, SetActivity.class));
            return true;
        }
        return false;
    }
}
