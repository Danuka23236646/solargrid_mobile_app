package com.example.smartsolarmicrogrid.network.dto;

import com.google.gson.annotations.SerializedName;

public class CompleteQrRequest {
    @SerializedName("qrPayload")
    private String qrPayload;

    @SerializedName("actualEnergyAmountKwh")
    private double actualEnergyAmountKwh;

    @SerializedName("completionNotes")
    private String completionNotes;

    public CompleteQrRequest() {}

    public CompleteQrRequest(String qrPayload, double actualEnergyAmountKwh, String completionNotes) {
        this.qrPayload = qrPayload;
        this.actualEnergyAmountKwh = actualEnergyAmountKwh;
        this.completionNotes = completionNotes;
    }

    public String getQrPayload() {
        return qrPayload;
    }

    public void setQrPayload(String qrPayload) {
        this.qrPayload = qrPayload;
    }

    public double getActualEnergyAmountKwh() {
        return actualEnergyAmountKwh;
    }

    public void setActualEnergyAmountKwh(double actualEnergyAmountKwh) {
        this.actualEnergyAmountKwh = actualEnergyAmountKwh;
    }

    public String getCompletionNotes() {
        return completionNotes;
    }

    public void setCompletionNotes(String completionNotes) {
        this.completionNotes = completionNotes;
    }
}
