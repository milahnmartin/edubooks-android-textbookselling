package edubooks.main.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import edubooks.main.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("EDUBOOKS - REGISTER");
    }
}