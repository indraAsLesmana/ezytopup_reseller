package com.ezytopup.reseller.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by luteh on 18/05/17.
 */

public class ContactUsResponse {
    @SerializedName("alamat")
    @Expose
    public Alamat alamat;
    @SerializedName("sosmed")
    @Expose
    public Sosmed sosmed;
    @SerializedName("support")
    @Expose
    public Support support;
    @SerializedName("pesanku")
    @Expose
    public String pesanku;
    @SerializedName("status")
    @Expose
    public Status status;

    public class Alamat {

        @SerializedName("nama")
        @Expose
        public String nama;
        @SerializedName("alamat")
        @Expose
        public String alamat;
        @SerializedName("kota")
        @Expose
        public String kota;
        @SerializedName("negara")
        @Expose
        public String negara;

    }

    public class Sosmed {

        @SerializedName("fb")
        @Expose
        public String fb;
        @SerializedName("twitter")
        @Expose
        public String twitter;
        @SerializedName("instagram")
        @Expose
        public String instagram;

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

    public class Support {

        @SerializedName("web")
        @Expose
        public String web;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("telp")
        @Expose
        public String telp;

    }
}
