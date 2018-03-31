package io.github.xueluwu.android.organizeyourcloset;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * MenuFragment
 */

public class MenuFragment extends ListFragment {

    private final static String DEBUG_TAG = "MenuFragment";

    int[] icons = new int[]{
            R.drawable.ic_statistics,
            R.drawable.ic_settings,
            R.drawable.ic_help,
            R.drawable.ic_feedback
    };

    ArrayList<HashMap<String, String>> data = new ArrayList<>();
    SimpleAdapter adapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(DEBUG_TAG, "onCreateView");
        getActivity().setTitle(R.string.menu);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(DEBUG_TAG, "onViewCreated");
        String[] menu_array = getResources().getStringArray(R.array.menu_array);
        HashMap<String, String> map;
        for (int i = 0; i < menu_array.length; i ++) {
            map = new HashMap<>();
            map.put("menu_text", menu_array[i]);
            map.put("menu_icon", Integer.toString(icons[i]));

            data.add(map);
        }
        // keys in map
        String[] from = {"menu_text", "menu_icon"};
        // id of Views
        int[] to = {R.id.menu_text, R.id.menu_icon};

        // adapter
        adapter = new SimpleAdapter(getActivity(), data, R.layout.menu_listview_layout, from, to);
        setListAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(DEBUG_TAG, "onStart");

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity().getBaseContext(), SubmenuActivity.class);
                intent.putExtra("position", position);
                Log.d(DEBUG_TAG, "position" + position);
                startActivity(intent);
            }
        });
    }
}