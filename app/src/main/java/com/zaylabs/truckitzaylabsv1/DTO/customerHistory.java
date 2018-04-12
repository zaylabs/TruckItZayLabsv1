package com.zaylabs.truckitzaylabsv1.DTO;

import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

public class customerHistory {

    private String name;
    private String phone;
    private GeoPoint pickup;
    private GeoPoint drop;
    private Date date;
    private String CID;
    private String VT;
    private String weight;
    private String boxes;
    private String description;
    private String driverloading;
    private String ridedistance;
    private String pickupaddress;
    private String dropaddress;
    private String drivername;
    private String driverdp;
    private String drivernic;
    private String driverphone;
    private GeoPoint driverlocation;
    private String carregno;
    private String driverid;
    private String status;

    customerHistory(){

    }

    public customerHistory(String name, GeoPoint pickup,GeoPoint drop, String phone, Date date, String CID, String VT, String weight, String boxes ,String description, String driverloading, String ridedistance, String pickupaddress, String dropaddress, String drivername, String driverdp, String drivernic, String driverphone, GeoPoint driverlocation, String carregno, String driverid, String status ){

        this.name = name;
        this.pickup = pickup;
        this.drop = drop;
        this.phone = phone;
        this.date =date;
        this.CID = CID;
        this.VT = VT;
        this.weight=weight;
        this.boxes=boxes;
        this.description=description;
        this.driverloading=driverloading;
        this.ridedistance=ridedistance;
        this.pickupaddress=pickupaddress;
        this.dropaddress=dropaddress;
        this.drivername=drivername;
        this.driverdp=driverdp;
        this.drivernic=drivernic;
        this.driverphone=driverphone;
        this.driverlocation=driverlocation;
        this.carregno=carregno;
        this.driverid=driverid;
        this.status=status;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCID() {
        return CID;
    }

    public void setCID(String CID) {
        this.CID = CID;
    }

    public String getVT() {
        return VT;
    }

    public void setVT(String VT) {
        this.VT = VT;
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

    public String getRidedistance() {
        return ridedistance;
    }

    public void setRidedistance(String ridedistance) {
        this.ridedistance = ridedistance;
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

    public String getDrivername() {
        return drivername;
    }

    public void setDrivername(String drivername) {
        this.drivername = drivername;
    }

    public String getDriverdp() {
        return driverdp;
    }

    public void setDriverdp(String driverdp) {
        this.driverdp = driverdp;
    }

    public String getDrivernic() {
        return drivernic;
    }

    public void setDrivernic(String drivernic) {
        this.drivernic = drivernic;
    }

    public String getDriverphone() {
        return driverphone;
    }

    public void setDriverphone(String driverphone) {
        this.driverphone = driverphone;
    }

    public GeoPoint getDriverlocation() {
        return driverlocation;
    }

    public void setDriverlocation(GeoPoint driverlocation) {
        this.driverlocation = driverlocation;
    }

    public String getCarregno() {
        return carregno;
    }

    public void setCarregno(String carregno) {
        this.carregno = carregno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDriverid() {
        return driverid;
    }

    public void setDriverid(String driverid) {
        this.driverid = driverid;
    }
}

