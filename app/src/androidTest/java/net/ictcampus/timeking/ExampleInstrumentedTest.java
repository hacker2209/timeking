package net.ictcampus.timeking;

import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.Date;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("net.ictcampus.timeking", appContext.getPackageName());
    }

    @Test
    public void checkSetName() {
        String nameTest = "Hello";
        String dbName = "";
        Context testContent = InstrumentationRegistry.getTargetContext();
        Database_SQLite db = new Database_SQLite(testContent);
        db.update_name(nameTest);
        Cursor name = db.get_Table_Name();
        int colName = name.getColumnIndex("Name");
        while (name.moveToNext()) {
            dbName = name.getString(colName);
        }
        db.close();
        assertEquals(nameTest, dbName);

    }

    @Test
    public void checkAbsenz() {
        Context testContent = InstrumentationRegistry.getTargetContext();
        Database_SQLite db = new Database_SQLite(testContent);
        String testFach = "";
        db.add_Note("Englisch", Date.valueOf("2019-06-15"));
        Cursor dbAbs = db.get_Table_All();
        int lastCol = dbAbs.getColumnIndex("Fach");
        while (dbAbs.moveToNext()) {
            testFach = dbAbs.getString(lastCol);
        }
        assertEquals("Englisch", testFach);

    }

    @Test
    public void checkSchultag() {
        Context testContent = InstrumentationRegistry.getTargetContext();
        int tid = 0;
        Database_SQLite db = new Database_SQLite(testContent);
        db.add_Schultag(2);
        Cursor dbSchultage = db.get_Table_Schultage();
        int colTID = dbSchultage.getColumnIndex("TagID");
        while (dbSchultage.moveToNext()) {
            tid = dbSchultage.getInt(colTID);
        }
        assertEquals(2, tid);
    }

    @Test
    public void checkWecker() {
        Context testContent = InstrumentationRegistry.getTargetContext();
        Database_SQLite db = new Database_SQLite(testContent);
        int zid = 0;
        db.add_Wecker(1, 1);
        Cursor dbWecker = db.get_Table_Wecker();
        int colZID = dbWecker.getColumnIndex("TageszeitID");
        while (dbWecker.moveToNext()) {
            zid = dbWecker.getInt(colZID);
        }
        assertEquals(1, zid);
    }
}
