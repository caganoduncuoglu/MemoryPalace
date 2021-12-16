package com.atm.memoryPalace.utils.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.Nullable;

import com.atm.memoryPalace.entity.Memory;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "memory_palace.db";
    private static final int DATABASE_VERSION = 1;
    private DateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    private static final String TABLE_MEMORIES_CREATE =
            "CREATE TABLE " + TablesInfo.MemoryEntry.TABLE_NAME + " (" +
                    TablesInfo.MemoryEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TablesInfo.MemoryEntry.COLUMN_TITLE + " TEXT, " +
                    TablesInfo.MemoryEntry.COLUMN_DESCRIPTION + " TEXT, " +
                    TablesInfo.MemoryEntry.COLUMN_IMAGE + " BLOB, " +
                    TablesInfo.MemoryEntry.COLUMN_DATE + " TEXT, " +
                    TablesInfo.MemoryEntry.COLUMN_LOCATION + " TEXT, " +
                    TablesInfo.MemoryEntry.COLUMN_CREATE_DATE + " TEXT DEFAULT CURRENT_TIMESTAMP" +
                    ")";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_MEMORIES_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TablesInfo.MemoryEntry.TABLE_NAME);
        onCreate(db);
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public void addMemory(String title, String description, String date, String location, Bitmap bitmap) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TablesInfo.MemoryEntry.COLUMN_TITLE, title.trim());
        cv.put(TablesInfo.MemoryEntry.COLUMN_DESCRIPTION, description.trim());
        cv.put(TablesInfo.MemoryEntry.COLUMN_IMAGE, getBitmapAsByteArray(bitmap));
        cv.put(TablesInfo.MemoryEntry.COLUMN_DATE, date.trim());
        cv.put(TablesInfo.MemoryEntry.COLUMN_LOCATION, location.trim());
        cv.put(TablesInfo.MemoryEntry.COLUMN_CREATE_DATE, format.format(new Date()));


        long result = db.insert(TablesInfo.MemoryEntry.TABLE_NAME, null, cv);

        if (result > -1)
            Log.i("DatabaseHelper", "Memory successfully inserted.");
        else
            Log.i("DatabaseHelper", "Memory insertion failed.");

        db.close();
    }

    public void deleteMemory(String memoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TablesInfo.MemoryEntry.TABLE_NAME, TablesInfo.MemoryEntry.COLUMN_ID + "=?", new String[]{memoryId});
        db.close();
    }

    @SuppressLint("Range")
    public ArrayList<Memory> getMemoryList() {
        ArrayList<Memory> data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                TablesInfo.MemoryEntry.COLUMN_ID,
                TablesInfo.MemoryEntry.COLUMN_TITLE,
                TablesInfo.MemoryEntry.COLUMN_DESCRIPTION,
                TablesInfo.MemoryEntry.COLUMN_IMAGE,
                TablesInfo.MemoryEntry.COLUMN_LOCATION,
                TablesInfo.MemoryEntry.COLUMN_DATE,
                TablesInfo.MemoryEntry.COLUMN_CREATE_DATE
        };

        Cursor c = db.query(TablesInfo.MemoryEntry.TABLE_NAME, projection, null, null, null, null, null);
        while (c.moveToNext()) {
            try {
                data.add(new Memory(
                        c.getString(c.getColumnIndex(TablesInfo.MemoryEntry.COLUMN_ID)),
                        c.getString(c.getColumnIndex(TablesInfo.MemoryEntry.COLUMN_TITLE)),
                        c.getString(c.getColumnIndex(TablesInfo.MemoryEntry.COLUMN_DESCRIPTION)),
                        getImage(c.getBlob(c.getColumnIndex(TablesInfo.MemoryEntry.COLUMN_IMAGE))),
                        c.getString(c.getColumnIndex(TablesInfo.MemoryEntry.COLUMN_LOCATION)),
                        format.parse(c.getString(c.getColumnIndex(TablesInfo.MemoryEntry.COLUMN_DATE))),
                        format.parse(c.getString(c.getColumnIndex(TablesInfo.MemoryEntry.COLUMN_CREATE_DATE)))
                    ));
            } catch (ParseException e) {
                Log.e("DatabaseHelper", "ParseException occurred." , e);
                e.printStackTrace();
            }
        }
        c.close();
        db.close();

        return data;
    }

}
