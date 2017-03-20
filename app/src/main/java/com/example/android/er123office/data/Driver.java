package com.example.android.er123office.data;

import com.google.firebase.database.Exclude;

import java.util.Comparator;

/**
 * Created by Abshafi on 1/28/2017.
 */

public class Driver {

    private String driverEmail;
    private String driverPassword;
    private String driverName;
    private String plateChars;
    private String plateNums;
    private String phoneNumber;
    private float distance;
    private String LatPosition;
    private String LongPosition;
    private boolean isSelected = false;
    private String driverAvailable;

    public void setDriverAvailable(String driverAvailable) {
        this.driverAvailable = driverAvailable;
    }

    public String getDriverAvailable() {
        return driverAvailable;
    }

    public static final Comparator<Driver> DISTANCE
            = new Comparator<Driver>() {
        public int compare(Driver p1, Driver p2) {
            return (p1.getDistance()<p2.getDistance()) ? -1 :1;
        }
    };

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getLatPosition() {
        return LatPosition;
    }

    public String getLongPosition() {
        return LongPosition;
    }

    public void setLatPosition(String latPosition) {

        LatPosition = latPosition;
    }
    public void setLongPosition(String longPosition) {
        LongPosition = longPosition;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }

    public void setDriverPassword(String driverPassword) {
        this.driverPassword = driverPassword;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setPlateChars(String plateChars) {
        this.plateChars = plateChars;
    }

    public void setPlateNums(String plateNums) {
        this.plateNums = plateNums;
    }

    public String getDriverEmail() {
        return driverEmail;
    }

    public String getDriverPassword() {
        return driverPassword;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getPlateChars() {
        return plateChars;
    }

    public String getPlateNums() {
        return plateNums;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Exclude
    public boolean isSelected() {
        return isSelected;
    }
}
