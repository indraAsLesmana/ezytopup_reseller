package com.ezytopup.reseller.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by luteh on 17/05/17.
 */

public class TutorialStepResponse {

    @SerializedName("result")
    @Expose
    public ArrayList<Result> result = null;
    @SerializedName("status")
    @Expose
    public Status status;

    public class Result {

        @SerializedName("judul")
        @Expose
        public String judul;
        @SerializedName("isi")
        @Expose
        public String isi;
    }

    public class Status {

        @SerializedName("code")
        @Expose
        private String code;
        @SerializedName("message")
        @Expose
        private String message;

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

    }
}
