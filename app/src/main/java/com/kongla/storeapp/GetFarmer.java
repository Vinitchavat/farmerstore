package com.kongla.storeapp;

public class GetFarmer {
    String farmName;
    String farmDescription;
    String farmerNumber;
    String memberID;
    int farmRating;
    int farmSellProduct;
    public GetFarmer(String farmName){
        this.farmName = farmName;
    }
    public String getFarmName() {
        return farmName;
    }
    public String getFarmDescription(){
        return farmDescription;
    }
    public String getFarmerNumber(){
        return farmerNumber;
    }
    public String getMemberID(){
        return memberID;
    }
    public int getFarmRating(){
        return farmRating;
    }
    public int getFarmSellProduct(){
        return farmSellProduct;
    }
}
