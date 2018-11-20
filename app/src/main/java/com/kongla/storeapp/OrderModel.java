package com.kongla.storeapp;

public class OrderModel  {
    String farmID;
    String buyerID;
    String productID;
    String orderStatus;
    String date;
    String sellerID;
    public OrderModel(){}
    public OrderModel(String buyerID, String sellerID, String farmID, String productID, String orderStatus){
        this.buyerID = buyerID;
        this.farmID = farmID;
        this.productID = productID;
        this.orderStatus = orderStatus;
        this.sellerID = sellerID;
    }
    public OrderModel(String buyerID, String sellerID, String farmID,String productID, String orderStatus, String date){
        this.buyerID = buyerID;
        this.farmID = farmID;
        this.productID = productID;
        this.orderStatus = orderStatus;
        this.date = date;
        this.sellerID = sellerID;
    }
}
