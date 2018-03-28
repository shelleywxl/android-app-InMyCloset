package com.example.android.organizeyourcloset;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;


/**
 * ClosetEditItemFragment: to edit item
 * shows after click "edit" button in ClosetShowItemFragment
 */

public class ClosetEditItemFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String DEBUG_TAG = "ClosetEditItemFragment";

    private DatabaseHandler db;
    private ImageView image_iv;
    private EditText price_et;
    private Spinner kind_sp, category_sp, season_sp;
    private Button save_btn, delete_btn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_closet_edit_item, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        image_iv = getActivity().findViewById(R.id.iv_showimg);
        kind_sp =  getActivity().findViewById(R.id.sp_editkind);
        category_sp = getActivity().findViewById(R.id.sp_editcategory);
        price_et = getActivity().findViewById(R.id.et_editprice);
        season_sp = getActivity().findViewById(R.id.sp_editseason);

        // adapter for season spinner
        ArrayAdapter adapter_se = ArrayAdapter.createFromResource(getActivity(), R.array.Season, android.R.layout.simple_spinner_item);
        adapter_se.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        season_sp.setAdapter(adapter_se);

        // adapter for kind spinner
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.Kind_no_all, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kind_sp.setAdapter(adapter1);
        kind_sp.setOnItemSelectedListener(this);

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String sp1= String.valueOf(kind_sp.getSelectedItem());
        if(sp1.contentEquals("Top") || sp1.contentEquals("上装")) {
            ArrayAdapter adapter2 = ArrayAdapter.createFromResource(getActivity(),R.array.Top_no_all, android.R.layout.simple_spinner_item);
            category_sp.setAdapter(adapter2);
        }
        if(sp1.contentEquals("Bottom") || sp1.contentEquals("下装")){
            ArrayAdapter adapter2 = ArrayAdapter.createFromResource(getActivity(),R.array.Bottom_no_all, android.R.layout.simple_spinner_item);
            category_sp.setAdapter(adapter2);
        }
        if(sp1.contentEquals("Footwear") || sp1.contentEquals("鞋类")){
            ArrayAdapter adapter2 = ArrayAdapter.createFromResource(getActivity(),R.array.Footwear_no_all, android.R.layout.simple_spinner_item);
            category_sp.setAdapter(adapter2);
        }
        if(sp1.contentEquals("Accessories") || sp1.contentEquals("配饰")){
            ArrayAdapter adapter2 = ArrayAdapter.createFromResource(getActivity(),R.array.Accessories_no_all, android.R.layout.simple_spinner_item);
            category_sp.setAdapter(adapter2);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Do nothing
    }


    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);


        // item_id is passed by intent from ShowItemFragment
        // get item's attributes in the database
        // set the spinners/edittexts to show them as the default value

        Intent intent = getActivity().getIntent();
        final Integer item_id = intent.getIntExtra("item_id", -1);
        Log.d(DEBUG_TAG, "item_id passed by ShowItemFrag: " + item_id);

        db = new DatabaseHandler(getActivity());
        final Item item = db.getOneItem(item_id);

        final byte[] b = item.getImage();
        image_iv.setImageBitmap(BitmapFactory.decodeByteArray(b, 0, b.length));


        // set default values for 4 view widgets (as in the database)

        ArrayAdapter adapter1 = (ArrayAdapter) kind_sp.getAdapter();
        int pos1 = adapter1.getPosition(item.getKind());
        kind_sp.setSelection(pos1);

        ArrayAdapter adapter2 = (ArrayAdapter) kind_sp.getAdapter();
        int pos2 = adapter2.getPosition(item.getCategory());
        category_sp.setSelection(pos2);

        price_et.setText(String.valueOf(item.getPrice()));

        ArrayAdapter adapter4 = (ArrayAdapter) kind_sp.getAdapter();
        int pos4 = adapter4.getPosition(item.getSeason());
        kind_sp.setSelection(pos4);


        // for "SAVE" button, update the database, then go back to ClosetFragment

        save_btn = getActivity().findViewById(R.id.btn_editsave);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer new_price = 0;
                if (price_et.getText().length() != 0) {
                    Integer.parseInt(price_et.getText().toString());
                }
                Item updated_item = new Item(
                        item_id,
                        b,
                        kind_sp.getSelectedItem().toString(),
                        category_sp.getSelectedItem().toString(),
                        new_price,
                        season_sp.getSelectedItem().toString());

                db.updateItem(updated_item, item_id);

                Log.d(DEBUG_TAG, "kind after updating: " + item.getKind());
                Intent intent1 = new Intent(getActivity(), ClosetActivity.class);
                startActivity(intent1);
            }
        });


        // for "DELETE" button, show AlertDialog, yes -> delete in the database, then go back to ClosetFragment
        delete_btn = getActivity().findViewById(R.id.btn_editdelete);
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.deleteitem)
                        .setMessage(R.string.areyousureyouwanttodeletethisitem)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                db.deleteItem(item_id);
                                Intent intent2 = new Intent(getActivity(), ClosetActivity.class);
                                startActivity(intent2);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });
    }
}