package com.example.adbank;

import android.app.usage.UsageStats;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

public class ModelTranzaction{


    public String uid_account;
    public String uid_application;
    public String statusTranzaction;
    public String dateRequest;
    public String quantityTranzaction;

    public String getUid_account() {
        return uid_account;
    }

    public void setUid_account(String uid_account) {
        this.uid_account = uid_account;
    }

    public String getUid_application() {
        return uid_application;
    }

    public void setUid_application(String uid_application) {
        this.uid_application = uid_application;
    }

    public String getStatusTranzaction() {
        return statusTranzaction;
    }

    public void setStatusTranzaction(String statusTranzaction) {
        this.statusTranzaction = statusTranzaction;
    }

    public String getDateRequest() {
        return dateRequest;
    }

    public void setDateRequest(String dateRequest) {
        this.dateRequest = dateRequest;
    }

    public String getQuantityTranzaction() {
        return quantityTranzaction;
    }

    public void setQuantityTranzaction(String quantityTranzaction) {
        this.quantityTranzaction = quantityTranzaction;
    }

    public ModelTranzaction(String uid_application, String uid_account, String dateRequest, String statusTranzaction, String quantityTranzaction) {
        this.uid_account = uid_account;
        this.uid_application = uid_application;
        this.statusTranzaction = statusTranzaction;
        this.dateRequest = dateRequest;
        this.quantityTranzaction = quantityTranzaction;
    }
}
