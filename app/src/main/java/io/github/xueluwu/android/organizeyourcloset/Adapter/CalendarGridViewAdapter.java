package io.github.xueluwu.android.organizeyourcloset.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import io.github.xueluwu.android.organizeyourcloset.R;

/**
 * CalendarGridViewAdapter
 * Calendar grid displaying dates
 */

public class CalendarGridViewAdapter extends ArrayAdapter<Date> {
    // for view inflation
    private LayoutInflater inflater;
    private List<Date> days;
    private Calendar currentDate;
    private HashSet<String> addedDateSet;

    private int mselectedPosition;

    public CalendarGridViewAdapter(Context context, ArrayList<Date> days, Calendar currentDate, HashSet<String> addedDateSet) {
        super(context, R.layout.control_calendar_day, days);
        this.days = days;
        this.currentDate = currentDate;
        this.addedDateSet = addedDateSet;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Date date = getItem(position);

        SimpleDateFormat dateFormatDay = new SimpleDateFormat("d");
        SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMM");
        SimpleDateFormat dateFormatYear = new SimpleDateFormat("yyyy");
        String formatDay = dateFormatDay.format(date);
        String formatMonth = dateFormatMonth.format(date);
        String formatYear = dateFormatYear.format(date);

        int day = date.getDate();
        int month = date.getMonth();
        int year = date.getYear();

        // today
        Date today = new Date();

        // inflate item if it does not exist yet
        if (view == null)
            view = inflater.inflate(R.layout.control_calendar_day, parent, false);

        // if this day has an event, specify event image
        view.setBackgroundResource(0);

        if (! addedDateSet.isEmpty()) {

            for (String addedDate : addedDateSet) {

                if (addedDate == null) {
                    continue;
                }

                String formatAddedMonth = addedDate.substring(0,3);
                String formatAddedDay = addedDate.substring(4,6);
                String formatAddedYear = addedDate.substring(8);

                if (formatDay.equals(formatAddedDay) &&
                        formatMonth.equals(formatAddedMonth) &&
                        formatYear.equals(formatAddedYear)) {
                    view.setBackgroundResource(R.drawable.ic_comment);
                    break;
                }
            }
        }

        // clear styling
        ((TextView)view).setTypeface(null, Typeface.NORMAL);
        ((TextView)view).setTextColor(Color.BLACK);

        if (month != today.getMonth() || year != today.getYear()) {
            // if this day is outside current month, grey it out
            ((TextView)view).setTextColor(getContext().getResources().getColor(R.color.colorGrey));
        }
        else if (day == today.getDate()) {
            // if it is today, set it to primary color/bold
            ((TextView)view).setTypeface(null, Typeface.BOLD);
            ((TextView)view).setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
        }

        // set text
        ((TextView)view).setText(String.valueOf(date.getDate()));

        // for highlighting selected day to colorPrimaryDarkest
        if (position == mselectedPosition)
            ((TextView)view).setTextColor(getContext().getResources().getColor(R.color.colorPrimaryDarkest));

        return view;
    }

    // for highlighting background of the selected day
    public void setSelectedPosition(int position) {
        mselectedPosition = position;
    }

}