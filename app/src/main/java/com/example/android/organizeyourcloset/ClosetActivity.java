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

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//        mPrefs1 = PreferenceManager.getDefaultSharedPreferences(this);
//        String languageToLoad = mPrefs1.getString("languagePref", Locale.getDefault().getLanguage());
//        Locale locale = new Locale(languageToLoad);
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.locale = locale;
//        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
//        setContentView(R.layout.activity_main);
//        finish();
//        startActivity(getIntent());
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
//        String lang = PreferenceManager.getDefaultSharedPreferences(this).getString("languagePref", "default");
//        Configuration config = getResources().getConfiguration();
//        if( lang.equals("default") ) lang = Locale.getDefault().getLanguage();
//        config.locale = new Locale(lang);
//        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
//    }
//
//    public void setLocale(String lang) {
//        Locale locale = new Locale(lang);
//        Configuration configuration=getBaseContext().getResources().getConfiguration();
//        configuration.locale=locale;
//        Resources res = getResources();
//        DisplayMetrics dm = res.getDisplayMetrics();
//        Configuration conf = res.getConfiguration();
//        conf.locale = locale;
//        Configuration systemConf= Resources.getSystem().getConfiguration();
//        systemConf.locale=locale;
//        Locale.setDefault(locale);
//        res.updateConfiguration(conf, dm);
//    }
}
