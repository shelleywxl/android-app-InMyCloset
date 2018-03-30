package com.example.android.organizeyourcloset;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.support.design.widget.NavigationView;

import java.util.Locale;

public class ClosetActivity extends NavDrawerActivity {

    private static final String DEBUG_TAG = "NavdrawerActivity";

    SharedPreferences mPrefs1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.screen_area, new ClosetFragment());
        ft.commit();
    }

}
