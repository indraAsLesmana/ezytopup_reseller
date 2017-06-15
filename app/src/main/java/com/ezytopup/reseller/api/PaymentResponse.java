package com.ezytopup.reseller.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by indraaguslesmana on 4/12/17.
 */

public class PaymentResponse {
    @SerializedName("payment_methods")
    @Expose
    public ArrayList<PaymentMethod> paymentMethods = null;
    @SerializedName("status")
    @Expose
    public Status status;

    public class PaymentMethod implements Serializable{

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("payment_logo")
        @Expose
        public String paymentLogo;
        @SerializedName("payment_note")
        @Expose
        public String paymentNote;
        @SerializedName("payment_method")
        @Expose
        public String paymentMethod;
        @SerializedName("admin_fee")
        @Expose
        public String adminFee;
        @SerializedName("admin_fee_persen")
        @Expose
        public String adminFeePersen;
        @SerializedName("payment_url")
        @Expose
        public String paymentUrl;

        public String getId() {
            return id;
        }

        public String getPaymentLogo() {
            return paymentLogo;
        }

        public String getPaymentNote() {
            return paymentNote;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public String getAdminFee() {
            return adminFee;
        }

        public String getAdminFeePersen() {
            return adminFeePersen;
        }

        public String getPaymentUrl() {
            return paymentUrl;
        }
    }
    public class Status {

        @SerializedName("code")
        @Expose
        public String code;
        @SerializedName("message")
        @Expose
        public String message;

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
