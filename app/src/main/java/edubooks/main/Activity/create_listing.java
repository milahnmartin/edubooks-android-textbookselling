package edubooks.main.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.Objects;

import edubooks.main.R;

public class create_listing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_listing);
        Objects.requireNonNull(getSupportActionBar()).hide();

        TextView BookTitleTextView = findViewById(R.id.BookTitle);
        TextView AuthorNameTextView = findViewById(R.id.AuthorName);
        TextView ISBDTextView = findViewById(R.id.ISBNnr);
        TextView SellingPriceTextView = findViewById(R.id.SellingPrice);
        TextView BookConditionTextView = findViewById(R.id.BookCondition);

        Button AddList = (Button) findViewById(R.id.SubmitBook);
        AddList.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if(
                        BookTitleTextView.getText().toString().equals("") ||
                        AuthorNameTextView.getText().toString().equals("") ||
                        ISBDTextView.getText().toString().equals("") ||
                        SellingPriceTextView.getText().toString().equals("") ||
                        BookConditionTextView.getText().toString().equals("")
                ) {
                    Snackbar.make(view, "One or more fields are empty", Snackbar.LENGTH_SHORT).show();
                }
                else{
                    if(!ISBNValid(ISBDTextView.getText().toString())){
                        Snackbar.make(view, "Invalid ISBN Number", Snackbar.LENGTH_SHORT).show();
                    }
                    else if(!priceValid(SellingPriceTextView.getText().toString())){
                        Snackbar.make(view, "Invalid Price, ensure 1/2 decimals.", Snackbar.LENGTH_SHORT).show();
                    }
                    else{
                        Snackbar.make(view, "Book Listed!", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //early redictect to listing page
        ImageButton BackHome = (ImageButton) findViewById(R.id.BackButton);
        BackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                //Intent RedirectLogin = new Intent(create_listing.this, LoginActivity.class);
                //startActivity(RedirectLogin);
                Snackbar.make(v1, "Redirecting You Back Home!", Snackbar.LENGTH_SHORT).show();
            }
        });
        //react to click of +
        ImageButton UploadBook = (ImageButton) findViewById(R.id.AddBook);
        UploadBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                Snackbar.make(v1, "Picture Uploaded!", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
    // validate isbn for either 10/13 numbers
    private boolean ISBNValid(String isbnstr) {
        String RegexStr = "^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$";
        return isbnstr.matches(RegexStr);
    }
    // validate price with one or two decimals with either use of '.' or ','
    private boolean priceValid(String pricestr) {
        String RegexStr = "^\\d+((,|.)\\d{1,2})?$";
        return pricestr.matches(RegexStr);
    }
}
