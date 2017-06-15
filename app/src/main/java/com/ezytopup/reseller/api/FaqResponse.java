package com.ezytopup.reseller.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by indraaguslesmana on 4/8/17.
 */

public class FaqResponse {

    @SerializedName("result")
    @Expose
    public ArrayList<Result> result = null;
    @SerializedName("status")
    @Expose
    public Status status;

    public class Result {

        @SerializedName("question")
        @Expose
        public String question;
        @SerializedName("answer")
        @Expose
        public String answer;

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
