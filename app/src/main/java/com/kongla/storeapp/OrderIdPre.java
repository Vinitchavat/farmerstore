package com.kongla.storeapp;

public class OrderIdPre {
    String buyerID,sellerID,farmID,date,productID;
    public OrderIdPre(){}
    public OrderIdPre(String buyerID, String sellerID, String farmID, String date, String productID){
        this.buyerID=buyerID;
        this.sellerID=sellerID;
        this.farmID = farmID;
        this.date = date;
        this.productID = productID;
    }
    public String getBuyerID() {
        return buyerID;
    }
    public String getSellerID() {
        return sellerID;
    }
    public String getFarmID() {
        return farmID;
    }
    public String getProductID() {
        return productID;
    }
    public String getDate() {
        return date;
    }

}
