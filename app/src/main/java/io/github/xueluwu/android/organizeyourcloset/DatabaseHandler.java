package io.github.xueluwu.android.organizeyourcloset;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * DatabaseHandler
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "itemsManager";

    // Table names
    // TABLE_ITEMS for closet, TABLE_WORN for calendar
    private static final String TABLE_ITEMS = "items";
    private static final String TABLE_WORN = "worn";

    // Table Columns names
    // TABLE_ITEMS
    private static final String KEY_ID = "_id";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_KIND = "kind";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_PRICE = "price";
    private static final String KEY_SEASON = "season";
    // TABLE_WORN
    private static final String KEY_ITEMID = "id";
    private static final String KEY_DATE = "Date";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    //Create tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ITEMS + "("
                + KEY_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_IMAGE  + " BLOB, "
                + KEY_KIND + " TEXT, "
                + KEY_CATEGORY + " TEXT, "
                + KEY_PRICE + " UNSIGNED, "
                + KEY_SEASON + " TEXT);");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_WORN +"("
                + KEY_ITEMID + " INTEGER, "
                + KEY_DATE + " TEXT);");

    }


    // Upgrade database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORN);
        onCreate(db);
    }


    // Add items in TABLE_ITEMS
    // Used in ClosetAddItem
    // Return a boolean, true -> successfully inserted, false -> not inserted
    public boolean addItem(Item item){   //addContacts(Item contact)
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(KEY_IMAGE, item.getImage());
        values.put(KEY_KIND, item.getKind());
        values.put(KEY_CATEGORY, item.getCategory());
        values.put(KEY_PRICE, item.getPrice());
        values.put(KEY_SEASON, item.getSeason());

        long result = db.insert(TABLE_ITEMS, null, values);
        db.close();
        return (result != -1);
    }


    // Get all items from TABLE_ITEMS
    // Used for showing images in the gridview
    public List<Item> getAllItems(String kind, String category)
    {
        List<Item> itemList = new ArrayList<Item>();

        String selectQuery;
        if (kind.equals("All") || kind.equals("全部")) { // Select All Query
            selectQuery = "SELECT  * FROM " + TABLE_ITEMS;
        } else if (category.equals("All")){
            selectQuery = "SELECT  * FROM " + TABLE_ITEMS + " WHERE " + KEY_KIND + " = '" + kind + "'";
        } else {
            selectQuery = "SELECT  * FROM " + TABLE_ITEMS +
                    " WHERE " + KEY_CATEGORY + " = '" + category + "'";
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setID(Integer.parseInt(cursor.getString(0)));
                item.setImage(cursor.getBlob(1));
                item.setKind(cursor.getString(2));
                item.setCategory(cursor.getString(3));
                item.setPrice(Integer.parseInt(cursor.getString(4)));
                item.setSeason(cursor.getString(5));

                // Adding item to list
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        return itemList;
    }


    // Update a single item in TABLE_ITEMS
    // Used in ClosetEditItem
    public boolean updateItem(Item item, int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IMAGE, item.getImage());
        values.put(KEY_KIND, item.getKind());
        values.put(KEY_CATEGORY, item.getCategory());
        values.put(KEY_PRICE, item.getPrice());
        values.put(KEY_SEASON, item.getSeason());

        // updating row
        db.update(TABLE_ITEMS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });

        return true;
    }


    // Delete a single item in TABLE_ITEMS, also delete in TABLE_WORN
    // Used in ClosetEditItem
    public void deleteItem(int Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, KEY_ID + " = ?", new String[] { String.valueOf(Id) });
        db.delete(TABLE_WORN, KEY_ITEMID + " = ?", new String[] { String.valueOf(Id) });
        db.close();
    }


    // ----------
    // Add items in TABLE_WORN
    // Used in CalendarAdd
    public void addInWorn(Integer itemid, String date) {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put(KEY_ITEMID, itemid);
        values.put(KEY_DATE, date);
        db.insert(TABLE_WORN,null,values);
        db.close();
    }


    // Get all items with the chosen date from TABLE_WORN
    // Used in Calendar
    public List<Item> getWornItems(String chosenDate) {
        List<Item> itemList = new ArrayList<Item>();
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_WORN + " WHERE " + KEY_DATE + " ='" + chosenDate + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            Integer itemid;
            do {
                itemid = Integer.parseInt(cursor.getString(0));
                Item item = getOneItem(itemid);
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        db.close();
        return itemList;
    }


    // Delete an event (itemid, date) in TABLE_WORN
    // Used in Calendar
    public void deleteInWorn(Integer itemid, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WORN, KEY_ITEMID + " = ? AND " + KEY_DATE + " = ?", new String[] { String.valueOf(itemid), date });
        db.close();
    }


    // Get a HashSet<String> of the dates that has clothes
    public HashSet<String> addedDateSet() {
        SQLiteDatabase db = this.getReadableDatabase();
        HashSet<String> addedDateSet = new HashSet<String>();

        String query = "Select * from " + TABLE_WORN;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                addedDateSet.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        db.close();
        return addedDateSet;
    }


    // Check if the item and date combination has been added in TABLE_WORN
    // Used in CalendarAddActivity
    public boolean checkWornItem(int id, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_WORN + " WHERE "
                + KEY_ITEMID + " = " + id + " AND " + KEY_DATE + " ='" + date + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() == 1) return true;
        else return false;
    }


    // Retrieve one Item according to the unique id assigned by TABLE_ITEMS
    // Used in Closet and Calendar
    public Item getOneItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ITEMS,
                new String[] {KEY_ID, KEY_IMAGE, KEY_KIND, KEY_CATEGORY, KEY_PRICE, KEY_SEASON},
                KEY_ID + "=?", new String[] {String.valueOf(id)},
                null, null, null, null);

        Item item = new Item();

        if (cursor != null && cursor.moveToFirst()) {

            item.setID(Integer.parseInt(cursor.getString(0)));
            item.setImage(cursor.getBlob(1));
            item.setKind(cursor.getString(2));
            item.setCategory(cursor.getString(3));
            item.setPrice(Integer.parseInt(cursor.getString(4)));
            item.setSeason(cursor.getString(5));

            cursor.close();
        }

        return item;
    }


    // Get the total count of items in the chosen kind
    // Used in MenuStatistics
    public int getCount(String kind) {
        SQLiteDatabase db = this.getReadableDatabase();

        String countQuery;
        if (kind.equals("All")) {  // all rows
            countQuery = "SELECT  * FROM " + TABLE_ITEMS;
        } else {  // count of each kind
            countQuery = "SELECT  * FROM " + TABLE_ITEMS + " WHERE " + KEY_KIND + " = '" + kind + "'";
        }
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    // Get sum of price value of the chosen kind kind
    // Used in MenuStatistics
    public int getSum(String kind) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sumQuery;
        if (kind.equals("All")) {
            sumQuery = "SELECT SUM(" + KEY_PRICE + ") FROM " + TABLE_ITEMS;
        } else {
            sumQuery = "SELECT SUM(" + KEY_PRICE + ") FROM " + TABLE_ITEMS +
                    " WHERE " + KEY_KIND + " = '" + kind + "'";
        }
        Cursor cursor = db.rawQuery(sumQuery, null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return 0;
    }

}
