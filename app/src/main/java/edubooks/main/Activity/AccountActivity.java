package edubooks.main.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import edubooks.main.R;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilesettings);

        Intent loggeduser = getIntent();
        //-1 shows user_id couldnt be retrieved or error occured
        int userId = loggeduser.getIntExtra("user_id",-1);

        ImageView BackHome = (ImageView) findViewById(R.id.BackButton);
        BackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                Intent RedirectLogin = new Intent(AccountActivity.this, ListingActivity.class);
                RedirectLogin.putExtra("user_id",userId);
                startActivity(RedirectLogin);
            }
        });
    }
}