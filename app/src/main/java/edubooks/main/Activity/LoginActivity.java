package edubooks.main.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import edubooks.main.R;
import edubooks.main.controllers.DatabaseConnection;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Hides the action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        TextView EmailTextView = findViewById(R.id.Email);
        TextView PasswordTextView = findViewById(R.id.Password);

        MaterialButton LoginBtn = (MaterialButton) findViewById(R.id.LoginBtn);
        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (
                        EmailTextView.getText().toString().equals("") ||
                        PasswordTextView.getText().toString().equals("")
                ) {
                    Snackbar.make(v, "One or more fields are empty", Snackbar.LENGTH_SHORT).show();
                } else {
                    DatabaseConnection DatabaseConnectionObj = new DatabaseConnection(LoginActivity.this);
                    int validateAndRetrieveId = DatabaseConnectionObj.validateUser(EmailTextView.getText().toString(),PasswordTextView.getText().toString());
                    if (validateAndRetrieveId != -1) {
                        Snackbar.make(v, "Success", Snackbar.LENGTH_SHORT).show();
                        Intent listingIntent = new Intent(LoginActivity.this,ListingActivity.class);
                        listingIntent.putExtra("user_id",validateAndRetrieveId);
                        startActivity(listingIntent);
                    } else {
                        Snackbar.make(v, "The information provided is incorrect.", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Redirects to Signup once the "Don't Have an Account?" Text is clicked
        TextView SignUpRedirect = (TextView) findViewById(R.id.SignUp);
        SignUpRedirect.setOnClickListener (new View.OnClickListener() {

            @Override
            public void onClick(View v1) {
                Intent RedirectSignUp = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(RedirectSignUp);
            }
        });
    }
}