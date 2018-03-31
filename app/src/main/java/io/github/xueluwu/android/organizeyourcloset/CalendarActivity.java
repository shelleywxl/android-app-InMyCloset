package io.github.xueluwu.android.organizeyourcloset;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


/**
 * CalendarActivity
 *
 */

public class CalendarActivity extends NavDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.screen_area, new CalendarFragment());
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ClosetActivity.class);
        startActivity(intent);
    }
}