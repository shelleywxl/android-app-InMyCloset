package io.github.xueluwu.android.organizeyourcloset;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import java.util.Locale;

import io.github.xueluwu.android.organizeyourcloset.Closet.ClosetActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnstart;
    private RadioButton eng,chi;
    public static String lang="en";
    private SharedPreferences mPrefs;
    final String welcomeScreenShownPref = "welcomeScreenShown";

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        setLocale(lang);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findviewByid();
        setonclicklistener();
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
