package com.kongla.storeapp;

public class GetData {
    String date,farmID,fruitName,productName,unit;
    int price,quantity;
    public GetData(){}
    public GetData(String date, String farmID, String fruitName, int price, String productName, int quantity, String unit){
        this.date = date;
        this.farmID = farmID;
        this.fruitName = fruitName;
        this.price = price;
        this.productName = productName;
        this.quantity = quantity;
        this.unit = unit;
    }
    public String getDate() {
        return date;
    }
    public String getFarmID() {
        return farmID;
    }
    public String getFruitName() {
        return fruitName;
    }
    public int getPrice() {
        return price;
    }
    public String getProductName() {
        return productName;
    }
    public int getQuantity() {
        return quantity;
    }
    public String getUnitPro() {
        return unit;
    }
}
