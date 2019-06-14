package io.github.xueluwu.android.organizeyourcloset.Closet;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayout;

import java.util.Calendar;

import io.github.xueluwu.android.organizeyourcloset.DatabaseHandler;
import io.github.xueluwu.android.organizeyourcloset.Item;
import io.github.xueluwu.android.organizeyourcloset.R;

/**
 * ClosetEditItemFragment: to edit item
 * shows after click "edit" button in ClosetShowItemFragment
 */

public class ClosetEditItemFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private DatabaseHandler db;
    private ImageView image;
    private EditText priceText, sizeText, brandText, boughtDateText, ownerText;
    private Spinner kindSpinner, categorySpinner, seasonSpinner;
    private Button saveButton, deleteButton;
    private FlexboxLayout sizeFlexboxLayout, brandFlexboxLayout, ownerFlexboxLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        getActivity().setTitle("Edit");
        return inflater.inflate(R.layout.fragment_closet_edit_item, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        image = getActivity().findViewById(R.id.image);
        kindSpinner =  getActivity().findViewById(R.id.spinnerEditKind);
        categorySpinner = getActivity().findViewById(R.id.spinnerEditCategory);
        priceText = getActivity().findViewById(R.id.textEditPrice);
        seasonSpinner = getActivity().findViewById(R.id.spinnerEditSeason);
        sizeText = getActivity().findViewById(R.id.textEditSize);
        brandText = getActivity().findViewById(R.id.textEditBrand);
        boughtDateText = getActivity().findViewById(R.id.textEditBoughtDate);
        ownerText = getActivity().findViewById(R.id.textEditOwner);

        sizeFlexboxLayout = getActivity().findViewById(R.id.flexboxLayoutSize);
        sizeFlexboxLayout.setFlexDirection(FlexDirection.ROW);
        brandFlexboxLayout = getActivity().findViewById(R.id.flexboxLayoutBrand);
        brandFlexboxLayout.setFlexDirection(FlexDirection.ROW);
        ownerFlexboxLayout = getActivity().findViewById(R.id.flexboxLayoutOwner);
        ownerFlexboxLayout.setFlexDirection(FlexDirection.ROW);

        saveButton = getActivity().findViewById(R.id.buttonEditSave);
        deleteButton = getActivity().findViewById(R.id.buttonEditDelete);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
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

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Do nothing
    }

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);

        Intent intent = getActivity().getIntent();
        final int item_id = intent.getIntExtra("item_id", -1);
        db = new DatabaseHandler(getActivity());
        final Item item = db.getOneItem(item_id);

        final byte[] b = item.getImage();
        image.setImageBitmap(BitmapFactory.decodeByteArray(b, 0, b.length));

        String itemKind = item.getKind();
        ArrayAdapter kindAdapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.Kind_no_all, android.R.layout.simple_spinner_item
        );
        kindAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kindSpinner.setAdapter(kindAdapter);
        // Set initially selected kind.
        int kindPosition = kindAdapter.getPosition(itemKind);
        kindSpinner.setSelection(kindPosition, false);
        kindSpinner.setOnItemSelectedListener(this);

        // Initially as (itemKind.contentEquals("Top") || itemKind.contentEquals("上装"))
        ArrayAdapter categoryAdapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.Top_no_all, android.R.layout.simple_spinner_item
        );
        if(itemKind.contentEquals("Bottom") || itemKind.contentEquals("下装")){
            categoryAdapter = ArrayAdapter.createFromResource(
                    getActivity(), R.array.Bottom_no_all, android.R.layout.simple_spinner_item
            );
        } else if(itemKind.contentEquals("Footwear") || itemKind.contentEquals("鞋类")){
            categoryAdapter = ArrayAdapter.createFromResource(
                    getActivity(), R.array.Footwear_no_all, android.R.layout.simple_spinner_item
            );
        } else if(itemKind.contentEquals("Accessories") || itemKind.contentEquals("配饰")) {
            categoryAdapter = ArrayAdapter.createFromResource(
                    getActivity(), R.array.Accessories_no_all, android.R.layout.simple_spinner_item
            );
        }
        categorySpinner.setAdapter(categoryAdapter);
        // Set initially selected category.
        int categoryPosition = categoryAdapter.getPosition(item.getCategory());
        categorySpinner.setSelection(categoryPosition);

        priceText.setText(String.valueOf(item.getPrice()));

        ArrayAdapter seasonAdapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.Season, android.R.layout.simple_spinner_item
        );
        seasonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seasonSpinner.setAdapter(seasonAdapter);
        int seasonPosition = seasonAdapter.getPosition(item.getSeason());
        seasonSpinner.setSelection(seasonPosition);

        sizeText.setText(item.getSize());
        String[] existedSizes = db.getExistedSizes();
        addExistedValueButtons(existedSizes, sizeFlexboxLayout, sizeText);
        setTextListener(sizeText, sizeFlexboxLayout);

        brandText.setText(item.getBrand());
        String[] existedBrands = db.getExistedBrands();
        addExistedValueButtons(existedBrands, brandFlexboxLayout, brandText);
        setTextListener(brandText, brandFlexboxLayout);

        boughtDateText.setText(item.getBoughtDate());
        boughtDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        boughtDateText.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                pickerDialog.show();
            }
        });

        ownerText.setText(item.getOwner());
        String[] existedOwners = db.getExistedOwners();
        addExistedValueButtons(existedOwners, ownerFlexboxLayout, ownerText);
        setTextListener(ownerText, ownerFlexboxLayout);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newPrice = 0;
                if (priceText.getText().length() != 0) {
                    newPrice = Integer.parseInt(priceText.getText().toString());
                }
                Item updated_item = new Item(
                        item_id,
                        b,
                        kindSpinner.getSelectedItem().toString(),
                        categorySpinner.getSelectedItem().toString(),
                        newPrice,
                        seasonSpinner.getSelectedItem().toString(),
                        sizeText.getText().toString(),
                        brandText.getText().toString(),
                        ownerText.getText().toString(),
                        boughtDateText.getText().toString()
                        );
                db.updateItem(updated_item, item_id);

                Intent intent = new Intent(getActivity(), ClosetActivity.class);
                startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.deleteitem)
                        .setMessage(R.string.areyousureyouwanttodeletethisitem)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                db.deleteItem(item_id);
                                Intent intent = new Intent(getActivity(), ClosetActivity.class);
                                startActivity(intent);
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
}