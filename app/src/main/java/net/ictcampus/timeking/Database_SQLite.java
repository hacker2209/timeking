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

        String createTableAbsenz = "CREATE TABLE tbl_Absenz " +
                "(  ID INTEGER PRIMARY KEY AUTOINCREMENT " +
                " , Fach TEXT " +      //*Note of type TEXT
                " , Datum DATETIME " +       //*Note of type TEXT
                " , Betrieb  TEXT DEFAULT 'False' " +
                " , Lehrer  TEXT DEFAULT 'False' " +
                ")"
                ;
        String creatTableZeitenWecker = "CREATE TABLE zeiten_Wecker"+
                "( ID INTEGER PRIMARY KEY AUTOINCREMENT"+
                ", Tageszeit TEXT"+
                ")"
                ;
        String creatTableTageWecker = "CREATE TABLE Tage_Wecker"+
                "( ID INTEGER PRIMARY KEY AUTOINCREMENT"+
                ", Tag TEXT"+
                ")"
                ;
        String creatTableWecker = "CREATE TABLE Wecker"+
                "( ID INTEGER PRIMARY KEY AUTOINCREMENT"+
                ", TageszeitID INTEGER"+
                ",TagID INTEGER"+
                ",FOREIGN KEY(TageszeitID) REFERENCES zeiten_Wecker(ID)"+
                ")"
                ;

        String creatTableName = "CREATE TABLE Name"+
                "( ID INTEGER PRIMARY KEY AUTOINCREMENT"+
                ", Name TEXT"+
                ")"
                ;

        //Insert befehle für die Tageszeit und den Tag
        String inserstTageszeitMO = "INSERT INTO zeiten_Wecker (ID, Tageszeit) VALUES ( 1, 'Morgen')";
        String inserstTageszeitMI = "INSERT INTO zeiten_Wecker (ID, Tageszeit) VALUES ( 2,'Mittag')";
        String inserstTageszeitAB = "INSERT INTO zeiten_Wecker (ID, Tageszeit) VALUES ( 3, 'Abend')";
        String insertTageMO = "INSERT INTO Tage_Wecker (ID, Tag) VALUES (1, 'MO')";
        String insertTageDI = "INSERT INTO Tage_Wecker (ID, Tag) VALUES (2, 'DI')";
        String insertTageMI = "INSERT INTO Tage_Wecker (ID, Tag) VALUES (3, 'MI')";
        String insertTageDO = "INSERT INTO Tage_Wecker (ID, Tag) VALUES (4, 'DO')";
        String insertTageFR = "INSERT INTO Tage_Wecker (ID, Tag) VALUES (5, 'FR')";


        //Erstellt die Datenbanke mit den Vorher gebrauchten befehlen
        db.execSQL(createTableAbsenz);
        db.execSQL(creatTableZeitenWecker);
        db.execSQL(creatTableTageWecker);
        db.execSQL(creatTableWecker);
        db.execSQL(creatTableName);

        //Hinzufügen der von Anfang an gebrauchten Daten
        db.execSQL(inserstTageszeitMO);
        db.execSQL(inserstTageszeitMI);
        db.execSQL(inserstTageszeitAB);
        db.execSQL(insertTageMO);
        db.execSQL(insertTageDI);
        db.execSQL(insertTageMI);
        db.execSQL(insertTageDO);
        db.execSQL(insertTageFR);
    }

    //Absenzen hinzufügen (Mitgeben --> Fach und Datum)
    public long add_Note(String sFach,Date dtNoteDay) {
        //Datenbank
        SQLiteDatabase db = this.getWritableDatabase();
        //Formate der Daten --> DD.MM.YY
        SimpleDateFormat fmtDate = new SimpleDateFormat("dd.MM.YY");
        //Format in ein String speichern
        String sDate_Note = fmtDate.format(dtNoteDay);

        //Verbinndunge
        ContentValues values = new ContentValues();
        //Wert abspeichern
        values.put("Fach", sFach);
        //Wert abspeichern
        values.put("Datum", sDate_Note);
        long newID = db.insert("tbl_Absenz", null, values);
        if (newID == -1)
        {
            return -1;
        }
        else
        {
            return newID;
        }
    }



    public Cursor get_Table_All(){

        SQLiteDatabase db = this.getWritableDatabase();
        String sSQL = "SELECT * FROM tbl_Absenz";
        Cursor data = db.rawQuery(sSQL, null);
        return data;
    }
    public Cursor get_Table_Open(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sSQL = "SELECT * FROM tbl_Absenz WHERE Betrieb='False' OR Lehrer='False'";
        Cursor data = db.rawQuery(sSQL, null);
        return data;
    }
    public void update_status_betrieb(Integer IDNote){
        SQLiteDatabase db = this.getWritableDatabase();
        String sSQL = "UPDATE tbl_Absenz " +
                " SET Betrieb = 'True'"    +
        " WHERE ID=" + IDNote ;
        db.execSQL(sSQL);
    }
    public void update_status_lehrer(Integer IDNote){
        SQLiteDatabase db = this.getWritableDatabase();
        String sSQL = "UPDATE tbl_Absenz " +
                " SET Lehrer = 'True'"    +
                " WHERE ID=" + IDNote ;
        db.execSQL(sSQL);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
