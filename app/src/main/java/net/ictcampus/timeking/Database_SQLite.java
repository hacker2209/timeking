package net.ictcampus.timeking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Editable;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class Database_SQLite extends SQLiteOpenHelper {
    private static final String DatabaseName = "timeking.db";

    public Database_SQLite(Context context) {
        super(context, DatabaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableAbsenz = "CREATE TABLE tbl_Absenz " +
                "(  ID INTEGER PRIMARY KEY AUTOINCREMENT " +
                " , Fach TEXT " +      //*Note of type TEXT
                " , Datum DATETIME " +       //*Note of type TEXT
                " , Betrieb  TEXT DEFAULT 'False' " +
                " , Lehrer  TEXT DEFAULT 'False' " +
                ")";
        String creatTableZeitenWecker = "CREATE TABLE zeiten_Wecker" +
                "( ID INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", Tageszeit TEXT" +
                ")";
        String creatTableTageWecker = "CREATE TABLE Tage_Wecker" +
                "( ID INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", Tag TEXT" +
                ")";
        String creatTableWecker = "CREATE TABLE Wecker" +
                "( ID INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", TageszeitID INTEGER" +
                ",TagID INTEGER" +
                ",FOREIGN KEY(TageszeitID) REFERENCES zeiten_Wecker(ID)" +
                ")";

        String creatTableName = "CREATE TABLE Name" +
                "( ID INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", Name TEXT" +
                ")";
        String creatTableSchultag = "CREATE TABLE Schultag" +
                "( ID INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", TagID INTEGER" +
                ", FOREIGN KEY(TagID) REFERENCES Tage_Wecker(ID)" +
                ")";
        //Insert befehle für die Tageszeit und den Tag
        String inserstTageszeitMO = "INSERT INTO zeiten_Wecker (ID, Tageszeit) VALUES ( 1, 'Morgen')";
        String inserstTageszeitMI = "INSERT INTO zeiten_Wecker (ID, Tageszeit) VALUES ( 2,'Mittag')";
        String inserstTageszeitAB = "INSERT INTO zeiten_Wecker (ID, Tageszeit) VALUES ( 3, 'Abend')";
        String insertTageMO = "INSERT INTO Tage_Wecker (ID, Tag) VALUES (1, 'MO')";
        String insertTageDI = "INSERT INTO Tage_Wecker (ID, Tag) VALUES (2, 'DI')";
        String insertTageMI = "INSERT INTO Tage_Wecker (ID, Tag) VALUES (3, 'MI')";
        String insertTageDO = "INSERT INTO Tage_Wecker (ID, Tag) VALUES (4, 'DO')";
        String insertTageFR = "INSERT INTO Tage_Wecker (ID, Tag) VALUES (5, 'FR')";
        String insertName = "INSERT INTO Name (ID, Tag) VALUES (1, 'Default')";


        //Erstellt die Datenbanke mit den Vorher gebrauchten befehlen
        db.execSQL(createTableAbsenz);
        db.execSQL(creatTableZeitenWecker);
        db.execSQL(creatTableTageWecker);
        db.execSQL(creatTableWecker);
        db.execSQL(creatTableName);
        db.execSQL(creatTableSchultag);

        //Hinzufügen der von Anfang an gebrauchten Daten
        db.execSQL(inserstTageszeitMO);
        db.execSQL(inserstTageszeitMI);
        db.execSQL(inserstTageszeitAB);
        db.execSQL(insertTageMO);
        db.execSQL(insertTageDI);
        db.execSQL(insertTageMI);
        db.execSQL(insertTageDO);
        db.execSQL(insertTageFR);
        db.execSQL(insertName);
    }

    //Absenzen hinzufügen (Mitgeben --> Fach und Datum)
    public long add_Note(String sFach, Date dtNoteDay) {
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
        if (newID == -1) {
            return -1;
        } else {
            return newID;
        }
    }

    //Absenzen hinzufügen (Mitgeben --> Fach und Datum)
    public long add_Schultag(int id) {
        //Datenbank
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //Wert abspeichern
        values.put("TagID", id);
        long newID = db.insert("Schultag", null, values);
        if (newID == -1) {
            return -1;
        } else {
            return newID;
        }
    }




    public Cursor get_Table_All() {

        SQLiteDatabase db = this.getWritableDatabase();
        String sSQL = "SELECT * FROM tbl_Absenz";
        Cursor data = db.rawQuery(sSQL, null);
        return data;
    }

    public Cursor get_Table_Zeit() {

        SQLiteDatabase db = this.getWritableDatabase();
        String sSQL = "SELECT * FROM zeiten_Wecker";
        Cursor data = db.rawQuery(sSQL, null);
        return data;
    }

    public Cursor get_Table_Tage() {

        SQLiteDatabase db = this.getWritableDatabase();
        String sSQL = "SELECT * FROM Tage_Wecker";
        Cursor data = db.rawQuery(sSQL, null);
        return data;
    }
    public Cursor get_Table_Name() {

        SQLiteDatabase db = this.getWritableDatabase();
        String sSQL = "SELECT * FROM Name";
        Cursor data = db.rawQuery(sSQL, null);
        return data;
    }

    public Cursor get_Table_Open() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sSQL = "SELECT * FROM tbl_Absenz WHERE Betrieb='False' OR Lehrer='False'";
        Cursor data = db.rawQuery(sSQL, null);
        return data;
    }

    public void update_status_betrieb(Integer IDNote) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sSQL = "UPDATE tbl_Absenz " +
                " SET Betrieb = 'True'" +
                " WHERE ID=" + IDNote;
        db.execSQL(sSQL);
    }
    public void update_name(Integer IDNote,String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sSQL = "UPDATE Name " +
                " SET Name = "+newName +
                " WHERE ID=" + IDNote;
        db.execSQL(sSQL);
    }

    public void update_status_lehrer(Integer IDNote) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sSQL = "UPDATE tbl_Absenz " +
                " SET Lehrer = 'True'" +
                " WHERE ID=" + IDNote;
        db.execSQL(sSQL);
    }
    public void clearWecker() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sSQL = "DELETE FROM Wecker";
        db.execSQL(sSQL);
    }


    public long insert_wecker(int idDay, int idTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Verbinndunge
        ContentValues values = new ContentValues();
        //Wert abspeichern
        values.put("TageszeitID", idTime);
        values.put("TagID", idDay);
        long newID = db.insert("Wecker", null, values);
        if (newID == -1) {
            return -1;
        } else {
            return newID;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
