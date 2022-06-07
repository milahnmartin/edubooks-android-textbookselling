package edubooks.main.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import edubooks.main.R;
import edubooks.main.controllers.DatabaseConnection;
import edubooks.main.controllers.PasswordCreation;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Hides the action bar
        Objects.requireNonNull(getSupportActionBar()).hide();
//        setTitle("EDUBOOKS - REGISTER"); TODO discuss if we rather have the no action bar
        TextView FirstNameTextView = findViewById(R.id.FirstName);
        TextView LastNameTextView = findViewById(R.id.LastName);
        TextView EmailTextView = findViewById(R.id.Email);
        TextView CellphoneTextView = findViewById(R.id.Cellphone);
        TextView PasswordTextView = findViewById(R.id.Password);

        MaterialButton SignUpBtn = (MaterialButton) findViewById(R.id.SignUpBtn);
        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Checks if all fields have been filled in
                if (
                        FirstNameTextView.getText().toString().equals("") ||
                        LastNameTextView.getText().toString().equals("") ||
                        EmailTextView.getText().toString().equals("") ||
                        CellphoneTextView.getText().toString().equals("") ||
                        PasswordTextView.getText().toString().equals("")
                ) {
                    Snackbar.make(v, "One or more fields are empty", Snackbar.LENGTH_SHORT).show();
                } else {
                    if (!isEmailValid(EmailTextView.getText().toString())) {
                        Snackbar.make(v, "Invalid Email Address", Snackbar.LENGTH_SHORT).show();
                    } else if (!isValidPassword(PasswordTextView.getText().toString())) {
                        Snackbar.make(v, "Invalid Password", Snackbar.LENGTH_SHORT).show();
                    } else {
                        // TODO Hash the Password
                        DatabaseConnection DatabaseConnectionObj = new DatabaseConnection(RegisterActivity.this);
                        try {
                            JSONObject InsertUserJsonObj =  DatabaseConnectionObj.insertAccountDetails(
                                    FirstNameTextView.getText().toString(),
                                    LastNameTextView.getText().toString(),
                                    EmailTextView.getText().toString(),
                                    PasswordTextView.getText().toString(),
                                    CellphoneTextView.getText().toString()
                            );
                            if ((Boolean) InsertUserJsonObj.get("Result")) {
                                // If Result from Json is true then user has been created
                                // and the user is redirected
                                Intent redirectLoginActivity = new Intent(RegisterActivity.this,LoginActivity.class);
                                startActivity(redirectLoginActivity);
                            } else {
                                // display the error message
                                String MessageStr = (String) InsertUserJsonObj.get("Message");
                                Snackbar.make(v, MessageStr, Snackbar.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.d("Register User Error", String.valueOf(e));
                        }
                    }
                }
            }
        });
        // Redirects to login once the "Have an Account?" Text is clicked
        TextView LoginRedirect = (TextView) findViewById(R.id.LoginText);
        LoginRedirect.setOnClickListener (new View.OnClickListener() {

            @Override
            public void onClick(View v1) {
                Intent RedirectLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(RedirectLogin);
            }
        });
    }

    //validates password
    private boolean isValidPassword(String PasswordStr) {
        // The password should be at least 8 characters long.
        // The password should contain at least one lower case.
        // The password should contain at least one upper case.
        // The password should have at least one digit.
        return (PasswordStr.length() >= 8) &&
                (PasswordStr.matches(".*[a-z]+.*")) &&
                (PasswordStr.matches(".*[A-Z]+.*")) &&
                (PasswordStr.matches(".*[0-9]+.*"));
    }

    // validates email
    private boolean isEmailValid(String EmailStr) {
        String RegexStr = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return EmailStr.matches(RegexStr);
    }

    public void handleCreateUser(View v){
        DatabaseConnection EduDB = new DatabaseConnection(this);
//        Boolean userValid = EduDB.insertAccountDetails();
    }

}