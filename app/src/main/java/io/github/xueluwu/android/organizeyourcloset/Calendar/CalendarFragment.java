package io.github.xueluwu.android.organizeyourcloset.Calendar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import java.util.Locale;

import io.github.xueluwu.android.organizeyourcloset.Adapter.CalendarGridViewAdapter;
import io.github.xueluwu.android.organizeyourcloset.Adapter.GridViewAdapter;
import io.github.xueluwu.android.organizeyourcloset.DatabaseHandler;
import io.github.xueluwu.android.organizeyourcloset.Item;
import io.github.xueluwu.android.organizeyourcloset.R;

import static io.github.xueluwu.android.organizeyourcloset.Closet.ClosetFilterActivity.CALENDAR_CHOSEN_DATE;

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

    private ImageView calendarPrevButton, calendarNextButton;
    private TextView dateText, chosenDateText;
    private LinearLayout header;
    private GridView calendarGridView, imageGridView;
    private Button addButton, deleteButton;
    private String chosenDate;
    private static Item chosenItem = null;
    private DatabaseHandler db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(io.github.xueluwu.android.organizeyourcloset.R.string.calendar);
        View view = inflater.inflate(io.github.xueluwu.android.organizeyourcloset.R.layout.fragment_calendar, null);

        header = (LinearLayout)view.findViewById(R.id.calendarHeader);
        calendarPrevButton = (ImageView)view.findViewById(R.id.calendarPrevButton);
        calendarNextButton = (ImageView)view.findViewById(R.id.calendarNextButton);
        dateText = (TextView)view.findViewById(R.id.calendarDateDisplay);
        calendarGridView = (GridView)view.findViewById(R.id.calendarGrid);
        chosenDateText = (TextView)view.findViewById(R.id.textCalendarChosenDate);
        addButton = (Button)view.findViewById(R.id.buttonCalendarAdd);
        deleteButton = (Button)view.findViewById(R.id.buttonCalendarDelete);
        imageGridView = (GridView)view.findViewById(R.id.gridViewCalendarImage);

        db = new DatabaseHandler(getContext());

        updateCalendar();
        assignClickHandlers();

        return view;
    }

    private void assignClickHandlers() {
        // subtract one month and refresh UI
        calendarPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, -1);
                updateCalendar();
            }
        });

        // add one month and refresh UI
        calendarNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, 1);
                updateCalendar();
            }
        });

        // add item to the chosen date
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chosenDate == null) {
                    Toast.makeText(
                            getActivity(), getString(R.string.selectadate), Toast.LENGTH_LONG
                    ).show();
                } else {
                    Intent intent = new Intent(getActivity(), CalendarAddActivity.class);
                    intent.putExtra(CALENDAR_CHOSEN_DATE, chosenDate);
                    startActivity(intent);
                }
            }
        });

        // delete the chosen item/event from the database TABLE_WORN
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Item chosenItem2 = chosenItem;
                if (chosenItem == null) {
                    Toast.makeText(
                            getActivity(),
                            getResources().getString(R.string.selectanitemtodeletefromthisdate),
                            Toast.LENGTH_LONG
                    ).show();
                }
                else {
                    AlertDialog.Builder dialogbox = new AlertDialog.Builder(
                            new ContextThemeWrapper(getActivity(), R.style.dailog)
                    );
                    dialogbox.setTitle(R.string.delete);
                    dialogbox.setMessage(R.string.areyousureyouwanttodeletethisitem);
                    dialogbox.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
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
        dateText.setText(sdf.format(currentDate.getTime()));

        // update grid, showing marks for dates with clothes
        final CalendarGridViewAdapter calendarGridviewAdapter = new CalendarGridViewAdapter(
                getContext(), cells, currentDate, addedDateSet
        );
        calendarGridView.setAdapter(calendarGridviewAdapter);
        // click a date in the calendar grid
        calendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Date date = (Date)parent.getItemAtPosition(position);
                chosenDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(date);
                chosenDateText.setText(chosenDate);
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
        final GridViewAdapter gridAdapter = new GridViewAdapter(
                getActivity(), R.layout.grid_item_layout, itemArray
        );
        imageGridView.setAdapter(gridAdapter);
        imageGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item currItem = (Item) parent.getItemAtPosition(position);
                if (currItem == chosenItem) {
                    // Uncheck this item
                    deleteButton.setVisibility(View.GONE);
                    chosenItem = null;
                    gridAdapter.setUnselectedPosition();
                    gridAdapter.notifyDataSetChanged();
                } else {
                    // Check this item
                    deleteButton.setVisibility(View.VISIBLE);
                    chosenItem = (Item) parent.getItemAtPosition(position);
                    gridAdapter.setSelectedPosition(position);
                    gridAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
