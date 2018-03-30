package com.example.android.organizeyourcloset;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;


public class GridViewAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList<Item> data = new ArrayList<Item>();
    private int mSelectedPosition = -1;

    public GridViewAdapter(Context context, int layoutResourceId, ArrayList data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.image_view = (ImageView) row.findViewById(R.id.image_view);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Item item = data.get(position);
        byte[] b = item.getImage();
        holder.image_view.setImageBitmap(BitmapFactory.decodeByteArray(b, 0, b.length));

        holder.image_view.setLayoutParams(new LinearLayout.LayoutParams(300, 300));

        if (position == mSelectedPosition)
            holder.image_view.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));

        return row;
    }

    static class ViewHolder {
        ImageView image_view;
    }

    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
    }
}