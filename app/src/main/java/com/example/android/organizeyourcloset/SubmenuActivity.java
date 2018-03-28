package com.example.android.organizeyourcloset;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;


/**
 * SubmenuActivity
 * For MenuStatisticsFragment / MenuSettingsFragment / MenuHelpFragment / MenuFeedbackFragment
 */

public class SubmenuActivity extends AppCompatActivity {

    final static String DEBUG_TAG = "SubmenuActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);

        setContentView(R.layout.activity_submenu);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        //Log.d(DEBUG_TAG, "position" + position);

        if(position==0) {
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction ft=fragmentManager.beginTransaction();
            ft.replace(R.id.submenu_container, new MenuStatisticsFragment());
            ft.commit();
        }
        if(position==1) {
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction ft=fragmentManager.beginTransaction();
            ft.replace(R.id.submenu_container, new MenuSettingsFragment());
            ft.commit();
        }
        if(position==2) {
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction ft=fragmentManager.beginTransaction();
            ft.replace(R.id.submenu_container, new MenuHelpFragment());
            ft.commit();
        }
        if(position==3) {
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction ft=fragmentManager.beginTransaction();
            ft.replace(R.id.submenu_container, new MenuFeedbackFragment());
            ft.commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(SubmenuActivity.this, MenuActivity.class);
                        startActivity(i);
                    }
                }
        );
    }
}