package edubooks.main.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseConnection extends SQLiteOpenHelper  {

    public DatabaseConnection(Context context) {
        super(context,"edubooksDB.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("create table Account(id INTEGER PRIMARY KEY AUTOINCREMENT,FirstName TEXT,LastName TEXT,EmailAddress TEXT,Password TEXT,PhoneNumber TEXT)");
        db.execSQL("create table TestLogin(id INTEGER PRIMARY KEY AUTOINCREMENT,FirstName TEXT,LastName TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists TestLogin");
    }

    public Boolean insertAccountDetails(String FirstName,String LastName,String EmailAddress,String Password,String PhoneNumber){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("FirstName",FirstName);
        contentValues.put("LastName",LastName);
        contentValues.put("EmailAddress",EmailAddress);
        contentValues.put("Password",Password);
        contentValues.put("PhoneNumber",PhoneNumber);
        long result = DB.insert("Account",null,contentValues);

        return result != -1;
    }

    public Boolean insertTestlogin(String FirstName,String LastName){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("FirstName",FirstName);
        contentValues.put("LastName",LastName);
        long result = DB.insert("TestLogin",null,contentValues);
        return result != -1;
    }
    public Cursor getTestInfo(){
        SQLiteDatabase DB = this.getWritableDatabase();
        return DB.rawQuery("select * from TestLogin",null);
    }


    public Boolean updateAccountDetails(String FirstName,String LastName,String EmailAddress,String Password,String PhoneNumber,int userId){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("FirstName",FirstName);
        contentValues.put("LastName",LastName);
        contentValues.put("LastName",LastName);
        contentValues.put("EmailAddress",EmailAddress);
        contentValues.put("Password",Password);
        contentValues.put("PhoneNumber",PhoneNumber);
        long result = DB.update("Account",contentValues,"Id=?",new String[]{String.valueOf(userId)});

        return result != -1;
    }
//
    public Cursor getAccountInfo(int userID){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select * from Account where id = ?",new String[]{String.valueOf(userID)});
        return cursor;
    }
}