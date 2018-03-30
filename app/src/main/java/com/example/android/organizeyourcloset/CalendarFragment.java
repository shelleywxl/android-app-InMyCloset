package com.example.android.organizeyourcloset;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;


/**
 * CalendarFragment
 */

public class CalendarFragment extends Fragment {

    private static final String DEBUG_TAG = "CalendarFragment";
    // current displayed month
    private Calendar currentDate = Calendar.getInstance(Locale.ENGLISH);

    // how many days to show, defaults to six weeks, 42 days
    private static final int DAYS_COUNT = 42;

    // default date format
    private static final String DATE_FORMAT = "MMM yyyy";

    // calendar view
    private ImageView btnPrev, btnNext;
    private TextView txtDate, txtChosenDate;
    private LinearLayout header;
    private GridView grid;

    private Button btnAdd, btnDelete;
    private GridView calendarImageGridview;

    private String chosenDate;
    private static Item chosenItem = null;
    private DatabaseHandler db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(R.string.calendar);
        View view = inflater.inflate(R.layout.fragment_calendar, null);

        header = (LinearLayout)view.findViewById(R.id.calendar_header);
        btnPrev = (ImageView)view.findViewById(R.id.calendar_prev_button);
        btnNext = (ImageView)view.findViewById(R.id.calendar_next_button);
        txtDate = (TextView)view.findViewById(R.id.calendar_date_display);
        grid = (GridView)view.findViewById(R.id.calendar_grid);

        txtChosenDate = (TextView)view.findViewById(R.id.chosen_date_tv);
        btnAdd = (Button)view.findViewById(R.id.calendar_add_btn);
        btnDelete = (Button)view.findViewById(R.id.calendar_delete_btn);
        calendarImageGridview = (GridView)view.findViewById(R.id.calendar_image_gridview);

        db = new DatabaseHandler(getContext());

        updateCalendar();
        assignClickHandlers();

        return view;
    }


    // buttons (btnPrev, btnNext, btnAdd, btnDelete)
    private void assignClickHandlers() {

        // subtract one month and refresh UI
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, -1);
                updateCalendar();
            }
        });

        // add one month and refresh UI
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, 1);
                updateCalendar();
            }
        });

        // add item to the chosen date
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CalendarAddActivity.class);
                intent.putExtra("chosenDate", chosenDate);
                startActivity(intent);
            }
        });

        // delete the chosen item/event from the database TABLE_WORN
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Item chosenItem2 = chosenItem;
                if (chosenItem == null) {
                    Toast.makeText(getActivity(), "Choose an item to delete from this date.", Toast.LENGTH_LONG).show();
                }
                else {
                    AlertDialog.Builder dialogbox = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.dailog));
                    dialogbox.setTitle(R.string.delete);
                    dialogbox.setMessage(R.string.areyousureyouwanttodeletethisitem);
                    dialogbox.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Log.d(DEBUG_TAG, "clickdeletedialog, chosenItem.getId: " + chosenItem2 + chosenDate);
                            db.deleteInWorn(chosenItem2.getID(), chosenDate);
                            showItemInGrid(chosenDate);
                            updateCalendar();
                        }
                    });
                    dialogbox.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialogbox.show();

                    // update the gridview
                    chosenItem = null;

                }
            }
        });
    }


    // Display dates in calendar grid
    public void updateCalendar() {
        ArrayList<Date> cells = new ArrayList<>();
        HashSet<String> addedDateSet = db.addedDateSet();

        Calendar calendar = (Calendar) currentDate.clone();

        // determine the cell for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        // move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        // fill cells
        while (cells.size() < DAYS_COUNT) {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // update title
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        txtDate.setText(sdf.format(currentDate.getTime()));

        // update grid, showing marks for dates with clothes
        final CalendarGridViewAdapter calendarGridviewAdapter = new CalendarGridViewAdapter(getContext(), cells, currentDate, addedDateSet);
        grid.setAdapter(calendarGridviewAdapter);

        // click a date in the calendar grid
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Date date = (Date)parent.getItemAtPosition(position);
                chosenDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(date);
                txtChosenDate.setText(chosenDate);
                showItemInGrid(chosenDate);

                calendarGridviewAdapter.setSelectedPosition(position);
                calendarGridviewAdapter.notifyDataSetChanged();
            }
        });
    }


    // Retrieve date from database TABLE_WORN and set to the calendarImageGridview
    private void showItemInGrid(String chosenDate) {
        ArrayList<Item> itemArray = new ArrayList<Item>();
        itemArray.addAll(db.getWornItems(chosenDate));
        final GridViewAdapter gridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, itemArray);
        calendarImageGridview.setAdapter(gridAdapter);

        // click an item in the image gridview
        calendarImageGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                btnDelete.setEnabled(true);
                chosenItem = (Item) parent.getItemAtPosition(position);

                gridAdapter.setSelectedPosition(position);
                gridAdapter.notifyDataSetChanged();
            }
        });
    }

}
