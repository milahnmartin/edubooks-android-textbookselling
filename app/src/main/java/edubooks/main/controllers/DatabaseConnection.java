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

import java.util.HashMap;

public class DatabaseConnection extends SQLiteOpenHelper  {

    public DatabaseConnection(Context context) {
        super(context,"edubooksDB.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Account(Id INTEGER PRIMARY KEY AUTOINCREMENT,FirstName TEXT,LastName TEXT,EmailAddress TEXT,Password TEXT,PhoneNumber TEXT)");
        db.execSQL("create table ListedBook(id INTEGER PRIMARY KEY AUTOINCREMENT,Title TEXT,Author TEXT,Category TEXT,Faculty TEXT,Quality TEXT,IsbnNumber INTEGER,isAvailible INTEGER,AccountId INTEGER,Price REAL,FOREIGN KEY(AccountId) REFERENCES Account (Id))");
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
        }
        JsonObj.put("Result", Boolean.valueOf(false));
        JsonObj.put("Message", "Something went wrong while creating user");
        return JsonObj;
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
    public HashMap<String,String> getAccountInfo(int userID) {
        HashMap<String, String> userInfo = new HashMap<>();
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select * from Account where id = ?", new String[]{String.valueOf(userID)});
        if (cursor.moveToFirst()) {
            userInfo.put("FirstName", cursor.getString(1));
            userInfo.put("LastName", cursor.getString(2));
            userInfo.put("EmailAddress", cursor.getString(3));
            userInfo.put("PhoneNumber", cursor.getString(5));
            return userInfo;
        }
        return null;
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
        Cursor cursor = DB.rawQuery("SELECT Id FROM Account where EmailAddress = ? AND Password = ?",new String[]{EmailAddress,Password});
//        RETURNS -1 IF DOESNT EXIST AND 1 IF DOES EXIST HENCE -1 WILL RETURN FALSE
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
        return -1;
    }

    public Cursor getListingPageDetails(){
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT Title,Author,IsbnNumber,Price FROM ListedBook",null);
        return cursor;
    }

    public Cursor getListofBooksViaIsbN(int isbnNumber){
        SQLiteDatabase DB = this.getReadableDatabase();
        return DB.rawQuery("SELECT Title,Author,IsbnNumber,Price FROM ListedBook WHERE IsbnNumber = ?",new String[]{String.valueOf(isbnNumber)});
    }

    //listing page check if clicked item is in db to confirm and then take over to page specific
    public int getBookName(String BookName){
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM ListedBook WHERE Title = ?",new String[]{String.valueOf(BookName)});
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
        return -1;
    }

    public JSONObject insertNewBook(String Title, String Author, String Category, String faculty, String Quality ,int IsbnNumber, boolean isAvailible,float bookPrice,int accountId) throws JSONException {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        // Going to user the JSONObject to provide a result and feedback
        JSONObject JsonObj = new JSONObject();
        contentValues.put("Title",Title);
        contentValues.put("Author",Author);
        contentValues.put("Category",Category);
        contentValues.put("Faculty",faculty);
        contentValues.put("Quality",Quality);
        contentValues.put("IsbnNumber",IsbnNumber);
        contentValues.put("IsAvailible",isAvailible);
        contentValues.put("Price",bookPrice);
        contentValues.put("AccountId",accountId);
        long result = DB.insert("ListedBook",null,contentValues);
        if (result != -1) {
            JsonObj.put("Result", Boolean.valueOf(true));
            JsonObj.put("Message","Book Added Succesfully");
            return JsonObj;
        }
        JsonObj.put("Result", Boolean.valueOf(false));
        JsonObj.put("Message", "Something went wrong while adding new Book");
        return JsonObj;
    }

    public Cursor listingBookSearchQuery(String sQuery){
        SQLiteDatabase DB = this.getReadableDatabase();
        return DB.rawQuery("SELECT * FROM ListedBook WHERE Title LIKE '%' || ? || '%'",new String[]{sQuery});
    }
}