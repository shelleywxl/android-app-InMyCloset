package io.github.xueluwu.android.organizeyourcloset;

/**
 * Item in the closet.
 */

public class Item {
    private int id;
    private byte[] image;
    private String kind;
    private String category;
    private int price;
    private String season;
    private String size;
    private String brand;
    private String owner;
    private String boughtDate;

    public Item() {
    }

    public Item(
            int id,
            byte[] img,
            String kind,
            String category,
            int price,
            String season,
            String size,
            String brand,
            String owner,
            String boughtDate) {
        this.id = id;
        this.image = img;
        this.kind = kind;
        this.category = category;
        this.price = price;
        this.season = season;
        this.size = size;
        this.brand = brand;
        this.owner = owner;
        this.boughtDate = boughtDate;
    }

    public Item(
            byte[] img,
            String kind,
            String category,
            int price,
            String season,
            String size,
            String brand,
            String owner,
            String boughtDate) {
        this.image = img;
        this.kind = kind;
        this.category = category;
        this.price = price;
        this.season = season;
        this.size = size;
        this.brand = brand;
        this.owner = owner;
        this.boughtDate = boughtDate;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] b) {
        this.image = b;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getBoughtDate() {
        return boughtDate;
    }

    public void setBoughtDate(String boughtDate) {
        this.boughtDate = boughtDate;
    }
}