package com.kongla.storeapp;

public class OrderModel  {
    String farmID;
    String buyerID;
    String productID;
    String orderStatus;
    String date;
    public OrderModel(){}
    public OrderModel(String farmID, String buyerID, String productID, String orderStatus){
        this.buyerID = buyerID;
        this.farmID = farmID;
        this.productID = productID;
        this.orderStatus = orderStatus;
    }
    public OrderModel(String farmID, String buyerID, String productID, String orderStatus, String date){
        this.buyerID = buyerID;
        this.farmID = farmID;
        this.productID = productID;
        this.orderStatus = orderStatus;
        this.date = date;
    }
}
