package edubooks.main.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import edubooks.main.R;
import edubooks.main.controllers.DatabaseConnection;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void handleLogin(View v){
        DatabaseConnection EduDB = new DatabaseConnection(this);
        Boolean userValid = EduDB.validateUser("userEmail","UserPassword");
    }
}