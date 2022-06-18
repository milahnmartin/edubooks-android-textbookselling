package edubooks.main.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Objects;

import edubooks.main.R;
import edubooks.main.controllers.DatabaseConnection;

public class BookSpecificActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_specific);

        Intent LoginIntent = getIntent();
        //-1 shows user_id couldnt be retrieved or error occured
        int isbnget = LoginIntent.getIntExtra("isbn",0);
        Objects.requireNonNull(getSupportActionBar()).hide();
        ImageView BackHome = (ImageView) findViewById(R.id.BackButton);
        BackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                Intent RedirectLogin = new Intent(BookSpecificActivity.this, ListingActivity.class);
                startActivity(RedirectLogin);
            }
        });
        final ListView list = findViewById(R.id.list);
        ArrayList<String> arrayList = new ArrayList<>();
        DatabaseConnection DatabaseConnectionObj = new DatabaseConnection(BookSpecificActivity.this);
        Cursor isbn = DatabaseConnectionObj.getListofBooksViaIsbN(isbnget);
        while(isbn.moveToNext()){
            String BookN = isbn.getString(0);
            String Author = isbn.getString(1);
            String ISBN = isbn.getString(2);
            String Price = isbn.getString(3);
            String listitem = "Book Name:" + BookN + "\nAuthor:" +  Author + "\nISBN:" + ISBN + "\nPrice: R" +Price;
            arrayList.add(listitem);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem = (String) list.getItemAtPosition(position);
            }
        });


    }
}