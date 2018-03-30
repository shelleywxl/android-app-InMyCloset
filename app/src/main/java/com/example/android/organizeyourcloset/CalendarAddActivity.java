package com.example.android.organizeyourcloset;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarAddActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DatabaseHandler db;
    private TextView txtDate;
    private Button btnAdd;
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private Spinner spinner11,spinner22;
    private String chosenDate;
    private Item chosenItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.add);
        setContentView(R.layout.activity_calendar_add);

        // get the chosenDate passed by CalendarFragment
        Intent intent = getIntent();
        chosenDate = intent.getStringExtra("chosenDate");

        txtDate = (TextView)findViewById(R.id.calendar_add_chosen_date_tv);
        txtDate.setText(chosenDate);

        btnAdd = (Button)findViewById(R.id.calendar_add_add_btn);

        spinner11 = (Spinner)findViewById(R.id.spinner11);
        spinner22 = (Spinner)findViewById(R.id.spinner22);
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this, R.array.Kind, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner11.setAdapter(adapter1);
        spinner11.setOnItemSelectedListener(this);

        db = new DatabaseHandler(this);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(CalendarAddActivity.this, CalendarActivity.class);
                        startActivity(i);
                    }
                }
        );
    }


    @Override
    public void onStart() {
        super.onStart();

        // for category spinner
        spinner22.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sp2 = String.valueOf(spinner22.getSelectedItem());
                if(!sp2.equals("All") && !sp2.equals("全部")) {
                    showRecords("", sp2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });

        // add chosenItem with chosenDate in database TABLE_WORN
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.checkWornItem(chosenItem.getID(), chosenDate)) {
                    Toast.makeText(getBaseContext(), R.string.imagealreadyselectedforthisdate, Toast.LENGTH_SHORT).show();
                }
                else {
                    db.addInWorn(chosenItem.getID(), chosenDate);
                    Intent intent = new Intent(CalendarAddActivity.this, CalendarActivity.class);
                    startActivity(intent);
//                    onBackPressed();
                }
            }
        });

    }

    // for kind spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch(adapterView.getId()) {

            case R.id.spinner11:

                String sp1 = String.valueOf(spinner11.getSelectedItem());
                showRecords(sp1, "All");

                if (sp1.contentEquals("All") || sp1.contentEquals("全部")) {

                    spinner22.setEnabled(false);
                }
                if (sp1.contentEquals("Top") || sp1.contentEquals("上装")) {
                    spinner22.setEnabled(true);
                    ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this, R.array.Top, android.R.layout.simple_spinner_item);
                    spinner22.setAdapter(adapter2);
                }
                if (sp1.contentEquals("Bottom") || sp1.contentEquals("下装")) {
                    spinner22.setEnabled(true);
                    ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this, R.array.Bottom, android.R.layout.simple_spinner_item);
                    spinner22.setAdapter(adapter2);
                }
                if (sp1.contentEquals("Footwear") || sp1.contentEquals("鞋类")) {
                    spinner22.setEnabled(true);
                    ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this, R.array.Footwear, android.R.layout.simple_spinner_item);
                    spinner22.setAdapter(adapter2);
                }
                if (sp1.contentEquals("Accessories") || sp1.contentEquals("配饰")) {
                    spinner22.setEnabled(true);
                    ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this, R.array.Accessories, android.R.layout.simple_spinner_item);
                    spinner22.setAdapter(adapter2);
                }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Do nothing
    }


    //Retrieve data from the database and set to the grid view
    private void showRecords(String kind, String category){  //change to gridView
        ArrayList<Item> itemArray = new ArrayList<Item>();
        List<Item> items = db.getAllItems(kind, category);
        for (Item item : items) {
            itemArray.add(item);
        }

        gridView = (GridView)findViewById(R.id.calendar_add_gridview);

        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, itemArray);
        gridView.setAdapter(gridAdapter);

        // set click listener for each image in the grid view
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                chosenItem = (Item) parent.getItemAtPosition(position);

                gridAdapter.setSelectedPosition(position);
                gridAdapter.notifyDataSetChanged();
            }
        });
    }

}
