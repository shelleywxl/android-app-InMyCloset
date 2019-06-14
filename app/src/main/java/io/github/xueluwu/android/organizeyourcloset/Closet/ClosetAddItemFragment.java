package io.github.xueluwu.android.organizeyourcloset.Closet;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayout;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import io.github.xueluwu.android.organizeyourcloset.DatabaseHandler;
import io.github.xueluwu.android.organizeyourcloset.Item;
import io.github.xueluwu.android.organizeyourcloset.R;

/**
 * ClosetAddItemFragment: to add an item to the database TABLE_ITEMS
 * shows after taking/choosing pictures
 * attached to ClosetItemActivity
 */

public class ClosetAddItemFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private DatabaseHandler db;
    private byte[] image;
    private Spinner kindSpinner, categorySpinner, seasonSpinner;
    private EditText priceText, sizeText, brandText, boughtDateText, ownerText;
    private FlexboxLayout sizeFlexboxLayout, brandFlexboxLayout, ownerFlexboxLayout;
    private Button addButton;
    private String kind, category, season, size, brand, owner, boughtDate, worn;
    private Integer price;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
    ) {
        getActivity().setTitle("Add");
        return inflater.inflate(R.layout.fragment_closet_add_item, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        priceText = getActivity().findViewById(R.id.textAddPrice);
        sizeText = getActivity().findViewById(R.id.textAddSize);
        brandText = getActivity().findViewById(R.id.textAddBrand);
        boughtDateText = getActivity().findViewById(R.id.textAddBoughtDate);
        ownerText = getActivity().findViewById(R.id.textAddOwner);

        addButton = getActivity().findViewById(R.id.buttonAdd);

        kindSpinner = getActivity().findViewById(R.id.spinnerAddkind);
        categorySpinner = getActivity().findViewById(R.id.spinnerAddcategory);
        ArrayAdapter kindAdapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.Kind_no_all, android.R.layout.simple_spinner_item
        );
        kindAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kindSpinner.setAdapter(kindAdapter);
        kindSpinner.setOnItemSelectedListener(this);

        seasonSpinner = getActivity().findViewById(R.id.spinnerAddseason);
        ArrayAdapter seasonAdapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.Season, android.R.layout.simple_spinner_item
        );
        seasonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seasonSpinner.setAdapter(seasonAdapter);

        sizeFlexboxLayout = getActivity().findViewById(R.id.flexboxLayoutSize);
        sizeFlexboxLayout.setFlexDirection(FlexDirection.ROW);
        brandFlexboxLayout = getActivity().findViewById(R.id.flexboxLayoutBrand);
        brandFlexboxLayout.setFlexDirection(FlexDirection.ROW);
        ownerFlexboxLayout = getActivity().findViewById(R.id.flexboxLayoutOwner);
        ownerFlexboxLayout.setFlexDirection(FlexDirection.ROW);
    }

    // Spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedKind = String.valueOf(kindSpinner.getSelectedItem());
        if(selectedKind.contentEquals("Top") || selectedKind.contentEquals("上装")) {
            ArrayAdapter categoryAdapter = ArrayAdapter.createFromResource(
                    getActivity(), R.array.Top_no_all, android.R.layout.simple_spinner_item
            );
            categorySpinner.setAdapter(categoryAdapter);
        } else if(selectedKind.contentEquals("Bottom") || selectedKind.contentEquals("下装")){
            ArrayAdapter categoryAdapter = ArrayAdapter.createFromResource(
                    getActivity(), R.array.Bottom_no_all, android.R.layout.simple_spinner_item
            );
            categorySpinner.setAdapter(categoryAdapter);
        } else if(selectedKind.contentEquals("Footwear") || selectedKind.contentEquals("鞋类")){
            ArrayAdapter categoryAdapter = ArrayAdapter.createFromResource(
                    getActivity(), R.array.Footwear_no_all, android.R.layout.simple_spinner_item
            );
            categorySpinner.setAdapter(categoryAdapter);
        } else if(selectedKind.contentEquals("Accessories") || selectedKind.contentEquals("配饰")){
            ArrayAdapter categoryAdapter = ArrayAdapter.createFromResource(
                    getActivity(), R.array.Accessories_no_all, android.R.layout.simple_spinner_item
            );
            categorySpinner.setAdapter(categoryAdapter);
        }
    }

    // Spinner
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);

        db = new DatabaseHandler(getActivity());

        byte[] byteArray = getActivity().getIntent().getByteArrayExtra("image");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        image = profileImage(bmp);

        String[] existedSizes = db.getExistedSizes();
        addExistedValueButtons(existedSizes, sizeFlexboxLayout, sizeText);
        setTextListener(sizeText, sizeFlexboxLayout);
        String[] existedBrands = db.getExistedBrands();
        addExistedValueButtons(existedBrands, brandFlexboxLayout, brandText);
        setTextListener(brandText, brandFlexboxLayout);
        String[] existedOwners = db.getExistedOwners();
        addExistedValueButtons(existedOwners, ownerFlexboxLayout, ownerText);
        setTextListener(ownerText, ownerFlexboxLayout);

        boughtDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                DatePickerDialog pickerDialog = new DatePickerDialog(
                        getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        boughtDateText.setText((month + 1) + "/" + dayOfMonth + "/" + year);
                    }
                }, year, month, day);
                pickerDialog.show();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addItem();
            }
        });
    }

    //Convert bitmap to bytes
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private byte[] profileImage(Bitmap b){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();
    }

    private void addExistedValueButtons(
            final String[] existedArray, FlexboxLayout flexboxLayout, final EditText editText
    ) {
        final Button[] buttons = new Button[existedArray.length];
        for (int i = 0; i < existedArray.length; i++) {
            buttons[i] = new Button(getActivity());
            final String existedValue = existedArray[i];
            buttons[i].setText(existedValue);
            buttons[i].setBackgroundResource(R.drawable.bordered_button_enabled);
            buttons[i].setTextAppearance(getActivity(), R.style.BorderedButton);
            buttons[i].setTextSize(15);
            buttons[i].setMinHeight(25);
            buttons[i].setMinimumHeight(25);
            buttons[i].setMinWidth(25);
            buttons[i].setMinimumWidth(25);
            buttons[i].setPadding(10,5,10,5);
            flexboxLayout.addView(buttons[i]);
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText.setText(existedValue);
                }
            });
        }
    }

    public void addItem() {
        kind = kindSpinner.getSelectedItem().toString();
        category = categorySpinner.getSelectedItem().toString();
        price = 0;
        if (priceText.getText().length() != 0) {
            price = Integer.parseInt(priceText.getText().toString());
        }
        season = seasonSpinner.getSelectedItem().toString();
        size = sizeText.getText().toString();
        brand = brandText.getText().toString();
        owner = ownerText.getText().toString();
        boughtDate = boughtDateText.getText().toString();

        if (db.addItem(new Item(image, kind, category, price, season, size, brand, owner, boughtDate))) {
            Toast.makeText(getActivity().getApplicationContext(), R.string.savedsuccessfully, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), R.string.notsaved, Toast.LENGTH_LONG).show();
        }

        Intent intent = new Intent(getActivity(), ClosetActivity.class);
        startActivity(intent);
    }

    private void setTextListener(final EditText editText, final FlexboxLayout flexboxLayout) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    flexboxLayout.setVisibility(View.GONE);
                } else {
                    flexboxLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flexboxLayout.setVisibility(View.VISIBLE);
            }
        });
    }
}