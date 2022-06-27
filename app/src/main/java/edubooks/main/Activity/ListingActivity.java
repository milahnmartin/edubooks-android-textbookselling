package edubooks.main.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import edubooks.main.R;
import edubooks.main.controllers.DatabaseConnection;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Array;
import java.util.ArrayList;

import java.util.Objects;

public class ListingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);
        Intent LoginIntent = getIntent();
        //-1 shows user_id couldnt be retrieved or error occured
        int userId = LoginIntent.getIntExtra("user_id",-1);

        Objects.requireNonNull(getSupportActionBar()).hide();

        ImageView BackHome = (ImageView) findViewById(R.id.BackButton);
        BackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                //Snackbar.make(v1, ListedBooks.toString(), Snackbar.LENGTH_SHORT).show();
                Intent RedirectMenu = new Intent(ListingActivity.this, MenuActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("user_id",userId);
                extras.putString("activity_initiate","edubooks.main.Activity.ListingActivity");
                RedirectMenu.putExtras(extras);
                startActivity(RedirectMenu);
            }
        });
        DatabaseConnection DatabaseConnectionObj = new DatabaseConnection(ListingActivity.this);
        EditText listingsSearch = findViewById(R.id.Search);

        listingsSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchParams = String.valueOf(s);
                final ListView list = findViewById(R.id.list);
                ArrayList<String> arrayList = new ArrayList<>();
                Cursor cursor = DatabaseConnectionObj.listingBookSearchQuery(searchParams);
                //StringBuffer ListedBooksAll = new StringBuffer();
                if(cursor.getCount() > 0){
                    while(cursor.moveToNext()){
                        String BookN = cursor.getString(0);
                        String Author = cursor.getString(1);
                        String ISBN = cursor.getString(2);
                        String Price = cursor.getString(3);
                        String listitem = "Book Name:" + BookN + "\nAuthor:" +  Author + "\nISBN:" + ISBN + "\nPrice: R" +Price;
                        arrayList.add(listitem);
                    }
                }
                else{
                    //@todo do we need to add this into a array if there are no books?
                    arrayList.add("Database has no records.");
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ListingActivity.this, android.R.layout.simple_list_item_1, arrayList);
                list.setAdapter(arrayAdapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String clickedItem = (String) list.getItemAtPosition(position);
                        int validateAndRetrieveId = -1;
                        if(clickedItem != "Database has no records.") {
                            String New_list = clickedItem.replaceAll(":", ",");
                            String Spaceless = New_list.replaceAll("\\n+", ",");
                            String[] new_arr = Spaceless.split(",");
                            Toast.makeText(ListingActivity.this, new_arr[5], Toast.LENGTH_LONG).show();
                            //check to ensure that the listing is in the database
                            validateAndRetrieveId = DatabaseConnectionObj.getBookName(new_arr[1]);
                            //if success then grab isbn and take over to the next page.
                            if (validateAndRetrieveId != -1) {
                                Snackbar.make(view, "Success", Snackbar.LENGTH_SHORT).show();
                                String ISBNnum = (new_arr[5]);
                                Intent goBookpage = new Intent(ListingActivity.this, BookSpecificActivity.class);
                                goBookpage.putExtra("isbn", ISBNnum + "," + userId);
                                startActivity(goBookpage);
                            } else {
                                Snackbar.make(view, "The information provided is incorrect.", Snackbar.LENGTH_SHORT).show();
                            }
                        }else {
                            Snackbar.make(view, "No Records to display.", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        final ListView list = findViewById(R.id.list);
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor cursor = DatabaseConnectionObj.getListingPageDetails();
        //StringBuffer ListedBooksAll = new StringBuffer();
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                String BookN = cursor.getString(0);
                String Author = cursor.getString(1);
                String ISBN = cursor.getString(2);
                String Price = cursor.getString(3);
                String listitem = "Book Name:" + BookN + "\nAuthor:" +  Author + "\nISBN:" + ISBN + "\nPrice: R" +Price;
                arrayList.add(listitem);
            }
        }
        else{
            //@todo do we need to add this into a array if there are no books?
            arrayList.add("Database has no records.");
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ListingActivity.this, android.R.layout.simple_list_item_1, arrayList);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem = (String) list.getItemAtPosition(position);
                int validateAndRetrieveId = -1;
                if(clickedItem != "Database has no records.") {
                    String New_list = clickedItem.toString().replaceAll(":", ",");
                    String Spaceless = New_list.toString().replaceAll("\\n+", ",");
                    String[] new_arr = Spaceless.split(",");
                    Toast.makeText(ListingActivity.this, new_arr[5].toString(), Toast.LENGTH_LONG).show();
                    //check to ensure that the listing is in the database
                    validateAndRetrieveId = DatabaseConnectionObj.getBookName(new_arr[1]);
                    //if success then grab isbn and take over to the next page.
                    if (validateAndRetrieveId != -1) {
                        Snackbar.make(view, "Success", Snackbar.LENGTH_SHORT).show();
                        int ISBNnum = Integer.parseInt(new_arr[5]);
                        Intent goBookpage = new Intent(ListingActivity.this, BookSpecificActivity.class);
                        goBookpage.putExtra("isbn", ISBNnum + "," + userId);
                        startActivity(goBookpage);
                    } else {
                        Snackbar.make(view, "The information provided is incorrect.", Snackbar.LENGTH_SHORT).show();
                    }
                }else {
                    Snackbar.make(view, "No Records to display.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });



    }
}