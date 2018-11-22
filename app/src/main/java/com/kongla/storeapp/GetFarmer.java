package com.kongla.storeapp;

public class GetFarmer {
    String farmName;
    String farmDescription;
    String farmerNumber;
    String memberID;
    int farmSellProduct;
    String address;
    public GetFarmer(String farmName){
        this.farmName = farmName;
    }

    public GetFarmer(String farmName,String farmDescription,String farmerNumber,String address, String memberID){
        this.farmName = farmName;
        this.farmDescription = farmDescription;
        this.farmerNumber = farmerNumber;
        this.address = address;
        this.memberID = memberID;
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
    public int getFarmSellProduct(){
        return farmSellProduct;
    }
}
