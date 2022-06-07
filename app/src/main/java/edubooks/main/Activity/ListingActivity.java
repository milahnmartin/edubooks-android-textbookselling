package edubooks.main.Activity;

import androidx.appcompat.app.AppCompatActivity;
import edubooks.main.R;

import android.content.Intent;
import android.os.Bundle;

public class ListingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);
        Intent LoginIntent = getIntent();
        int userId = LoginIntent.getIntExtra("user_id",-1);
//        -1 shows user_id couldnt be retrieved or error occured
    }
}