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
        db.execSQL("create table ListedBook(id INTEGER PRIMARY KEY AUTOINCREMENT,Title TEXT,Author TEXT,Category TEXT,Faculty TEXT,Quality TEXT,IsbnNumber TEXT,isAvailible INTEGER,AccountId INTEGER,Price REAL,FOREIGN KEY(AccountId) REFERENCES Account (Id))");
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
        if (doesEmailExist(EmailAddress)) {
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

    public Boolean doesEmailExist(String EmailAddress) {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor doesUserEmailExistResultCursor = DB.rawQuery("SELECT EmailAddress FROM Account WHERE EmailAddress = ?",new String[]{EmailAddress});
        return doesUserEmailExistResultCursor.getCount() > 0;
    }

    public Boolean updateAccountDetails(ContentValues AccountDetailsContentValues, int userId){
        SQLiteDatabase DB = this.getWritableDatabase();
        int result = DB.update("Account",AccountDetailsContentValues,"Id=?",new String[]{String.valueOf(userId)});
//      THIS CHECKS IF ANY RECORDS WERE CHANGED RETURN TRUE IF IT DID FALSE IF NOT
        return result != -1;
    }
    public JSONObject getAccountInfo(int userID) throws JSONException {
        JSONObject JsonObj = new JSONObject();
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select * from Account where id = ?", new String[]{String.valueOf(userID)});
        if (cursor.moveToFirst()) {
            JsonObj.put("FirstName",cursor.getString(1));
            JsonObj.put("LastName",cursor.getString(2));
            JsonObj.put("EmailAddress",cursor.getString(3));
            JsonObj.put("PhoneNumber",cursor.getString(5));
            return JsonObj;
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
        return DB.rawQuery("SELECT Title,Author,IsbnNumber,Price,id FROM ListedBook WHERE isAvailible <> 0",null);
    }

    public Cursor getUserSpecificListings(int UserIdInt) {
        SQLiteDatabase DB = this.getReadableDatabase();
        return DB.rawQuery(
            "SELECT LB.Title, LB.Author, LB.IsbnNumber, LB.Price, LB.id FROM Account\n" +
            "INNER JOIN ListedBook LB on Account.Id = LB.AccountId\n" +
            "WHERE AccountId = ? AND LB.isAvailible <> 0",new String[]{String.valueOf(UserIdInt)}
        );
    }

    public JSONObject getSpecificBook(String BookIdInt) {
        JSONObject JsonObj = new JSONObject();
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT Title, Author, IsbnNumber, Category, Faculty, Price, Quality  from ListedBook where id = ?", new String[]{String.valueOf(BookIdInt)});
        if (cursor.moveToFirst()) {
            try {
                JsonObj.put("BookTitle",cursor.getString(0));
                JsonObj.put("Author",cursor.getString(1));
                JsonObj.put("IsbnNumber",cursor.getString(2));
                JsonObj.put("Category",cursor.getString(3));
                JsonObj.put("Faculty",cursor.getString(4));
                JsonObj.put("Price",cursor.getString(5));
                JsonObj.put("Quality",cursor.getString(6));
                return JsonObj;
            } catch (JSONException e) {
                Log.d("Error in loading Book", String.valueOf(e));
            }

        }
        return null;
    }

    public Boolean updateBookDetails(ContentValues BookDetailsContentValues, String BookIdInt) {
        SQLiteDatabase DB = this.getWritableDatabase();
        int result = DB.update("ListedBook", BookDetailsContentValues, "Id=?", new String[]{String.valueOf(BookIdInt)});
//      THIS CHECKS IF ANY RECORDS WERE CHANGED RETURN TRUE IF IT DID FALSE IF NOT
        return result != -1;
    }

    public Boolean removeBook(String BookIdInt) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isAvailible", 0);
        int result = DB.update("ListedBook", contentValues, "Id=?",new String[]{String.valueOf(BookIdInt)});
//      THIS CHECKS IF ANY RECORDS WERE CHANGED RETURN TRUE IF IT DID FALSE IF NOT
        return result != -1;
    }

    public Cursor getListofBooksViaIsbN(String isbnNumber){
        SQLiteDatabase DB = this.getReadableDatabase();
        return DB.rawQuery("SELECT Title,Author,IsbnNumber,Price, AccountId FROM ListedBook WHERE id = ?",new String[]{String.valueOf(isbnNumber)});
    }

    public Cursor getOwnerId(int id){
        SQLiteDatabase DB = this.getReadableDatabase();
        return DB.rawQuery("SELECT * FROM Account WHERE Id = ?",new String[]{String.valueOf(id)});
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

    public JSONObject insertNewBook(String Title, String Author, String Category, String faculty, String Quality ,String IsbnNumber, boolean isAvailible,float bookPrice,int accountId) throws JSONException {
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
        return DB.rawQuery("SELECT Title,Author,IsbnNumber,Price,id FROM ListedBook WHERE Title LIKE '%' || ? || '%'",new String[]{sQuery});
    }
}