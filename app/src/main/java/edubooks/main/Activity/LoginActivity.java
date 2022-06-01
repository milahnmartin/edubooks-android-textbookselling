package edubooks.main.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import edubooks.main.R;
import edubooks.main.controllers.DatabaseConnection;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView UsernameStr = findViewById(R.id.Username);
        TextView PasswordStr = findViewById(R.id.Password);
    }
    public void handleLogin(View v){
        DatabaseConnection EduDB = new DatabaseConnection(this);
        Boolean userValid = EduDB.validateUser("userEmail","UserPassword");
    }
}