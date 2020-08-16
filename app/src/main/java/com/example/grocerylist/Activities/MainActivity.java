package com.example.grocerylist.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.grocerylist.Data.DatabaseHandler;
import com.example.grocerylist.Model.Grocery;
import com.example.grocerylist.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private EditText groceryItem,groceryQty;
    private Button saveButton;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);
        byPAssActivity();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
              //          .setAction("Action", null).show();
                createPopup();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createPopup(){
        builder = new  AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup,null);

        groceryItem = (EditText) view.findViewById(R.id.groceryItem);
        groceryQty = (EditText) view.findViewById(R.id.groceryQuantity);
        saveButton = (Button) view.findViewById(R.id.saveButton);

        builder.setView(view);

        dialog = builder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save to database
                //move to next Screen
                if(!groceryItem.getText().toString().isEmpty()
                     && !groceryQty.getText().toString().isEmpty()) {
                    saveGroceryToDB(v);
                }
            }
        });
    }

    private void saveGroceryToDB(View v) {
        Grocery grocery = new Grocery();
        String newGroceryItem = groceryItem.getText().toString();
        String newquantity = groceryQty.getText().toString();

        grocery.setName(newGroceryItem);
        grocery.setQuantity(newquantity);

        db.addGrocery(grocery);

        Snackbar.make(v,"Item Saved",Snackbar.LENGTH_SHORT).show();

        Log.d("Saved Id :",String.valueOf(db.getGroceryCount()));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                //STart a new Activity
                startActivity(new Intent(MainActivity.this,ListActivity.class));
            }
        },1200);

    }

    public void byPAssActivity(){
        //if database by some entry then
        //it will go directly to list activity
        if(db.getGroceryCount()>0){
            startActivity(new Intent(MainActivity.this,ListActivity.class));
            finish();
        }
    }

}
