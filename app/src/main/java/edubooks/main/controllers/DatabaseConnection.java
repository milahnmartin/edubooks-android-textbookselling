package edubooks.main.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class DatabaseConnection extends SQLiteOpenHelper  {

    public DatabaseConnection(Context context) {
        super(context,"edubooksDB.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Account(Id INTEGER PRIMARY KEY AUTOINCREMENT,FirstName TEXT,LastName TEXT,EmailAddress TEXT,Password TEXT,PhoneNumber TEXT)");
        db.execSQL("create table ListedBook(id INTEGER PRIMARY KEY AUTOINCREMENT,Title TEXT,Author TEXT,Category TEXT,Faculty TEXT,IsbnNumber INTEGER,isAvailible INTEGER,AccountId INTEGER,Price REAL,FOREIGN KEY(AccountId) REFERENCES Account (Id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Account");
        db.execSQL("drop table if exists ListedBook");
        this.onCreate(db);
    }

    public JSONObject insertAccountDetails(String FirstName, String LastName, String EmailAddress, String Password, String PhoneNumber) throws JSONException {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        // Going to user the JSONObject to provide a result and feedback
        JSONObject JsonObj = new JSONObject();
        contentValues.put("FirstName",FirstName);
        contentValues.put("LastName",LastName);
        contentValues.put("EmailAddress",EmailAddress);
        contentValues.put("Password",Password);
        contentValues.put("PhoneNumber",PhoneNumber);
        Cursor doesUserEmailExistResultCursor = DB.rawQuery("SELECT EmailAddress FROM Account WHERE EmailAddress = ?",new String[]{EmailAddress});
        if (doesUserEmailExistResultCursor.getCount() > 0) {
            JsonObj.put("Result", Boolean.valueOf(false));
            JsonObj.put("Message", "This Email Address Already Exists");
            return JsonObj;
        }
        long result = DB.insert("Account",null,contentValues);
        if (result != -1) {
            JsonObj.put("Result", Boolean.valueOf(true));
            return JsonObj;
        } else {
            JsonObj.put("Result", Boolean.valueOf(false));
            JsonObj.put("Message", "Something went wrong while creating user");
            return JsonObj;
        }
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
        int result = DB.update("Account",contentValues,"Id=?",new String[]{String.valueOf(userId)});
//      THIS CHECKS IF ANY RECORDS WERE CHANGED RETURN TRUE IF IT DID FALSE IF NOT
        return result != -1;
    }
    public Cursor getAccountInfo(int userID){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select * from Account where id = ?",new String[]{String.valueOf(userID)});
//      RETURNS CURSOR OBJECT THAT IS A ITERABLE AND REQUIRES LOOP TO ITERATE THROUGH
        return cursor;
    }

    public Cursor getAllAccountInfo(){
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("select * from Account",null);
        return cursor;
    }

    public Boolean deleteAccount(int userID){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select * from Account where id = ?",new String[]{String.valueOf(userID)});
        if(cursor.getCount() > 0){
            int result = DB.delete("Account","Id = ?",new String[]{String.valueOf(userID)});
            return result != -1;
        }
//      USER DOENST EXIST
        return false;
    }

    public int validateUser(String EmailAddress,String Password){
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM Account where EmailAddress = ? AND Password = ?",new String[]{EmailAddress,Password});
//        RETURNS -1 IF DOESNT EXIST AND 1 IF DOES EXIST HENCE -1 WILL RETURN FALSE
        if(cursor.getCount() > 0){
            Cursor iDCursor = DB.rawQuery("SELECT Id FROM Account WHERE EmailAddress = ? AND Password = ?",new String[]{EmailAddress,Password});
            return iDCursor.getInt(0);
        }
        return -1;
    }

    public StringBuffer getAllListedBooks(){
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM ListedBook",null);
        StringBuffer ListedBooksAll = new StringBuffer();
        while(cursor.moveToNext()){
            ListedBooksAll.append(cursor.getString(0)).append("\n");
            ListedBooksAll.append(cursor.getString(1)).append("\n");
            ListedBooksAll.append(cursor.getString(2)).append("\n");
            ListedBooksAll.append(cursor.getString(3)).append("\n");
            ListedBooksAll.append(cursor.getString(4)).append("\n");
            ListedBooksAll.append(cursor.getString(5)).append("\n");
            ListedBooksAll.append(cursor.getString(6)).append("\n");
            ListedBooksAll.append(cursor.getString(7)).append("\n");
            ListedBooksAll.append(cursor.getString(8)).append("\n");
            ListedBooksAll.append(cursor.getString(9)).append("\n");
        }
        return ListedBooksAll;
    }

    public Boolean doesBookIdExist(int id){
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM ListedBook WHERE id = ?",new String[]{String.valueOf(id)});
        return cursor.getCount() > 0;
    }

    public Cursor getListofBooksViaIsbN(int isbnNumber){
        SQLiteDatabase DB = this.getReadableDatabase();
        return DB.rawQuery("SELECT * FROM ListedBook WHERE IsbnNumber = ?",new String[]{String.valueOf(isbnNumber)});
    }
}