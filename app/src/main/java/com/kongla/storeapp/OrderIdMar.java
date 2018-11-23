package com.kongla.storeapp;

public class OrderIdMar {
    String buyerID,sellerID,farmID,productID,orderStatus;
    public OrderIdMar(){}
    public OrderIdMar(String buyerID, String sellerID, String farmID, String productID,String orderStatus){
        this.buyerID=buyerID;
        this.sellerID=sellerID;
        this.farmID = farmID;
        this.productID = productID;
        this.orderStatus=orderStatus;
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
    public String getOrderStatus(){
        return orderStatus;
    }
}
