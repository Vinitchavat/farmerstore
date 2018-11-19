package com.kongla.storeapp;

public class ProductModel {
    String farmID, productName, fruitName, unit, date, imgLink;
    int quantity, price, productSell;

    public ProductModel() {
    }

    public ProductModel(String farmID, String fruitName, String productName, int price,
                        int quantity, String unit, int productSell, String imgLink) {
        this.productName = productName;
        this.farmID = farmID;
        this.fruitName = fruitName;
        this.price = price;
        this.quantity = quantity;
        this.unit = unit;
        this.productSell = productSell;
        this.imgLink = imgLink;
    }

    public ProductModel(String farmID, String fruitName, String productName, int price,
                        int quantity, String unit, String date, int productSell, String imgLink) {
        this.productName = productName;
        this.farmID = farmID;
        this.fruitName = fruitName;
        this.price = price;
        this.quantity = quantity;
        this.unit = unit;
        this.date = date;
        this.productSell = productSell;
        this.imgLink = imgLink;
    }

    public String getProductName() {
        return productName;
    }

    public String getFruitName() {
        return fruitName;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public String getDate() {
        return date;
    }

    public String getImgLink() {
        return imgLink;
    }
}