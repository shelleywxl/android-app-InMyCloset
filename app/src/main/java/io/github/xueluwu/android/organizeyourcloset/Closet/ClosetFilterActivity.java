package io.github.xueluwu.android.organizeyourcloset.Closet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.xueluwu.android.organizeyourcloset.Calendar.CalendarAddActivity;
import io.github.xueluwu.android.organizeyourcloset.DatabaseHandler;
import io.github.xueluwu.android.organizeyourcloset.R;

public class ClosetFilterActivity extends AppCompatActivity {
    public static final String FILTER_FOR_CLOSET_OR_CALENDAR = "intentForClosetOrCalendar";
    public static final String CALENDAR_CHOSEN_DATE = "calendarChosenDate";
    public static final String INTENT_SELECTED_KINDS = "intentSelectedKinds";
    public static final String INTENT_SELECTED_CATEGORIES = "intentSelectedCategories";
    public static final String INTENT_SELECTED_SEASONS = "intentSelectedSeasons";
    public static final String INTENT_SELECTED_SIZES = "intentSelectedSizes";
    public static final String INTENT_SELECTED_BRANDS = "intentSelectedBrands";
    public static final String INTENT_SELECTED_OWNERS = "intentSelectedOwners";
    private ArrayList<String> selectedKinds, selectedCategories, selectedSeasons;
    private ArrayList<String> selectedSizes, selectedBrands, selectedOwners;
    private Button kindFilterButton, categoryFilterButton, seasonFilterButton;
    private Button sizeFilterButton, brandFilterButton, ownerFilterButton;
    private TextView kindFilterText, categoryFilterText, seasonFilterText;
    private TextView sizeFilterText, brandFilterText, ownerFilterText;
    private DatabaseHandler db;
    private String forClosetOrCalendar, calendarChosenDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closet_filter);
        setTitle("Search");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(ClosetFilterActivity.this, ClosetActivity.class);
                        startActivity(i);
                    }
                }
        );

        db = new DatabaseHandler(this);

        Intent intent = getIntent();
        forClosetOrCalendar = intent.getStringExtra(FILTER_FOR_CLOSET_OR_CALENDAR);
        if (forClosetOrCalendar.equals("calendar")) {
            calendarChosenDate = intent.getStringExtra(CALENDAR_CHOSEN_DATE);
        }
        selectedKinds = intent.getStringArrayListExtra(INTENT_SELECTED_KINDS);
        selectedCategories = intent.getStringArrayListExtra(INTENT_SELECTED_CATEGORIES);
        selectedSeasons = intent.getStringArrayListExtra(INTENT_SELECTED_SEASONS);
        selectedSizes = intent.getStringArrayListExtra(INTENT_SELECTED_SIZES);
        selectedBrands = intent.getStringArrayListExtra(INTENT_SELECTED_BRANDS);
        selectedOwners = intent.getStringArrayListExtra(INTENT_SELECTED_OWNERS);

        initView();
        setClickListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filter_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ac_done:
                Intent intent;
                if (forClosetOrCalendar.equals("closet")) {
                    intent = new Intent(getApplicationContext(), ClosetActivity.class);
                } else {
                    // forClosetOrCalendar.equals("calendar")
                    intent = new Intent(getApplicationContext(), CalendarAddActivity.class);
                    intent.putExtra(CALENDAR_CHOSEN_DATE, calendarChosenDate);
                }
                intent.putStringArrayListExtra(INTENT_SELECTED_KINDS, selectedKinds);
                intent.putStringArrayListExtra(INTENT_SELECTED_CATEGORIES, selectedCategories);
                intent.putStringArrayListExtra(INTENT_SELECTED_SEASONS, selectedSeasons);
                intent.putStringArrayListExtra(INTENT_SELECTED_SIZES, selectedSizes);
                intent.putStringArrayListExtra(INTENT_SELECTED_BRANDS, selectedBrands);
                intent.putStringArrayListExtra(INTENT_SELECTED_OWNERS, selectedOwners);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initView() {
        kindFilterButton = (Button) findViewById(R.id.buttonKindFilter);
        categoryFilterButton = (Button) findViewById(R.id.buttonCategoryFilter);
        seasonFilterButton = (Button) findViewById(R.id.buttonSeasonFilter);
        sizeFilterButton = (Button) findViewById(R.id.buttonSizeFilter);
        brandFilterButton = (Button) findViewById(R.id.buttonBrandFilter);
        ownerFilterButton = (Button) findViewById(R.id.buttonOwnerFilter);

        kindFilterText = (TextView) findViewById(R.id.textKindFilter);
        categoryFilterText = (TextView) findViewById(R.id.textCategoryFilter);
        seasonFilterText = (TextView) findViewById(R.id.textSeasonFilter);
        sizeFilterText = (TextView) findViewById(R.id.textSizeFilter);
        brandFilterText = (TextView) findViewById(R.id.textBrandFilter);
        ownerFilterText = (TextView) findViewById(R.id.textOwnerFilter);

        showFilterText(selectedKinds, kindFilterText);
        showFilterText(selectedCategories, categoryFilterText);
        showFilterText(selectedSeasons, seasonFilterText);
        showFilterText(selectedSizes, sizeFilterText);
        showFilterText(selectedBrands, brandFilterText);
        showFilterText(selectedOwners, ownerFilterText);
    }

    private void setClickListener() {
        setFilterClickListener(kindFilterButton, selectedKinds, R.string.kind, kindFilterText);
        setFilterClickListener(categoryFilterButton, selectedCategories, R.string.category, categoryFilterText);
        setFilterClickListener(seasonFilterButton, selectedSeasons, R.string.season, seasonFilterText);
        setFilterClickListener(sizeFilterButton, selectedSizes, R.string.size, sizeFilterText);
        setFilterClickListener(brandFilterButton, selectedBrands, R.string.brand, brandFilterText);
        setFilterClickListener(ownerFilterButton, selectedOwners, R.string.owner, ownerFilterText);
    }

    private void setFilterClickListener(
            final Button button,
            final ArrayList<String> selectedList,
            final int dialogTitle,
            final TextView filterText
    ) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] existedArray;
                if (button == kindFilterButton) {
                    existedArray = db.getExistedKinds();
                } else if (button == categoryFilterButton) {
                    existedArray = db.getExistedCategories();
                } else if (button == seasonFilterButton) {
                    existedArray = db.getExistedSeasons();
                } else if (button == sizeFilterButton) {
                    existedArray = db.getExistedSizes();
                } else if (button == brandFilterButton) {
                    existedArray = db.getExistedBrands();
                } else if (button == ownerFilterButton) {
                    existedArray = db.getExistedOwners();
                } else {
                    existedArray = new String[0];
                }
                final boolean[] checkedArray = new boolean[existedArray.length];
                for (int i = 0; i < existedArray.length; i++) {
                    if (selectedList.contains(existedArray[i])) {
                        checkedArray[i] = true;
                    }
                }
                if (existedArray.length > 0) {
                    showMultiChoiceDialog(dialogTitle, existedArray, checkedArray, selectedList, filterText);
                }
            }
        });
    }

    private void showMultiChoiceDialog(
            int title,
            final String[] optionArray,
            final boolean[] checkedArray,
            final ArrayList<String> selectedList,
            final TextView filterTextView
    ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMultiChoiceItems(optionArray, checkedArray, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checkedArray[which] = isChecked;

                        if (isChecked) {
                            selectedList.add(optionArray[which]);
                        } else if (selectedList.contains(optionArray[which])) {
                            selectedList.remove(optionArray[which]);
                        }
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        showFilterText(selectedList, filterTextView);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showFilterText(ArrayList<String> selectedList, TextView filterTextView) {
        String filterText = "";
        for (String selected : selectedList) {
            filterText += ", " + selected;
        }
        if (!filterText.isEmpty()) {
            filterText = filterText.substring(2);
        }
        filterTextView.setText(filterText);
    }
}
