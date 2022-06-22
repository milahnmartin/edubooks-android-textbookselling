package edubooks.main.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import edubooks.main.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Intent menu = getIntent();
        Bundle extras = menu.getExtras();
        int userId = extras.getInt("user_id");
        String initiatedActivity = extras.getString("activity_initiate");
        Button btnHome = findViewById(R.id.navhome);
        Button btnCreateListing = findViewById(R.id.navcreatelisting);
        Button btnProfile = findViewById(R.id.navprofile);
        Button btnLogout = findViewById(R.id.navlogout);
        ImageView closeMenuBTN = findViewById(R.id.closeMenuBTN);

        closeMenuBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                try {
                    intent = new Intent(MenuActivity.this, Class.forName(initiatedActivity));
                    intent.putExtra("user_id",userId);
                } catch (ClassNotFoundException e) {
                    Snackbar.make(v, "Something Went Wrong", Snackbar.LENGTH_SHORT).show();
                }
                startActivity(intent);
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ListingActivity.class);
                intent.putExtra("user_id",userId);
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

