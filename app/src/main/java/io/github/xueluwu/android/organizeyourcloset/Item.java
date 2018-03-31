package io.github.xueluwu.android.organizeyourcloset;

/**
 * Item class
 */

public class Item {

    //private variables
    int _id;
    byte[] _img;
    String _kind;
    String _category;
    int _price;
    String _season;

    // Empty constructor
    public Item(){

    }

    // constructor
    public Item(int id, byte[] img, String kind, String category, int price, String season) {
        this._id = id;
        this._img = img;
        this._kind = kind;
        this._category = category;
        this._price = price;
        this._season = season;
    }

    // constructor
    public Item(byte[] img, String kind, String category, int price, String season) {
        this._img = img;
        this._kind = kind;
        this._category = category;
        this._price = price;
        this._season = season;
    }

    // getting ID
    public int getID() { return this._id; }

    // setting id
    public void setID(int id) { this._id = id; }


    public byte[] getImage() { return this._img; }
    public void setImage(byte[] b){
        this._img=b;
    }

    public String getKind() { return this._kind; }
    public void setKind(String kind) { this._kind = kind; }

    public String getCategory() { return this._category; }
    public void setCategory(String category) { this._category = category; }

    public int getPrice() { return this._price; }
    public void setPrice(int price) { this._price = price; }

    public String getSeason() { return this._season; }
    public void setSeason(String season) { this._season = season; }

}