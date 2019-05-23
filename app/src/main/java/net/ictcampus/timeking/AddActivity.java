package net.ictcampus.timeking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Date;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button;
    private Window w;
    private EditText fachIn;
    private DatePicker dateIn;
    private TimePicker timeIn;
    private String fach;
    String date;
    private String time;
    private TextView test;

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
    }
}
