package com.tbgosalvez.southhills;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tbg on 1/17/17.
 */

public class UserDB extends SQLiteOpenHelper
{
    public static final String DB_NAME = "this database";
    static final int DB_VERSION = 1;

    public UserDB(Context context)
    {
        super(context, DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String query = "CREATE TABLE users ( " +
                        CONSTANTS.strEmail + " Text PRIMARY KEY," +
                        CONSTANTS.strPasswd + " Text)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    public boolean insertUser(final String email, final String passwd)
    {
        Cursor cur = getUser(email);
        long result;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CONSTANTS.strPasswd,passwd);

        if(cur.getCount() == 0)
        {
            cv.put(CONSTANTS.strEmail,email);
            result = db.insert("users",null,cv);
        }
        else
        {
            result = db.update("users", cv, CONSTANTS.strEmail+"=?", new String[]{email});
        }

        return (result >= 0);
    }

    public Cursor getUser(String email)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM users WHERE " + CONSTANTS.strEmail + "=?";

        return db.rawQuery(query, new String[]{ email });
    }

    public void deleteUser(String email)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("users", CONSTANTS.strEmail+"=?", new String[]{email});
    }
}
