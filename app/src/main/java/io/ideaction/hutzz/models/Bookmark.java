package io.ideaction.hutzz.models;

public class Bookmark {

    private String id;
    private int image;
    private String property;
    private String propertyAddress;
    private String price;

    public Bookmark(String id, int image, String property, String propertyAddress, String price) {
        this.id = id;
        this.image = image;
        this.property = property;
        this.propertyAddress = propertyAddress;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
