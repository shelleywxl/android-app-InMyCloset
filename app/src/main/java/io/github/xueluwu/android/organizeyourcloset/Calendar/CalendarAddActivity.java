package io.github.xueluwu.android.organizeyourcloset.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.github.xueluwu.android.organizeyourcloset.Adapter.GridViewAdapter;
import io.github.xueluwu.android.organizeyourcloset.Closet.ClosetFilterActivity;
import io.github.xueluwu.android.organizeyourcloset.DatabaseHandler;
import io.github.xueluwu.android.organizeyourcloset.Item;
import io.github.xueluwu.android.organizeyourcloset.R;

import static io.github.xueluwu.android.organizeyourcloset.Closet.ClosetFilterActivity.CALENDAR_CHOSEN_DATE;
import static io.github.xueluwu.android.organizeyourcloset.Closet.ClosetFilterActivity.FILTER_FOR_CLOSET_OR_CALENDAR;
import static io.github.xueluwu.android.organizeyourcloset.Closet.ClosetFilterActivity.INTENT_SELECTED_BRANDS;
import static io.github.xueluwu.android.organizeyourcloset.Closet.ClosetFilterActivity.INTENT_SELECTED_CATEGORIES;
import static io.github.xueluwu.android.organizeyourcloset.Closet.ClosetFilterActivity.INTENT_SELECTED_KINDS;
import static io.github.xueluwu.android.organizeyourcloset.Closet.ClosetFilterActivity.INTENT_SELECTED_OWNERS;
import static io.github.xueluwu.android.organizeyourcloset.Closet.ClosetFilterActivity.INTENT_SELECTED_SEASONS;
import static io.github.xueluwu.android.organizeyourcloset.Closet.ClosetFilterActivity.INTENT_SELECTED_SIZES;

public class CalendarAddActivity extends AppCompatActivity {
    private DatabaseHandler db;
    private ArrayList<String> selectedKinds, selectedCategories, selectedSeasons;
    private ArrayList<String> selectedSizes, selectedBrands, selectedOwners;
    private TextView dateText;
    private Button searchButton, addButton;
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private String chosenDate;
    private Item chosenItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.add);
        setContentView(R.layout.activity_calendar_add);

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

        dateText = (TextView)findViewById(R.id.textCalendarAddChosenDate);
        searchButton = (Button)findViewById(R.id.buttonCalendarAddSearch);
        addButton = (Button)findViewById(R.id.buttonCalendarAddAdd);

        Intent intent = getIntent();
        chosenDate = intent.getStringExtra(CALENDAR_CHOSEN_DATE);
        selectedKinds = intent.getStringArrayListExtra(INTENT_SELECTED_KINDS);
        if (selectedKinds == null) {
            selectedKinds = new ArrayList<>();
        }
        selectedCategories = intent.getStringArrayListExtra(INTENT_SELECTED_CATEGORIES);
        if (selectedCategories == null) {
            selectedCategories = new ArrayList<>();
        }
        selectedSeasons = intent.getStringArrayListExtra(INTENT_SELECTED_SEASONS);
        if (selectedSeasons == null) {
            selectedSeasons = new ArrayList<>();
        }
        selectedSizes = intent.getStringArrayListExtra(INTENT_SELECTED_SIZES);
        if (selectedSizes == null) {
            selectedSizes = new ArrayList<>();
        }
        selectedBrands = intent.getStringArrayListExtra(INTENT_SELECTED_BRANDS);
        if (selectedBrands == null) {
            selectedBrands = new ArrayList<>();
        }
        selectedOwners = intent.getStringArrayListExtra(INTENT_SELECTED_OWNERS);
        if (selectedOwners == null) {
            selectedOwners = new ArrayList<>();
        }

        dateText.setText(chosenDate);
        db = new DatabaseHandler(this);
    }


    @Override
    public void onStart() {
        super.onStart();

        showRecords(
                selectedKinds,
                selectedCategories,
                selectedSeasons,
                selectedSizes,
                selectedBrands,
                selectedOwners
        );

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarAddActivity.this, ClosetFilterActivity.class);
                intent.putExtra(CALENDAR_CHOSEN_DATE, chosenDate);
                intent.putExtra(FILTER_FOR_CLOSET_OR_CALENDAR, "calendar");
                intent.putStringArrayListExtra(INTENT_SELECTED_KINDS, selectedKinds);
                intent.putStringArrayListExtra(INTENT_SELECTED_CATEGORIES, selectedCategories);
                intent.putStringArrayListExtra(INTENT_SELECTED_SEASONS, selectedSeasons);
                intent.putStringArrayListExtra(INTENT_SELECTED_SIZES, selectedSizes);
                intent.putStringArrayListExtra(INTENT_SELECTED_BRANDS, selectedBrands);
                intent.putStringArrayListExtra(INTENT_SELECTED_OWNERS, selectedOwners);
                startActivity(intent);
            }
        });

        // Add chosenItem with chosenDate in database TABLE_WORN
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chosenItem == null) {
                    Toast.makeText(
                            getBaseContext(), R.string.pleaseselectanitem, Toast.LENGTH_SHORT
                    ).show();
                } else {
                    if (db.checkWornItem(chosenItem.getID(), chosenDate)) {
                        Toast.makeText(
                                getBaseContext(),
                                R.string.imagealreadyselectedforthisdate,
                                Toast.LENGTH_SHORT
                        ).show();
                    } else {
                        db.addInWorn(chosenItem.getID(), chosenDate);
                        Intent intent = new Intent(
                                CalendarAddActivity.this, CalendarActivity.class
                        );
                        startActivity(intent);
//                    onBackPressed();
                    }
                }
            }
        });
    }

    private void showRecords(
            ArrayList<String> selectedKinds,
            ArrayList<String> selectedCategories,
            ArrayList<String> selectedSeasons,
            ArrayList<String> selectedSizes,
            ArrayList<String> selectedBrands,
            ArrayList<String> selectedOwners
    ){
        ArrayList<Item> items = db.getAllItems(
                selectedKinds,
                selectedCategories,
                selectedSeasons,
                selectedSizes,
                selectedBrands,
                selectedOwners
        );

        gridView = (GridView)findViewById(R.id.gridViewCalendarAdd);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, items);
        gridView.setAdapter(gridAdapter);
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
