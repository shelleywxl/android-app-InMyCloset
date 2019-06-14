package io.github.xueluwu.android.organizeyourcloset.Closet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import io.github.xueluwu.android.organizeyourcloset.R;

/**
 * ClosetItemActivity
 * For ClosetAddItemFragment / ClosetShowItemFragment / ClosetEditItemFragment
 */

public class ClosetItemActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closet_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(ClosetItemActivity.this, ClosetActivity.class);
                        startActivity(i);
                    }
                }
        );

        Intent intent = getIntent();
        String add_or_edit = intent.getStringExtra("add_show_edit");

        if (add_or_edit.equals("add")) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.item_fragment_container, new ClosetAddItemFragment());
            ft.commit();
        } else if (add_or_edit.equals("show")) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.item_fragment_container, new ClosetShowItemFragment());
            ft.commit();
        } else if (add_or_edit.equals("edit")) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.item_fragment_container, new ClosetEditItemFragment());
            ft.commit();
        }
    }
}
