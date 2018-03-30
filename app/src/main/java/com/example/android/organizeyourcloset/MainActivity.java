package com.example.android.organizeyourcloset;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnstart;
    private RadioButton eng,chi;
    public static String lang="en";
    private SharedPreferences mPrefs;
    final String welcomeScreenShownPref = "welcomeScreenShown";

    public MainActivity() {
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {

        setLocale(lang);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findviewByid();
        setonclicklistener();

        // One time welcome screen
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean welcomeScreenShown = mPrefs.getBoolean(welcomeScreenShownPref, false);

        if (welcomeScreenShown) {
            Intent in = new Intent(MainActivity.this, ClosetActivity.class);
            //           in.putExtra("lang",lang);
            startActivity(in);
        } else {

            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putBoolean(welcomeScreenShownPref, true);
            editor.commit();
        }
    }


    private void findviewByid() {
        btnstart = (Button) findViewById(R.id.getStarted);
        eng = (RadioButton) findViewById(R.id.eng);
        chi = (RadioButton) findViewById(R.id.chi);
    }

    private void setonclicklistener() {
        btnstart.setOnClickListener(this);
        eng.setOnClickListener(this);
        chi.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.getStarted:
                Intent intent = new Intent(MainActivity.this, ClosetActivity.class);
                startActivity(intent);
                break;

            case R.id.eng:
                setLocale("en");
                lang="en";
                break;

            case R.id.chi:
                setLocale("zh");
                lang="en";
                break;
        }
    }

    public void setLocale(String lang) {

        Locale locale = new Locale(lang);

        Configuration configuration=getBaseContext().getResources().getConfiguration();
        configuration.locale=locale;
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        Configuration systemConf= Resources.getSystem().getConfiguration();
        systemConf.locale=locale;
        Locale.setDefault(locale);
        res.updateConfiguration(conf, dm);

    }
}
