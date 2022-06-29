package edubooks.main.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import edubooks.main.R;
import edubooks.main.controllers.DatabaseConnection;

public class UserBookSpecificActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userspecificlisting);
        //Hides the action bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        //Class Objects
        DatabaseConnection DatabaseConnectionObj = new DatabaseConnection(UserBookSpecificActivity.this);

        Intent Intent = getIntent();
        Bundle profileListingArgs = Intent.getExtras();
        String clickedBookId = profileListingArgs.getString("bookid");
        int sessionUserId = profileListingArgs.getInt("userid");
        Log.d("BOOK_SPECIFIC",clickedBookId);
        Log.d("BOOK_SPECIFIC",String.valueOf(sessionUserId));

        TextView BookTitleTextView = findViewById(R.id.BookTitle);
        TextView AuthorNameTextView = findViewById(R.id.AuthorName);
        TextView ISBNTextView = findViewById(R.id.ISBNnr);
        TextView BookCategoryTextView = findViewById(R.id.Category);
        TextView BookFacultyTextView = findViewById(R.id.Faculty);
        TextView SellingPriceTextView = findViewById(R.id.SellingPrice);
        TextView BookConditionTextView = findViewById(R.id.BookCondition);

        String BookTitleStr = null;
        String AuthorNameStr = null;
        String ISBNStr = null;
        String BookCategoryStr = null;
        String BookFacultyStr = null;
        String SellingPriceStr = null;
        String BookConditionStr = null;

        try {
            JSONObject UserCurrentDataJsonObj = DatabaseConnectionObj.getSpecificBook(clickedBookId);
            BookTitleStr = (String) UserCurrentDataJsonObj.get("BookTitle");
            AuthorNameStr = (String) UserCurrentDataJsonObj.get("Author");
            ISBNStr = (String) UserCurrentDataJsonObj.get("IsbnNumber");
            BookCategoryStr = (String) UserCurrentDataJsonObj.get("Category");
            BookFacultyStr = (String) UserCurrentDataJsonObj.get("Faculty");
            SellingPriceStr = (String) UserCurrentDataJsonObj.get("Price");
            BookConditionStr = (String) UserCurrentDataJsonObj.get("Quality");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        BookTitleTextView.setText(BookTitleStr);
        AuthorNameTextView.setText(AuthorNameStr);
        ISBNTextView.setText(ISBNStr);
        BookCategoryTextView.setText(BookCategoryStr);
        BookFacultyTextView.setText(BookFacultyStr);
        SellingPriceTextView.setText(SellingPriceStr);
        BookConditionTextView.setText(BookConditionStr);

        Button AddList = findViewById(R.id.SubmitBook);
        String FinalBookTitleStr = BookTitleStr;
        String FinalAuthorNameStr = AuthorNameStr;
        String FinalISBNStr = ISBNStr;
        String FinalBookCategoryStr = BookCategoryStr;
        String FinalBookFacultyStr = BookFacultyStr;
        String FinalSellingPriceStr = SellingPriceStr;
        String FinalBookConditionStr = BookConditionStr;
        AddList.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if (
                        (BookTitleTextView.getText().toString().equals(FinalBookTitleStr) &&
                        AuthorNameTextView.getText().toString().equals(FinalAuthorNameStr) &&
                        ISBNTextView.getText().toString().equals(FinalISBNStr) &&
                        BookCategoryTextView.getText().toString().equals(FinalBookCategoryStr) &&
                        BookFacultyTextView.getText().toString().equals(FinalBookFacultyStr) &&
                        SellingPriceTextView.getText().toString().equals(FinalSellingPriceStr) &&
                        BookConditionTextView.getText().toString().equals(FinalBookConditionStr)) ||
                        (BookTitleTextView.getText().toString().equals(FinalBookTitleStr) &&
                        AuthorNameTextView.getText().toString().equals("") &&
                        ISBNTextView.getText().toString().equals("") &&
                        BookCategoryTextView.getText().toString().equals("") &&
                        BookFacultyTextView.getText().toString().equals("") &&
                        SellingPriceTextView.getText().toString().equals("") &&
                        BookConditionTextView.getText().toString().equals(""))
                ) {
                    Snackbar.make(view, "No Details Have Been Changed", Snackbar.LENGTH_SHORT).show();
                } else {
                    CreateListing CreateListingObj = new CreateListing();
                    JSONObject JsonObj = new JSONObject();
                    boolean IsEmailValidBool = true;
                    String MessageStr = null;
                    if (!CreateListingObj.ISBNValid(ISBNTextView.getText().toString())){
                        IsEmailValidBool = false;
                        MessageStr = "Invalid ISBN Number";
                    } else if (!CreateListingObj.priceValid(SellingPriceTextView.getText().toString())) {
                        IsEmailValidBool = false;
                        MessageStr = "Invalid Price, ensure 1/2 decimals.";
                    }
                    try {
                        JsonObj.put("Result", IsEmailValidBool);
                        JsonObj.put("Message", MessageStr);
                        if ((Boolean) JsonObj.get("Result")) {
                            Snackbar.make(view, "Success", Snackbar.LENGTH_SHORT).show();
                            ContentValues contentValues = new ContentValues();
                            if (!BookTitleTextView.getText().toString().equals(FinalBookTitleStr) && !BookTitleTextView.getText().toString().equals("")) {
                                contentValues.put("Title", BookTitleTextView.getText().toString());
                            }
                            if (!AuthorNameTextView.getText().toString().equals(FinalAuthorNameStr) && !AuthorNameTextView.getText().toString().equals("")) {
                                contentValues.put("Author", AuthorNameTextView.getText().toString());
                            }
                            if (!ISBNTextView.getText().toString().equals(FinalISBNStr) && !ISBNTextView.getText().toString().equals("")) {
                                contentValues.put("IsbnNumber", ISBNTextView.getText().toString());
                            }
                            if (!BookCategoryTextView.getText().toString().equals(FinalBookCategoryStr) && !BookCategoryTextView.getText().toString().equals("")) {
                                contentValues.put("Category", BookCategoryTextView.getText().toString());
                            }
                            if (!BookFacultyTextView.getText().toString().equals(FinalBookFacultyStr) && !BookFacultyTextView.getText().toString().equals("")) {
                                contentValues.put("Faculty", BookFacultyTextView.getText().toString());
                            }
                            if (!SellingPriceTextView.getText().toString().equals(FinalSellingPriceStr) && !SellingPriceTextView.getText().toString().equals("")) {
                                contentValues.put("Price", SellingPriceTextView.getText().toString());
                            }
                            if (!BookConditionTextView.getText().toString().equals(FinalBookConditionStr) && !BookConditionTextView.getText().toString().equals("")) {
                                contentValues.put("Quality", BookConditionTextView.getText().toString());
                            }
                            if (DatabaseConnectionObj.updateBookDetails(contentValues,clickedBookId)) {
                                Intent RedirectLogin = new Intent(UserBookSpecificActivity.this, UserListings.class);
                                Bundle extras = new Bundle();
                                extras.putInt("user_id",sessionUserId);
                                RedirectLogin.putExtras(extras);
                                startActivity(RedirectLogin);
                            } else {
                                Snackbar.make(view, "Something Went Wrong Updating The Details", Snackbar.LENGTH_SHORT).show();
                            }
                        } else {
                            String DisplayMessageStr = (String) JsonObj.get("Message");
                            Snackbar.make(view, DisplayMessageStr, Snackbar.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
//        Button DeleteBookBtn = findViewById(R.id.DeleteBook);
//        DeleteBookBtn.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void onClick(View view) {
//                if (DatabaseConnectionObj.removeBook(clickedBookId)) {
//                    Intent RedirectLogin = new Intent(UserBookSpecificActivity.this, UserListings.class);
//                    Bundle extras = new Bundle();
//                    extras.putInt("user_id",sessionUserId);
//                    RedirectLogin.putExtras(extras);
//                    startActivity(RedirectLogin);
//                } else {
//                    Snackbar.make(view, "Something Went Deleting The Book", Snackbar.LENGTH_SHORT).show();
//                }
//            }
//        });
    ImageView BackHome = findViewById(R.id.BackButton);
        BackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                Intent RedirectLogin = new Intent(UserBookSpecificActivity.this, MenuActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("user_id",sessionUserId);
                extras.putString("activity_initiate","edubooks.main.Activity.UserListings");
                RedirectLogin.putExtras(extras);
                startActivity(RedirectLogin);
            }
        });
    }
}
