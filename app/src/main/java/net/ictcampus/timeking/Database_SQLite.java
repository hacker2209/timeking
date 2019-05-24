package net.ictcampus.timeking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class Database_SQLite extends SQLiteOpenHelper {
    private static  final String DatabaseName= "timeking.db";
    public  Database_SQLite(Context context){super(context,DatabaseName,null,1);}
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE tbl_Absenz " +
                "(  IDAbsenz INTEGER PRIMARY KEY AUTOINCREMENT " +
                " , Fach TEXT " +      //*Note of type TEXT
                " , Datum DATETIME " +       //*Note of type TEXT
                ")"
                ;
        db.execSQL(createTable);
    }
    public long add_Note(String sFach,Date dtNoteDay) {
        //--------< add_Note() >--------
        //*add a recordset to the tbl_Notes Table
        //< getDB >
        SQLiteDatabase db = this.getWritableDatabase();
        //</ getDB >

        //< set_Values >
        SimpleDateFormat fmtDate = new SimpleDateFormat("dd.MM.YY");
        String sDate_Note = fmtDate.format(dtNoteDay);


        ContentValues values = new ContentValues();
        values.put("Fach", sFach);
        values.put("Datum", sDate_Note);
        //</ set_Values >

        //< add >
        long newID = db.insert("tbl_Absenz", null, values);
        //</ add >

        //< out >
        if (newID == -1)
        {
            //*SQLite Error on Insert: -1
            return -1;
        }
        else
        {
            //*return value is new ID
            return newID;
        }
        //</ out >
        //--------</ add_Note() >--------
    }
    public Cursor get_Table(){
        //--------< get_Table() >--------
        //
        SQLiteDatabase db = this.getWritableDatabase();
        String sSQL = "SELECT * FROM tbl_Absenz";
        Cursor data = db.rawQuery(sSQL, null);
        return data;
        //--------</ get_Table() >--------
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
