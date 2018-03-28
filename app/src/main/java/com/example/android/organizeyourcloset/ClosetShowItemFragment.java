package com.example.android.organizeyourcloset;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/*
    ClosetShowItemFragment: item details
    shows after click the GridView images in ClosetFragment
*/

public class ClosetShowItemFragment extends Fragment {

    private static final String DEBUG_TAG = "ShowItemFragment";

    private DatabaseHandler db;
    private ImageView image_iv;
    private TextView kind_tv, category_tv, price_tv, season_tv;
    private Button ok_btn, edit_btn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_closet_show_item, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);

        // Intent "item_id" is passed from ClosetFragment
        // Find item attributes in the database according to the unique id
        // set the content of TextViews

        Intent intent = getActivity().getIntent();
        final Integer item_id = intent.getIntExtra("item_id", -1);

        Log.d(DEBUG_TAG, "Item_id passed by ClosetFrag: " + item_id);

        db = new DatabaseHandler(getActivity());
        Item item = db.getOneItem(item_id);

        image_iv = (ImageView)getActivity().findViewById(R.id.iv_showimg);
        kind_tv = (TextView)getActivity().findViewById(R.id.tv_showkind);
        category_tv = (TextView)getActivity().findViewById(R.id.tv_showcategory);
        price_tv = (TextView)getActivity().findViewById(R.id.tv_showprice);
        season_tv = (TextView)getActivity().findViewById(R.id.tv_showseason);

        byte[] b = item.getImage();
        image_iv.setImageBitmap(BitmapFactory.decodeByteArray(b, 0, b.length));
        kind_tv.setText(item.getKind());
        category_tv.setText(item.getCategory());
        price_tv.setText(String.valueOf(item.getPrice()));
        season_tv.setText(item.getSeason());


        // for "OK" button, go back to ClosetFragment
        ok_btn = (Button)getActivity().findViewById(R.id.btn_showok);
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent1 = new Intent(getActivity(), NavdrawerActivity.class);
                //startActivity(intent1);
                getActivity().onBackPressed();
                // not using startActivity, so that the ClosetFragment will remember the chosen kind/category
            }
        });


        // for "EDIT" button, put EditItemFragment in ItemActivity
        edit_btn = (Button)getActivity().findViewById(R.id.btn_showedit);
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getActivity(), ClosetItemActivity.class);
                intent2.putExtra("item_id", item_id);
                intent2.putExtra("add_show_edit", "edit");
                startActivity(intent2);
            }
        });

    }

}