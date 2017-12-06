package com.example.tonycurrie.myapplication;


public class RowItem {
    private String imageURL;
    private String product;
    private String price,color,gender,size,description,quantity,category,seller,age;

    public String getImageURL() {
        return imageURL;
    }
    public void setimageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getprice() {
        return price;
    }
    public void setprice(String price) {
        this.price = price;
    }
    public String getproduct() {
        return product;
    }
    public void setproduct(String product) {
        this.product = product;
    }
    @Override
    public String toString() {
        return product + "\n" + price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}