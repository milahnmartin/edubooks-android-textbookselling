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
        String getvariable = LoginIntent.getStringExtra("bookid");
        String[] new_arr = getvariable.split(",");
        String bookid = new_arr[0];
        int userId = Integer.parseInt(new_arr[1]);

        Objects.requireNonNull(getSupportActionBar()).hide();
        ImageView BackHome = findViewById(R.id.BackButton);
        BackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                Intent RedirectLogin = new Intent(BookSpecificActivity.this, ListingActivity.class);
                RedirectLogin.putExtra("user_id",userId);
                startActivity(RedirectLogin);
            }
        });
        final ListView list = findViewById(R.id.list);
        ArrayList<String> arrayList = new ArrayList<>();
        DatabaseConnection DatabaseConnectionObj = new DatabaseConnection(BookSpecificActivity.this);
        Cursor isbn = DatabaseConnectionObj.getListofBooksViaIsbN(bookid);
        arrayList.add("Book information:");
        String[] bookinfo = new String[] { "Book owner information:", ""};
        while(isbn.moveToNext()){
            String BookN = isbn.getString(0);
            String Author = isbn.getString(1);
            String ISBN = isbn.getString(2);
            String Price =  isbn.getString(3);
            String id_dat = isbn.getString(4);
            bookinfo[1] = id_dat;
            String listitem = "Book Name:" + BookN + "\nAuthor:" +  Author + "\nISBN:" + ISBN + "\nPrice: R" +Price;
            arrayList.add(listitem);
        }
        //db.execSQL("create table Account(Id INTEGER PRIMARY KEY AUTOINCREMENT,FirstName TEXT,LastName TEXT,EmailAddress TEXT,Password TEXT,PhoneNumber TEXT)");
        arrayList.add(bookinfo[0]);
        Cursor bookowner = DatabaseConnectionObj.getOwnerId(Integer.parseInt(bookinfo[1]));
        while(bookowner.moveToNext()){
            String fname = bookowner.getString(1);
            String lname = bookowner.getString(2);
            String email = bookowner.getString(3);
            String number = bookowner.getString(5);
            String ownerdetails = "Owner Name:" + fname + " " + lname + "\n" + "Owner email:" + email +
                    "\nOwner Phone Number:" + number;
            arrayList.add(ownerdetails);
        }
        //ImageView i = new ImageView(this);
        //i.setImageResource(R.drawable.img_1);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem = (String) list.getItemAtPosition(position);
            }
        });
//        final ListView list2 = findViewById(R.id.list2);
//        ArrayList<ImageView> imageList = new ArrayList<>();
//        ImageView image = new ImageView(this);
//        image.setImageResource(R.drawable.img_1);
//        imageList.add(image);
//        //imagelist.add(R.drawable.img_1);
//        //iv.setImageResource(imagelist .get(position));
//        ArrayAdapter<ImageView> imageAdapter = new ArrayAdapter<ImageView>(this, android.R.layout.simple_list_item_1, imageList);
//        list2.setAdapter(imageAdapter);
//        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String clickedItem = (String) list2.getItemAtPosition(position);
//            }
//        });

//        final ListView list2 = findViewById(R.id.list2);
//        ArrayList<String> imagelist = new ArrayList<>();
//        imagelist.add("!BOOK PICTURE!");
//        ArrayAdapter<String> imageadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, imagelist);
//        list2.setAdapter(imageadapter);
//        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String clickedItem = (String) list2.getItemAtPosition(position);
//            }
//        });
    }
}