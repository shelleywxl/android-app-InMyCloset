package io.github.xueluwu.android.organizeyourcloset.Menu;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.Locale;

import io.github.xueluwu.android.organizeyourcloset.R;


/**
 * Menu - Settings
 */

public class MenuSettingsFragment extends Fragment {
    @Nullable

    private String localeStr = null;
    private RadioButton eng,chi;
    private Button btnSave;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(R.string.settings);
        return inflater.inflate(R.layout.fragment_menu_settings,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eng = getActivity().findViewById(R.id.eng);
        chi = getActivity().findViewById(R.id.chi);
        btnSave = getActivity().findViewById(R.id.settings_btn);

        eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                setLocale("en");
                localeStr = "en";
            }
        });

        chi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                setLocale("zh");
                localeStr = "zh";
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (localeStr != null) {
                    setLocale(localeStr);
                    Toast.makeText(getActivity(), R.string.savedsuccessfully, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void setLocale(String lang)
    {
        Locale locale= new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        res.updateConfiguration(conf, dm);
    }

}
