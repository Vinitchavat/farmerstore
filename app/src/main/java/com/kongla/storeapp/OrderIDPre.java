package com.kongla.storeapp;

public class OrderIDPre {
    String buyerID,sellerID,farmID,date,productID;
    public OrderIDPre(String buyerID, String sellerID, String farmID, String date, String productID){
        this.buyerID=buyerID;
        this.sellerID=sellerID;
        this.farmID = farmID;
        this.date = date;
        this.productID = productID;
    }
}
