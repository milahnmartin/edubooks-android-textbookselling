package edubooks.main.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

import edubooks.main.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Intent menu = getIntent();
        //-1 shows user_id couldnt be retrieved or error occured
        int userId = menu.getIntExtra("user_id",-1);

        Button btnHome = findViewById(R.id.navhome);
        Button btnCreateListing = findViewById(R.id.navcreatelisting);
        Button btnProfile = findViewById(R.id.navprofile);
        Button btnLogout = findViewById(R.id.navlogout);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ListingActivity.class);
                startActivity(intent);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, AccountActivity.class);
                intent.putExtra("user_id",userId);
                startActivity(intent);
            }
        });

        btnCreateListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, CreateListing.class);
                intent.putExtra("user_id",userId);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }


}

