package com.ezytopup.reseller.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by indraaguslesmana on 4/9/17.
 */

public class TermResponse {

    @SerializedName("result")
    @Expose
    public ArrayList<Result> result = null;
    @SerializedName("status")
    @Expose
    public Status status;

    public class Result {
        @SerializedName("term")
        @Expose
        public String term;
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
