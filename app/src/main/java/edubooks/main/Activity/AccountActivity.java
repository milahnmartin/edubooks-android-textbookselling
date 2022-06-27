package edubooks.main.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import edubooks.main.R;
import edubooks.main.controllers.DatabaseConnection;
import edubooks.main.controllers.PasswordCreation;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilesettings);

        //Hides the action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Objects
        RegisterActivity RegisterActivity = new RegisterActivity();
        DatabaseConnection DatabaseConnectionObj = new DatabaseConnection(AccountActivity.this);

        // Variables
        Intent loggeduser = getIntent();
        //-1 shows user_id couldnt be retrieved or error occured
        int userId = loggeduser.getIntExtra("user_id",-1);
        TextView FirstNameTextView = (TextView)findViewById(R.id.Name);
        TextView LastNameTextView = (TextView)findViewById(R.id.Surname);
        TextView EmailTextView = (TextView)findViewById(R.id.Email);
        TextView PhoneNumberTextView = (TextView)findViewById(R.id.PhoneNumber);
        TextView PasswordTextView = (TextView)findViewById(R.id.ProfilePassword);
        String FirstNameStr = null;
        String LastNameStr = null;
        String EmailAddressStr = null;
        String PhoneNumberStr = null;

        try {
            JSONObject UserCurrentDataJsonObj = DatabaseConnectionObj.getAccountInfo(userId);
            FirstNameStr = (String) UserCurrentDataJsonObj.get("FirstName");
            LastNameStr = (String) UserCurrentDataJsonObj.get("LastName");
            EmailAddressStr = (String) UserCurrentDataJsonObj.get("EmailAddress");
            PhoneNumberStr = (String) UserCurrentDataJsonObj.get("PhoneNumber");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FirstNameTextView.setText(FirstNameStr);
        LastNameTextView.setText(LastNameStr);
        EmailTextView.setText(EmailAddressStr);
        PhoneNumberTextView.setText(PhoneNumberStr);

        MaterialButton EditInformationBtn = (MaterialButton) findViewById(R.id.EditInformation);
        String FinalFirstNameStr = FirstNameStr;
        String FinalLastNameStr = LastNameStr;
        String FinalEmailAddressStr = EmailAddressStr;
        String FinalPhoneNumberStr = PhoneNumberStr;
        EditInformationBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                // checks if the input boxes are not the same as the db or empty
                if (
                        (FirstNameTextView.getText().toString().equals(FinalFirstNameStr) &&
                        LastNameTextView.getText().toString().equals(FinalLastNameStr) &&
                        EmailTextView.getText().toString().equals(FinalEmailAddressStr) &&
                        PhoneNumberTextView.getText().toString().equals(FinalPhoneNumberStr) &&
                        PasswordTextView.getText().toString().equals("")) ||
                        (FirstNameTextView.getText().toString().equals("") &&
                        LastNameTextView.getText().toString().equals("") &&
                        EmailTextView.getText().toString().equals("") &&
                        PhoneNumberTextView.getText().toString().equals("") &&
                        PasswordTextView.getText().toString().equals(""))

                ) {
                    Snackbar.make(v, "No Details Have Been Changed", Snackbar.LENGTH_SHORT).show();
                }  else {
                    JSONObject JsonObj = new JSONObject();
                    boolean IsEmailValidBool = true;
                    String MessageStr = null;
                    if (!EmailTextView.getText().toString().equals(FinalEmailAddressStr) && !EmailTextView.getText().toString().equals("")) {
                        if (!RegisterActivity.isEmailValid(EmailTextView.getText().toString())) {
                            IsEmailValidBool = false;
                            MessageStr = "Invalid Email Address";
                        }
                        if (DatabaseConnectionObj.doesEmailExist(EmailTextView.getText().toString())) {
                            IsEmailValidBool = false;
                            MessageStr = "This Email Address Already Exists";
                        }
                    }

                    try {
                        JsonObj.put("Result", IsEmailValidBool);
                        JsonObj.put("Message", MessageStr);
                        if ((Boolean) JsonObj.get("Result")) {
                            ContentValues contentValues = new ContentValues();
                            if (!FirstNameTextView.getText().toString().equals(FinalFirstNameStr) && !FirstNameTextView.getText().toString().equals("")) {
                                contentValues.put("FirstName",FirstNameTextView.getText().toString());
                            }
                            if (!LastNameTextView.getText().toString().equals(FinalLastNameStr) && !LastNameTextView.getText().toString().equals("")) {
                                contentValues.put("LastName",LastNameTextView.getText().toString());
                            }
                            if (!PhoneNumberTextView.getText().toString().equals(FinalPhoneNumberStr) && !PhoneNumberTextView.getText().toString().equals("")) {
                                contentValues.put("PhoneNumber",PhoneNumberTextView.getText().toString());
                            }
                            if (!EmailTextView.getText().toString().equals(FinalEmailAddressStr) && !EmailTextView.getText().toString().equals("")) {
                                contentValues.put("EmailAddress",EmailTextView.getText().toString());
                            }
                            if (!PasswordTextView.getText().toString().equals("")) {
                                if (RegisterActivity.isValidPassword(PasswordTextView.getText().toString())) {
                                    String EncryptedPasswordStr = PasswordCreation.encryptPassword(PasswordTextView.getText().toString());
                                    contentValues.put("Password",EncryptedPasswordStr);
                                }
                            }
                            if (DatabaseConnectionObj.updateAccountDetails(contentValues, userId)) {
                                Snackbar.make(v, "Details Updated Successfully", Snackbar.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(v, "Something Went Wrong Updating The Details", Snackbar.LENGTH_SHORT).show();
                            }
                        } else {
                            String DisplayMessageStr = (String) JsonObj.get("Message");
                            Snackbar.make(v, DisplayMessageStr, Snackbar.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        ImageView BackHome = findViewById(R.id.BackButton);
        BackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                Intent RedirectLogin = new Intent(AccountActivity.this, MenuActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("user_id",userId);
                extras.putString("activity_initiate","edubooks.main.Activity.AccountActivity");
                RedirectLogin.putExtras(extras);
                startActivity(RedirectLogin);
            }
        });
        MaterialButton MyListingsBtn = (MaterialButton) findViewById(R.id.MyListing);
        MyListingsBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Intent RedirectLogin = new Intent(AccountActivity.this, UserListings.class);
                Bundle extras = new Bundle();
                extras.putInt("user_id",userId);
                extras.putString("activity_initiate","edubooks.main.Activity.AccountActivity");
                RedirectLogin.putExtras(extras);
                startActivity(RedirectLogin);
            }
        });
    }
}