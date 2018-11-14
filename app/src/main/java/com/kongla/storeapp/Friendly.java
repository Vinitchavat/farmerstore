package com.kongla.storeapp;

public class Friendly {
    String name, sender, message, time,orderid,type;

    public Friendly(){}

    public Friendly(String sender, String message, String time, String type){
        this.sender = sender;
        this.message = message;
        this.time = time;
        this.type = type;
    }
    public String type() {
        return type;
    }
    public String Sender() {
        return sender;
    }
    public String getName() {
        return name;
    }
    public String getTime(){
        return time;
    }
    public String getMessage(){
        return message;
    }
    public String getOrderid(){
        return orderid;
    }

}