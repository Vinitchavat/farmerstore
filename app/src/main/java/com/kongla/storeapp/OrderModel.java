package com.kongla.storeapp;

public class OrderModel  {
    String farmID,buyerID,productID,orderStatus,sellerID,date;
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

    public String getDate() {
        return date;
    }

    public String getBuyerID() {
        return buyerID;
    }

    public String getFarmID() {
        return farmID;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getProductID() {
        return productID;
    }

    public String getSellerID() {
        return sellerID;
    }
}
