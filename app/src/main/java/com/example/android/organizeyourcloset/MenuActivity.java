package com.example.android.organizeyourcloset;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;


/**
 * MenuActivity
 */

public class MenuActivity extends NavDrawerActivity {

    private static final String DEBUG_TAG = "MenuActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.screen_area, new MenuFragment());
        ft.commit();
    }

}
