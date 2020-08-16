package com.example.grocerylist.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.grocerylist.R;

public class DetailActivity extends AppCompatActivity {

    private TextView itemName,quantity,dateAdded;
    private int groceryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        itemName = (TextView) findViewById(R.id.itemNameDet);
        quantity = (TextView) findViewById(R.id.quantityDet);
        dateAdded = (TextView) findViewById(R.id.dateDet);

        Bundle extras = getIntent().getExtras();

        if(extras!=null){
            itemName.setText(extras.getString("name"));
            quantity.setText(extras.getString("quantity"));
            dateAdded.setText(extras.getString("date"));
            groceryId = extras.getInt("id");
        }

    }
}
