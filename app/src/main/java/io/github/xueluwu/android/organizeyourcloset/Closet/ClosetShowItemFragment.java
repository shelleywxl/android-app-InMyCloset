package io.github.xueluwu.android.organizeyourcloset.Closet;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import io.github.xueluwu.android.organizeyourcloset.DatabaseHandler;
import io.github.xueluwu.android.organizeyourcloset.Item;
import io.github.xueluwu.android.organizeyourcloset.R;


/*
    ClosetShowItemFragment: item details
    shows after click the GridView images in ClosetFragment
*/

public class ClosetShowItemFragment extends Fragment {
    private DatabaseHandler db;
    private ImageView image;
    private TextView kindText, categoryText, priceText, seasonText;
    private TextView sizeText, brandText, ownerText, boughtDateText;
    private Button okButton, editButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        getActivity().setTitle("Details");
        return inflater.inflate(R.layout.fragment_closet_show_item, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        image = (ImageView)getActivity().findViewById(R.id.imageShow);
        kindText = (TextView)getActivity().findViewById(R.id.textShowKind);
        categoryText = (TextView)getActivity().findViewById(R.id.textShowCategory);
        priceText = (TextView)getActivity().findViewById(R.id.textShowPrice);
        seasonText = (TextView)getActivity().findViewById(R.id.textShowSeason);
        sizeText = (TextView)getActivity().findViewById(R.id.textShowSize);
        brandText = (TextView)getActivity().findViewById(R.id.textShowBrand);
        ownerText = (TextView)getActivity().findViewById(R.id.textShowOwner);
        boughtDateText = (TextView)getActivity().findViewById(R.id.textShowBoughtDate);
        okButton = (Button)getActivity().findViewById(R.id.buttonShowOk);
        editButton = (Button)getActivity().findViewById(R.id.buttonShowEdit);
    }

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);

        Intent intent = getActivity().getIntent();
        final Integer item_id = intent.getIntExtra("item_id", -1);

        db = new DatabaseHandler(getActivity());
        Item item = db.getOneItem(item_id);

        byte[] b = item.getImage();
        image.setImageBitmap(BitmapFactory.decodeByteArray(b, 0, b.length));
        kindText.setText(item.getKind());
        categoryText.setText(item.getCategory());
        priceText.setText(String.valueOf(item.getPrice()));
        seasonText.setText(item.getSeason());
        sizeText.setText(item.getSize());
        brandText.setText(item.getBrand());
        ownerText.setText(item.getOwner());
        boughtDateText.setText(item.getBoughtDate());

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent1 = new Intent(getActivity(), NavdrawerActivity.class);
                //startActivity(intent1);
                getActivity().onBackPressed();
                // not using startActivity so that the ClosetFragment will remember the filtered result
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClosetItemActivity.class);
                intent.putExtra("item_id", item_id);
                intent.putExtra("add_show_edit", "edit");
                startActivity(intent);
            }
        });

    }

}