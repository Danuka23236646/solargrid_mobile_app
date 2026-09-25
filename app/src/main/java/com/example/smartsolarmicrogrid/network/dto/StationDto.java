package com.example.smartsolarmicrogrid.network.dto;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class StationDto implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("stationCode")
    private String stationCode;

    @SerializedName("address")
    private String address;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("availableSlots")
    private int availableSlots;

    @SerializedName("totalSlots")
    private int totalSlots;

    @SerializedName("capacityKwh")
    private double capacityKwh;

    @SerializedName("receivedEnergyKwh")
    private double receivedEnergyKwh;

    @SerializedName("dispatchedEnergyKwh")
    private double dispatchedEnergyKwh;

    @SerializedName("currentStoredEnergyKwh")
    private double currentStoredEnergyKwh;

    @SerializedName("pendingIntakeKwh")
    private double pendingIntakeKwh;

    @SerializedName("availableIntakeKwh")
    private double availableIntakeKwh;

    @SerializedName("batteryStoragePercentage")
    private int batteryStoragePercentage;

    @SerializedName("isOutOfStorage")
    private boolean isOutOfStorage;

    @SerializedName("canReceiveEnergy")
    private boolean canReceiveEnergy;

    @SerializedName("status")
    private String status;

    public StationDto() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
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

    public int getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(int totalSlots) {
        this.totalSlots = totalSlots;
    }

    public double getCapacityKwh() {
        return capacityKwh;
    }

    public void setCapacityKwh(double capacityKwh) {
        this.capacityKwh = capacityKwh;
    }

    public double getReceivedEnergyKwh() {
        return receivedEnergyKwh;
    }

    public void setReceivedEnergyKwh(double receivedEnergyKwh) {
        this.receivedEnergyKwh = receivedEnergyKwh;
    }

    public double getDispatchedEnergyKwh() {
        return dispatchedEnergyKwh;
    }

    public void setDispatchedEnergyKwh(double dispatchedEnergyKwh) {
        this.dispatchedEnergyKwh = dispatchedEnergyKwh;
    }

    public double getCurrentStoredEnergyKwh() {
        return currentStoredEnergyKwh;
    }

    public void setCurrentStoredEnergyKwh(double currentStoredEnergyKwh) {
        this.currentStoredEnergyKwh = currentStoredEnergyKwh;
    }

    public double getPendingIntakeKwh() {
        return pendingIntakeKwh;
    }

    public void setPendingIntakeKwh(double pendingIntakeKwh) {
        this.pendingIntakeKwh = pendingIntakeKwh;
    }

    public double getAvailableIntakeKwh() {
        return availableIntakeKwh;
    }

    public void setAvailableIntakeKwh(double availableIntakeKwh) {
        this.availableIntakeKwh = availableIntakeKwh;
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

    public boolean isCanReceiveEnergy() {
        return canReceiveEnergy && !isOutOfStorage();
    }

    public void setCanReceiveEnergy(boolean canReceiveEnergy) {
        this.canReceiveEnergy = canReceiveEnergy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}