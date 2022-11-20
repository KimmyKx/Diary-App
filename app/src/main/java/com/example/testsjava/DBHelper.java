package com.example.testsjava;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.UUID;

public class DBHelper extends SQLiteOpenHelper {
    public String GenerateRandomUUID() {

        UUID randomUUID = UUID.randomUUID();

        return randomUUID.toString().replaceAll("_", "");

    }

    public DBHelper(Context context) {
        super(context, "Diarydata.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table Diary(id TEXT primary key, title TEXT, content TEXT, date TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int ii) {
        DB.execSQL("drop Table if exists Diary");
    }
    public Boolean insertuserdata(String title, String content, String date, String id)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("title", title);
        contentValues.put("content", content);
        contentValues.put("date", date);
        long result=DB.insert("Diary", null, contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }
    public Boolean updateuserdata(String id, String title, String content, String date)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("content", content);
        contentValues.put("date", date);
        Cursor cursor = DB.rawQuery("Select * from Diary where id = ?", new String[]{ id });
        if (cursor.getCount() > 0) {
            long result = DB.update("Diary", contentValues, "id=?", new String[]{ id });
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    public Boolean deletedata(String id)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Diary where id = ?", new String[]{ id });
        if (cursor.getCount() > 0) {
            long result = DB.delete("Diary", "id=?", new String[]{ id });
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Cursor getdata ()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Diary", null);
        return cursor;
    }
}