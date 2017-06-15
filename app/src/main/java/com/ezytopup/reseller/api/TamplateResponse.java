package com.ezytopup.reseller.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by indraaguslesmana on 4/21/17.
 */

public class TamplateResponse {
    @SerializedName("result")
    @Expose
    public ArrayList<Result> result = null;
    @SerializedName("status")
    @Expose
    public Status status;

    public class Result {

        @SerializedName("template_id")
        @Expose
        public String templateId;
        @SerializedName("template_name")
        @Expose
        public String templateName;
        @SerializedName("image")
        @Expose
        public String image;

        public String getTemplateId() {
            return templateId;
        }

        public String getTemplateName() {
            return templateName;
        }

        public String getImage() {
            return image;
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
