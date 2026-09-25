package com.example.smartsolarmicrogrid.models;

import java.io.Serializable;

/**
 * Model class representing a Solar Station microgrid node.
 */
public class SolarStation implements Serializable {
    private int id;
    private String stringId; // Real MongoDB ObjectId string
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private double capacityKw;
    private int availableSlots;
    private double currentStoredEnergyKwh;
    private double availableIntakeKwh;
    private int batteryStoragePercentage;
    private boolean isOutOfStorage;

    public SolarStation() {}

    public SolarStation(int id, String name, String address, double latitude, double longitude, double capacityKw, int availableSlots) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.capacityKw = capacityKw;
        this.availableSlots = availableSlots;
        this.isOutOfStorage = availableSlots <= 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStringId() {
        if (stringId != null && !stringId.isEmpty()) return stringId;
        return String.valueOf(id);
    }

    public void setStringId(String stringId) {
        this.stringId = stringId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getCapacityKw() {
        return capacityKw;
    }

    public void setCapacityKw(double capacityKw) {
        this.capacityKw = capacityKw;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
        if (availableSlots <= 0) {
            this.isOutOfStorage = true;
        }
    }

    public double getCurrentStoredEnergyKwh() {
        return currentStoredEnergyKwh;
    }

    public void setCurrentStoredEnergyKwh(double currentStoredEnergyKwh) {
        this.currentStoredEnergyKwh = currentStoredEnergyKwh;
    }

    public double getAvailableIntakeKwh() {
        return availableIntakeKwh;
    }

    public void setAvailableIntakeKwh(double availableIntakeKwh) {
        this.availableIntakeKwh = availableIntakeKwh;
        if (availableIntakeKwh <= 0) {
            this.isOutOfStorage = true;
        }
    }

    public int getBatteryStoragePercentage() {
        return batteryStoragePercentage;
    }

    public void setBatteryStoragePercentage(int batteryStoragePercentage) {
        this.batteryStoragePercentage = batteryStoragePercentage;
    }

    public boolean isOutOfStorage() {
        return isOutOfStorage || availableSlots <= 0 || availableIntakeKwh <= 0;
    }

    public void setOutOfStorage(boolean outOfStorage) {
        this.isOutOfStorage = outOfStorage;
    }
}