package com.kongla.storeapp;

public class Friendly {
    String sender, message, time,type;

    public Friendly(){}

    public Friendly(String sender, String message, String time, String type){
        this.sender = sender;
        this.message = message;
        this.time = time;
        this.type = type;
    }
    public String getType() {
        return type;
    }
    public String getSender() {
        return sender;
    }
    public String getTime(){
        return time;
    }
    public String getMessage(){
        return message;
    }


}