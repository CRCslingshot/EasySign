package net.ayman.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String DB_NAME = "AymanSignsApp";
    public static final String TABLE_NAME = "Signs";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PHRASE_ENGLISH = "phrase_english";
    public static final String COLUMN_PHRASE_ARABIC = "phrase_arabic";
    public static final String COLUMN_LETTER = "letter";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_SIGN = "sign";

    public static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (\n" +
                "    " + COLUMN_ID + " INTEGER NOT NULL CONSTRAINT employees_pk PRIMARY KEY AUTOINCREMENT,\n" +
                "    " + COLUMN_PHRASE_ENGLISH + " varchar(255) NOT NULL,\n" +
                "    " + COLUMN_PHRASE_ARABIC + " varchar(255) NOT NULL,\n" +
                "    " + COLUMN_SIGN + " varchar(255) NOT NULL " +
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        db.execSQL(sql);
        onCreate(db);
    }

    private boolean isDataAlreadySaved() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return cursor.moveToFirst();
    }

    private void savePhrases() {

    }

}
