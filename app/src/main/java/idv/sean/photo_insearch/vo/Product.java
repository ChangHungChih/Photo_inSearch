package idv.sean.photo_insearch.vo;


public class Product {
    private int picture;
    private String name;
    private int price;

    public Product() {
    }

    public Product(int picture, String name, int price) {
        this.picture = picture;
        this.name = name;
        this.price = price;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
