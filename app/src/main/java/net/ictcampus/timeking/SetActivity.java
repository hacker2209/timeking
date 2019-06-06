package net.ictcampus.timeking;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SetActivity extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavi;
    Database_SQLite db;
    EditText nameChange;
    Button btnMo, btnDi, btnMi, btnDo, btnFr, btnFinish;
    CheckBox checkMo, checkDi, checkMi, checkDo, checkFr, checkMid, checkMor, checkEve;
    List<Integer> weckerZeit, weckerTage, schulTage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        db = new Database_SQLite(this);
        initElements();

    }

    private void initElements() {
        weckerZeit = new ArrayList<>();
        weckerTage = new ArrayList<>();
        schulTage = new ArrayList<>();
        bottomNavi = (BottomNavigationView) findViewById(R.id.bottomMenu);
        btnFinish = (Button) findViewById(R.id.anwendenSet);
        btnMo = (Button) findViewById(R.id.moSet);
        btnDi = (Button) findViewById(R.id.diSet);
        btnMi = (Button) findViewById(R.id.miSet);
        btnDo = (Button) findViewById(R.id.doSet);
        btnFr = (Button) findViewById(R.id.frSet);
        checkMo = (CheckBox) findViewById(R.id.moCheck);
        checkDi = (CheckBox) findViewById(R.id.diCheck);
        checkMi = (CheckBox) findViewById(R.id.miCheck);
        checkDo = (CheckBox) findViewById(R.id.doCheck);
        checkFr = (CheckBox) findViewById(R.id.frCheck);
        checkMor = (CheckBox) findViewById(R.id.morCheck);
        checkMid = (CheckBox) findViewById(R.id.midCheck);
        checkEve = (CheckBox) findViewById(R.id.eveCheck);
        nameChange = (EditText) findViewById(R.id.nameChange);
        bottomNavi.setOnNavigationItemSelectedListener(this);
        btnMo.setOnClickListener(this);
        btnDi.setOnClickListener(this);
        btnMi.setOnClickListener(this);
        btnDo.setOnClickListener(this);
        btnFr.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        checkMo.setOnClickListener(this);
        checkDi.setOnClickListener(this);
        checkMi.setOnClickListener(this);
        checkDo.setOnClickListener(this);
        checkFr.setOnClickListener(this);
        checkMor.setOnClickListener(this);
        checkMid.setOnClickListener(this);
        checkEve.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.close();
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
    protected void onResume() {
        super.onResume();
        if (weckerTage.size() > 0 && weckerZeit.size() > 0 && schulTage.size() > 0) {
            schulTage.clear();
            weckerTage.clear();
            weckerZeit.clear();
        }
        initSchultage();
        setIDtoTag();
        setChecked();
        setButtonColored();
    }

    private void setButtonColored() {
        Cursor schulDat = db.get_Table_Schultage();
        int colDay = schulDat.getColumnIndex("TagID");
        while (schulDat.moveToNext()){
            int tid = schulDat.getInt(colDay);
            switch (tid){
                case 1:
                    btnMo.setBackgroundResource(R.color.colorAccent);
                    break;
                case 2:
                    btnDi.setBackgroundResource(R.color.colorAccent);
                    break;
                case 3:
                    btnMi.setBackgroundResource(R.color.colorAccent);
                    break;
                case 4:
                    btnDo.setBackgroundResource(R.color.colorAccent);
                    break;
                case 5:
                    btnFr.setBackgroundResource(R.color.colorAccent);
                    break;
            }
        }
    }

    private void initSchultage() {
        Cursor schultagDat = db.get_Table_Schultage();
        int cTID = schultagDat.getColumnIndex("TagID");
        while (schultagDat.moveToNext()) {
            schulTage.add(schultagDat.getInt(cTID));
        }
    }

    @Override
    public void onClick(View v) {
        int clickedID = v.getId();
        switch (clickedID) {
            case R.id.diSet:
                if (schulTage.contains(v.getTag())) {
                    db.deleteSchultag((Integer) v.getTag());
                    Toast.makeText(getApplicationContext(), "Schultag entfernt",
                            Toast.LENGTH_SHORT).show();
                } else {
                    v.setBackgroundResource(R.color.colorAccent);
                    db.add_Schultag((Integer) v.getTag());
                    Toast.makeText(getApplicationContext(), "Schultag hinzugefügt",
                            Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.moSet:
                if (schulTage.contains(v.getTag())) {
                    db.deleteSchultag((Integer) v.getTag());
                    Toast.makeText(getApplicationContext(), "Schultag entfernt",
                            Toast.LENGTH_SHORT).show();
                } else {
                    v.setBackgroundResource(R.color.colorAccent);
                    db.add_Schultag((Integer) v.getTag());
                    Toast.makeText(getApplicationContext(), "Schultag hinzugefügt",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.miSet:
                if (schulTage.contains(v.getTag())) {
                    db.deleteSchultag((Integer) v.getTag());
                    Toast.makeText(getApplicationContext(), "Schultag entfernt",
                            Toast.LENGTH_SHORT).show();
                } else {
                    v.setBackgroundResource(R.color.colorAccent);
                    db.add_Schultag((Integer) v.getTag());
                    Toast.makeText(getApplicationContext(), "Schultag hinzugefügt",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.doSet:
                if (schulTage.contains(v.getTag())) {
                    db.deleteSchultag((Integer) v.getTag());
                    Toast.makeText(getApplicationContext(), "Schultag entfernt",
                            Toast.LENGTH_SHORT).show();
                } else {
                    v.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
                    db.add_Schultag((Integer) v.getTag());
                    Toast.makeText(getApplicationContext(), "Schultag hinzugefügt",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.frSet:
                if (schulTage.contains(v.getTag())) {
                    db.deleteSchultag((Integer) v.getTag());
                    Toast.makeText(getApplicationContext(), "Schultag entfernt",
                            Toast.LENGTH_SHORT).show();
                } else {
                    v.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
                    db.add_Schultag((Integer) v.getTag());
                    Toast.makeText(getApplicationContext(), "Schultag hinzugefügt",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.midCheck:
                weckerZeit.add((Integer) v.getTag());
                break;
            case R.id.morCheck:
                weckerZeit.add((Integer) v.getTag());
                break;
            case R.id.eveCheck:
                weckerZeit.add((Integer) v.getTag());
                break;
            case R.id.moCheck:
                weckerTage.add((Integer) v.getTag());
                break;
            case R.id.diCheck:
                weckerTage.add((Integer) v.getTag());
                break;
            case R.id.miCheck:
                weckerTage.add((Integer) v.getTag());
                break;
            case R.id.doCheck:
                weckerTage.add((Integer) v.getTag());
                break;
            case R.id.frCheck:
                weckerTage.add((Integer) v.getTag());
                break;
            case R.id.anwendenSet:
                if (nameChange.getText().toString().trim().length() > 0) {
                    db.update_name(nameChange.getText().toString());
                }
                int zid = 0;
                int tid = 0;
                for (int i = 0; i < weckerZeit.size(); i++) {
                    if (weckerZeit.get(i) != null) {
                        zid = weckerZeit.get(i);
                    }
                    for (int x = 0; x < weckerTage.size(); x++) {
                        db.clearWecker();
                        if (weckerTage.get(x) != null) {
                            tid = weckerTage.get(x);
                        }
                        if (tid > 0 && zid > 0) {
                            db.add_Wecker(tid, zid);
                        }

                    }
                }
                Toast.makeText(getApplicationContext(), "Erfolgreich",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SetActivity.this, MainActivity.class));
                break;


        }
    }

    public void setChecked() {
        Cursor weckerDat = db.get_Table_Wecker();
        Cursor nameDat = db.get_Table_Name();
        int cNid = nameDat.getColumnIndex("Name");
        int cZid = weckerDat.getColumnIndex("TageszeitID");
        int cTid = weckerDat.getColumnIndex("TagID");
        while (weckerDat.moveToNext()) {
            int tagID = weckerDat.getInt(cTid);
            int zeitID = weckerDat.getInt(cZid);
            switch (tagID) {
                case 1:
                    checkMo.setChecked(true);
                    break;
                case 2:
                    checkDi.setChecked(true);
                    break;
                case 3:
                    checkMi.setChecked(true);
                    break;
                case 4:
                    checkDo.setChecked(true);
                    break;
                case 5:
                    checkFr.setChecked(true);
                    break;
            }
            switch (zeitID) {
                case 1:
                    checkMor.setChecked(true);
                    break;
                case 2:
                    checkMid.setChecked(true);
                    break;
                case 3:
                    checkEve.setChecked(true);
                    break;
            }
        }

        while (nameDat.moveToNext()) {
            nameChange.setHint(nameDat.getString(cNid) + "");
        }
    }

    public void setIDtoTag() {
        Cursor cursorDay = db.get_Table_Tage();
        Cursor cursorTime = db.get_Table_Zeit();
        int idDay = cursorDay.getColumnIndex("ID");
        int idTime = cursorTime.getColumnIndex("ID");
        if (cursorDay.getCount() > 0) {
            while (cursorDay.moveToNext()) {
                int id = cursorDay.getInt(idDay);
                switch (id) {
                    case 1:
                        btnMo.setTag(id);
                        checkMo.setTag(id);
                        break;
                    case 2:
                        btnDi.setTag(id);
                        checkDi.setTag(id);
                        break;
                    case 3:
                        btnMi.setTag(id);
                        checkDi.setTag(id);
                        break;
                    case 4:
                        btnDo.setTag(id);
                        checkDo.setTag(id);
                        break;
                    case 5:
                        btnFr.setTag(id);
                        checkFr.setTag(id);
                        break;
                }
            }
        }
        if (cursorTime.getCount() > 0) {
            while (cursorTime.moveToNext()) {
                int id = cursorTime.getInt(idTime);
                switch (id) {
                    case 1:
                        checkMor.setTag(id);
                        break;
                    case 2:
                        checkMid.setTag(id);
                        break;
                    case 3:
                        checkEve.setTag(id);
                        break;
                }
            }
        }
    }
}


