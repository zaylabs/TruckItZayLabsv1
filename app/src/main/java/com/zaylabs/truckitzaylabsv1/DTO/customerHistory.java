package com.zaylabs.truckitzaylabsv1.DTO;

import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

public class customerHistory {
    private String driverName;
    private String driverphone;
    private String driverNIC;
    private GeoPoint pickup;
    private GeoPoint drop;
    private Date date;
    private String cid;
    private String did;
    private String vt;
    private String weight;
    private String boxes;
    private String description;
    private String driverloading;
    private String estridedistance;
    private String actualRideDistance;
    private String estFare;
    private String actualFare;
    private String pickupaddress;
    private String dropaddress;
customerHistory(){

}

public customerHistory(String driverName,String driverphone,String driverNIC, GeoPoint pickup,GeoPoint drop,Date date,String cid,String did, String vt, String weight, String boxes, String description, String driverloading, String estridedistance, String actualRideDistance, String estFare, String actualFare, String pickupaddress, String dropaddress){

    this.driverName = driverName;
    this.driverphone = driverphone;
    this.driverNIC = driverNIC;
    this.pickup = pickup;
    this.drop = drop;
    this.date = date;
    this.cid = cid;
    this.did = did;
    this.vt = vt;
    this.weight = weight;
    this.boxes = boxes;
    this.description = description;
    this.driverloading = driverloading;
    this.estridedistance = estridedistance;
    this.actualRideDistance = actualRideDistance;
    this.estFare = estFare;
    this.actualFare = actualFare;
    this.pickupaddress=pickupaddress;
    this.dropaddress=dropaddress;
}

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverphone() {
        return driverphone;
    }

    public void setDriverphone(String driverphone) {
        this.driverphone = driverphone;
    }

    public String getDriverNIC() {
        return driverNIC;
    }

    public void setDriverNIC(String driverNIC) {
        this.driverNIC = driverNIC;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getVt() {
        return vt;
    }

    public void setVt(String vt) {
        this.vt = vt;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBoxes() {
        return boxes;
    }

    public void setBoxes(String boxes) {
        this.boxes = boxes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDriverloading() {
        return driverloading;
    }

    public void setDriverloading(String driverloading) {
        this.driverloading = driverloading;
    }

    public String getEstridedistance() {
        return estridedistance;
    }

    public void setEstridedistance(String estridedistance) {
        this.estridedistance = estridedistance;
    }

    public String getActualRideDistance() {
        return actualRideDistance;
    }

    public void setActualRideDistance(String actualRideDistance) {
        this.actualRideDistance = actualRideDistance;
    }

    public String getEstFare() {
        return estFare;
    }

    public void setEstFare(String estFare) {
        this.estFare = estFare;
    }

    public String getActualFare() {
        return actualFare;
    }

    public void setActualFare(String actualFare) {
        this.actualFare = actualFare;
    }

    public String getPickupaddress() {
        return pickupaddress;
    }

    public void setPickupaddress(String pickupaddress) {
        this.pickupaddress = pickupaddress;
    }

    public String getDropaddress() {
        return dropaddress;
    }

    public void setDropaddress(String dropaddress) {
        this.dropaddress = dropaddress;
    }
}

