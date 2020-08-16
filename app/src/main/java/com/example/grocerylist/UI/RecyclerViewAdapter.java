package com.example.grocerylist.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerylist.Activities.DetailActivity;
import com.example.grocerylist.Activities.MainActivity;
import com.example.grocerylist.Data.DatabaseHandler;
import com.example.grocerylist.Model.Grocery;
import com.example.grocerylist.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Grocery> groceryList;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater layoutInflater;

    public RecyclerViewAdapter(Context context, List<Grocery> groceryList) {
        this.context = context;
        this.groceryList = groceryList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
            Grocery grocery = groceryList.get(position);

            holder.groceryItemName.setText(grocery.getName());
            holder.quantity.setText(grocery.getQuantity());
            holder.dateAdded.setText(grocery.getDateItemAdded());
    }

    @Override
    public int getItemCount() {
        return groceryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView groceryItemName,quantity,dateAdded;
        public Button editButton,deleteButton;
        public int id;

        public ViewHolder(@NonNull View view, final Context ctx) {
            super(view);
            context = ctx;

            groceryItemName = (TextView) view.findViewById(R.id.name);
            quantity = (TextView ) view.findViewById(R.id.quantity);
            dateAdded = (TextView) view.findViewById(R.id.dateAdd);
            editButton =(Button) view.findViewById(R.id.editButton);
            deleteButton = (Button) view.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //go to next scrren/Detail Activity
                    int position = getAdapterPosition();

                    Grocery grocery = groceryList.get(position);
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("name",grocery.getName());
                    intent.putExtra("quantity",grocery.getQuantity());
                    intent.putExtra("id",grocery.getId());
                    intent.putExtra("date",grocery.getDateItemAdded());
                    context.startActivity(intent);

                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.editButton:
                    int position = getAdapterPosition();
                    Grocery groceriees  = groceryList.get(position);
                    editItem(groceriees);
                    break;

                case R.id.deleteButton :
                    int pos = getAdapterPosition();
                    Grocery groceries = groceryList.get(pos);
                    deleteItem(groceries.getId());

                    break;
            }
        }
        public void deleteItem(final int id)
        {
            builder = new AlertDialog.Builder(context);
            layoutInflater = LayoutInflater.from(context);

            View view = layoutInflater.inflate(R.layout.confirmation_dialog,null);
            Button noButton = (Button) view.findViewById(R.id.noButton);
            Button yesButton = (Button) view.findViewById(R.id.yesButton);

            builder.setView(view);
            dialog = builder.create();
            dialog.show();

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db = new DatabaseHandler(context);
                    db.deleteGrocery(id);
                    groceryList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    dialog.dismiss();
                    if(db.getGroceryCount()==0){
                        context.startActivity(new Intent(context, MainActivity.class));
                    }
                }
            });

        }

        public void editItem(final Grocery grocery)
        {
            builder = new AlertDialog.Builder(context);
            layoutInflater = LayoutInflater.from(context);

            final View view = layoutInflater.inflate(R.layout.popup,null);

            final EditText groceryItem = (EditText) view.findViewById(R.id.groceryItem);
            final EditText groceryQuantity = (EditText) view.findViewById(R.id.groceryQuantity);
            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText("Edit Grocery");
            Button saveButton = (Button) view.findViewById(R.id.saveButton);

            builder.setView(view);
            dialog = builder.create();
            dialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db = new DatabaseHandler(context);

                    grocery.setName(groceryItem.getText().toString());
                    grocery.setQuantity(groceryQuantity.getText().toString());

                    if(!groceryItem.getText().toString().isEmpty()
                        && !groceryQuantity.getText().toString().isEmpty()){
                        db.updateGrocery(grocery);

                        notifyItemChanged(getAdapterPosition(),grocery);
                    }else {
                        Snackbar.make(view,"Enter Grocery and Quantity",Snackbar.LENGTH_LONG);
                    }
                    dialog.dismiss();
                }
            });
        }
    }
}
