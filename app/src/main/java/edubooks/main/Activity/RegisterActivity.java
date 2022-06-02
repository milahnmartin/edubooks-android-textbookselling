package edubooks.main.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

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
                        if (
                                DatabaseConnectionObj.insertAccountDetails(
                                        FirstNameTextView.getText().toString(),
                                        LastNameTextView.getText().toString(),
                                        EmailTextView.getText().toString(),
                                        CellphoneTextView.getText().toString(),
                                        PasswordTextView.getText().toString()
                                )
                        ) {
                            Snackbar.make(v, "Account Successfully created", Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(v, "Something went wrong while creating user.", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }
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