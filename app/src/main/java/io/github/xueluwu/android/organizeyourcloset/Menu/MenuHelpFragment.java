package io.github.xueluwu.android.organizeyourcloset.Menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.xueluwu.android.organizeyourcloset.R;


/**
 * Menu - Help
 */

public class MenuHelpFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(R.string.help);
        return inflater.inflate(R.layout.fragment_menu_help,null);
    }
}
