package edubooks.main.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.Objects;

import edubooks.main.R;

public class create_listing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_listing);
        Objects.requireNonNull(getSupportActionBar()).hide();
    }
}
