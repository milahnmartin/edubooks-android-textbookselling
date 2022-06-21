package edubooks.main.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import edubooks.main.R;
import edubooks.main.controllers.DatabaseConnection;

public class CreateListing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_listing);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Intent loggeduser = getIntent();
        //-1 shows user_id couldnt be retrieved or error occured
        int userId = loggeduser.getIntExtra("user_id",-1);

        TextView BookTitleTextView = findViewById(R.id.BookTitle);
        TextView AuthorNameTextView = findViewById(R.id.AuthorName);
        TextView ISBNTextView = findViewById(R.id.ISBNnr);
        TextView BookCategory = findViewById(R.id.Category);
        TextView BookFaculty = findViewById(R.id.Faculty);
        TextView SellingPriceTextView = findViewById(R.id.SellingPrice);
        TextView BookConditionTextView = findViewById(R.id.BookCondition);
        //Spinner spinnercat=findViewById(R.id.BookCategory);
        //ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this, R.array.BookCategories, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        //spinnercat.setAdapter(adapter);

        Button AddList = (Button) findViewById(R.id.SubmitBook);
        AddList.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if(
                        BookTitleTextView.getText().toString().equals("") ||
                        AuthorNameTextView.getText().toString().equals("") ||
                        ISBNTextView.getText().toString().equals("") ||
                        BookCategory.getText().toString().equals("") ||
                        BookFaculty.getText().toString().equals("") ||
                        SellingPriceTextView.getText().toString().equals("") ||
                        BookConditionTextView.getText().toString().equals("")
                ) {
                    Snackbar.make(view, "One or more fields are empty", Snackbar.LENGTH_SHORT).show();
                }
                else{
                    if(!ISBNValid(ISBNTextView.getText().toString())){
                        Snackbar.make(view, "Invalid ISBN Number", Snackbar.LENGTH_SHORT).show();
                    }
                    else if(!priceValid(SellingPriceTextView.getText().toString())){
                        Snackbar.make(view, "Invalid Price, ensure 1/2 decimals.", Snackbar.LENGTH_SHORT).show();
                    }
                    else{
                        //____________________________
                        DatabaseConnection DatabaseConnectionObj = new DatabaseConnection(CreateListing.this);
    //insertNewBook(String Title*, String Author*, String Category*, String faculty*, String Quality* ,int IsbnNumber*, boolean isAvailible*,float bookPrice*,int accountId*)
                        try {
                            JSONObject InsertBookJsonObj =  DatabaseConnectionObj.insertNewBook(
                                    BookTitleTextView.getText().toString(),
                                    AuthorNameTextView.getText().toString(),
                                    BookCategory.getText().toString(),
                                    BookFaculty.getText().toString(),
                                    BookConditionTextView.getText().toString(),
                                    Integer.parseInt(ISBNTextView.getText().toString()),
                                    true,
                                    Float.parseFloat(SellingPriceTextView.getText().toString()),
                                    userId
                            );
                            if ((Boolean) InsertBookJsonObj.get("Result")) {
                                // If Result from Json is true then user has been created
                                Snackbar.make(view, "Book Listed!", Snackbar.LENGTH_SHORT).show();
                                // and the user is redirected
                                Intent RedirectLogin = new Intent(CreateListing.this, ListingActivity.class);
                                RedirectLogin.putExtra("user_id",userId);
                                startActivity(RedirectLogin);
                            } else {
                                // display the error message
                                String MessageStr = (String) InsertBookJsonObj.get("Message");
                                Snackbar.make(view, MessageStr, Snackbar.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.d("Register User Error", String.valueOf(e));
                        }

                        //___________________________
                    }
                }
            }
        });
        //early redictect to listing page
        ImageView BackHome = (ImageView) findViewById(R.id.BackButton);
        BackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                Intent RedirectLogin = new Intent(CreateListing.this, ListingActivity.class);
                RedirectLogin.putExtra("user_id",userId);
                startActivity(RedirectLogin);
            }
        });
        //react to click of +
        ImageButton UploadBook = (ImageButton) findViewById(R.id.AddBook);
        UploadBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                Snackbar.make(v1, "Picture Uploaded!" + userId, Snackbar.LENGTH_SHORT).show();
            }
        });
    }
    // validate isbn for either 10/13 numbers
    public boolean ISBNValid(String isbnstr) {
        String RegexStr = "^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$";
        return isbnstr.matches(RegexStr);
    }
    // validate price with one or two decimals with either use of '.' or ','
    public boolean priceValid(String pricestr) {
        String RegexStr = "^\\d+((,|.)\\d{1,2})?$";
        return pricestr.matches(RegexStr);
    }
}
