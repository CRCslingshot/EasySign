package net.ayman.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String DB_NAME = "AymanSignsApp";
    public static final String TABLE_NAME = "VideoData";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_VIDEO = "video";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_VALUE = "value";

    public static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (\n" +
                "    " + COLUMN_ID + " integer NOT NULL CONSTRAINT FoodItems_pk PRIMARY KEY AUTOINCREMENT,\n" +
                "    " + COLUMN_VIDEO + " blob NOT NULL,\n" +
                "    " + COLUMN_TYPE + " varchar(255) NOT NULL,\n" +
                "    " + COLUMN_VALUE + " varchar(255) NOT NULL\n" +
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        db.execSQL(sql);
        onCreate(db);
    }

    private boolean isDataAlreadySaved(String value) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_VALUE + " = " + value + ";", null);
        return cursor.moveToFirst();
    }

    public boolean saveData(String uri, String type, String value) {
        if (isDataAlreadySaved(value)) {
            return false;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_VIDEO, uri);
        contentValues.put(COLUMN_TYPE, type);
        contentValues.put(COLUMN_VALUE, value);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, null, contentValues);

        return true;
    }

    public String getVideo(String alphabet) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_VALUE + " = ? ;";
        Cursor cursor = db.rawQuery(sql, new String[]{alphabet});

        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(COLUMN_VIDEO));
        }
        return null;
    }

}
