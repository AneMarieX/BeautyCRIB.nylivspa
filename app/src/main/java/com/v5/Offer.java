package com.v5;


public class Offer {

    private String key;
    private String name;
    private String desc;
    private String date;
    private String time;
    private String price;
    private String location;


    public Offer()
    {
    }



    public Offer(String offerName, String offerDesc, String offerDate, String offerTime, String offerPrice, String offerLocation ) {

        name=offerName;
        desc=offerDesc;
        date=offerDate;
        time=offerTime;
        price=offerPrice;
        location=offerLocation;

    }

    public void setKey(String key){
        this.key = key;
    }

    public String getKey(){
        return key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public  String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }



}



