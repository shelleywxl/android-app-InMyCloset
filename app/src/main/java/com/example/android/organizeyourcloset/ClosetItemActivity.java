package com.example.android.organizeyourcloset;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


/**
 * ClosetItemActivity
 * For ClosetAddItemFragment / ClosetShowItemFragment / ClosetEditItemFragment
 */

public class ClosetItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closet_item);

        Intent intent = getIntent();
        String add_or_edit = intent.getStringExtra("add_show_edit");

        if (add_or_edit.equals("add")) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.item_fragment_container, new ClosetAddItemFragment());
            ft.commit();
        }

        if (add_or_edit.equals("show")) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.item_fragment_container, new ClosetShowItemFragment());
            ft.commit();
        }

        if (add_or_edit.equals("edit")) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.item_fragment_container, new ClosetEditItemFragment());
            ft.commit();
        }
    }
}
