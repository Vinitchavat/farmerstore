package com.kongla.storeapp;

public class OrderM {
    String buyerID,sellerID,farmID,productID;
    public OrderM(){}
    public OrderM(String buyerID, String sellerID, String farmID, String productID){
        this.buyerID=buyerID;
        this.sellerID=sellerID;
        this.farmID = farmID;
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

}
