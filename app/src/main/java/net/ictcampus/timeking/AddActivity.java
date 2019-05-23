package net.ictcampus.timeking;

import android.content.Intent;
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

import java.sql.Time;
import java.util.Date;

public class AddActivity extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
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
        fach = fachIn.getText().toString();
        int day = dateIn.getDayOfMonth();
        int month = dateIn.getMonth();
        int year = dateIn.getYear();
        date = day + "." + month + "." + year;
        int hour = timeIn.getHour();
        int minute = timeIn.getMinute();
        time = hour + ":" + minute;
        test.setText(fach+time+date);
        dateText.setText(date);
        fachText.setText(fach);
        newAbsenz.addView(fachText);
        newAbsenz.addView(dateText);
        absenzen.addView(newAbsenz);

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
