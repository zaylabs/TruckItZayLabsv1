package com.zaylabs.truckitzaylabsv1.DTO;

import com.google.firebase.firestore.GeoPoint;

public class customerRequest {

    private String name;
    private String phone;
    private GeoPoint pickup;


    private GeoPoint drop;
    private String photodp;


    customerRequest(){

    }

    public customerRequest(String name, String phone, GeoPoint pickup,GeoPoint drop,String photodp){

        this.name = name;
        this.phone = phone;
        this.pickup = pickup;
        this.drop = drop;
        this.photodp = photodp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public GeoPoint getPickup() {
        return pickup;
    }

    public void setPickup(GeoPoint pickup) {
        this.pickup = pickup;
    }

    public GeoPoint getDrop() {
        return drop;
    }

    public void setDrop(GeoPoint drop) {
        this.drop = drop;
    }

    public String getPhotodp() {
        return photodp;
    }

    public void setPhotodp(String photodp) {
        this.photodp = photodp;
    }

}