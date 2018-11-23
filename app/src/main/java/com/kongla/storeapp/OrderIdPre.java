package com.kongla.storeapp;

public class OrderIdPre {
    String buyerID,sellerID,farmID,date,productID,orderStatus;
    public OrderIdPre(){}
    public OrderIdPre(String buyerID, String sellerID, String farmID, String date, String productID,String orderStatus){
        this.buyerID=buyerID;
        this.sellerID=sellerID;
        this.farmID = farmID;
        this.date = date;
        this.productID = productID;
        this.orderStatus = orderStatus;
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
    public String getOrderStatus(){
        return orderStatus;
    }
}
