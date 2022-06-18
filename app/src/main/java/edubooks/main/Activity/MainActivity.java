package edubooks.main.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import edubooks.main.R;
import edubooks.main.controllers.DatabaseConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("THIS IS M");
        setContentView(R.layout.activity_main);


    }

    public void handleButton(@NonNull View v) {
        EditText eduUsername = findViewById(R.id.eduEmail);
        EditText eduPassword = findViewById(R.id.eduPassword);

        String eduEmailText = eduUsername.getText().toString();
        String eduPasswordText = eduPassword.getText().toString();

//        DatabaseConnection loginDB = new DatabaseConnection(this);
//        Boolean queryResult = loginDB.insertAccountDetails(eduEmailText,eduPasswordText);
//        if(queryResult){
//            Toast.makeText(this,"EDUSQL SUCCESS",Toast.LENGTH_LONG).show();
//        }else{
//            Toast.makeText(this,"EDUSQL ERROR",Toast.LENGTH_LONG).show();
//        }

    }

    public void getDataBTN(@NonNull View v){
        DatabaseConnection dataDB = new DatabaseConnection(this);
        Cursor myTestResults = dataDB.getAllAccountInfo();
        if(myTestResults.getCount() == 0){
            Toast.makeText(this,"NO DATA",Toast.LENGTH_LONG).show();
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while(myTestResults.moveToNext()){
            buffer.append("UserID :"+myTestResults.getString(0)+"\n");
            buffer.append("FirstName :"+myTestResults.getString(1)+"\n");
            buffer.append("LastName :"+myTestResults.getString(2)+"\n");
            buffer.append("EmailAddress :"+myTestResults.getString(3)+"\n");
            buffer.append("Password :"+myTestResults.getString(4)+"\n");
            buffer.append("PhoneNumber :"+myTestResults.getString(5)+"\n");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("User Entries");
        builder.setMessage(buffer.toString());
        builder.show();
        Log.d("MYBUFFER", String.valueOf(buffer.length()));
    }
}