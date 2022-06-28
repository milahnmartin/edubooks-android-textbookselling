package edubooks.main.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import edubooks.main.R;
import edubooks.main.controllers.DatabaseConnection;

public class UserListings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlisting);

        //Hides the action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Class Objects
        DatabaseConnection DatabaseConnectionObj = new DatabaseConnection(UserListings.this);

        // Variables
        Intent loggeduser = getIntent();
        //-1 shows user_id couldnt be retrieved or error occured
        int userId = loggeduser.getIntExtra("user_id",-1);

        final ListView list = findViewById(R.id.list);
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor cursor = DatabaseConnectionObj.getUserSpecificListings(userId);
        //StringBuffer ListedBooksAll = new StringBuffer();
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                String BookN = cursor.getString(0);
                String Author = cursor.getString(1);
                String ISBN = cursor.getString(2);
                String Price = cursor.getString(3);
                String id = cursor.getString(4);
                String listitem ="Book Name:" + BookN + "\nAuthor:" +  Author + "\nISBN:" + ISBN + "\nPrice: R" +Price + "\nBookId: " +id;
                arrayList.add(listitem);
            }
        }
        else{
            arrayList.add("You do not have any books listed.");
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(UserListings.this, android.R.layout.simple_list_item_1, arrayList);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem = (String) list.getItemAtPosition(position);
                boolean validateAndRetrieveId;
                if (!Objects.equals(clickedItem, "You do not have any books listed.")) {
                    String New_list = clickedItem.replaceAll(":", ",");
                    String Spaceless = New_list.replaceAll("\\n+", ",");
                    String[] new_arr = Spaceless.split(",");

                    //check to ensure that the listing is in the database
                    validateAndRetrieveId = DatabaseConnectionObj.doesBookExist(new_arr[new_arr.length - 1]);
                    //if success then grab isbn and take over to the next page.
                    Log.d("CLICKED_ITEM", String.valueOf(Boolean.valueOf(validateAndRetrieveId)));
                    Log.d("CLICKED_ITEM", Arrays.toString(new_arr));

                    if(validateAndRetrieveId){
                        Intent goBookpage = new Intent(UserListings.this, UserBookSpecificActivity.class);
                        Bundle clickedArgs = new Bundle();
                        clickedArgs.putString("bookid",new_arr[new_arr.length - 1]);
                        clickedArgs.putInt("userid",userId);
                        goBookpage.putExtras(clickedArgs);
                        startActivity(goBookpage);
                    }
                    Snackbar.make(view, "Error Occured When Trying to Get Book Details", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(view, "No Records to display.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        ImageView BackHome = findViewById(R.id.BackButton);
        BackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                Intent RedirectLogin = new Intent(UserListings.this, MenuActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("user_id",userId);
                extras.putString("activity_initiate","edubooks.main.Activity.UserListings");
                RedirectLogin.putExtras(extras);
                startActivity(RedirectLogin);
            }
        });
    }
}
