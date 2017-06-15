package com.ezytopup.reseller.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by indraaguslesmana on 4/15/17.
 */

public class TransactionHistoryResponse {
    @SerializedName("result")
    @Expose
    public ArrayList<Result> result = null;
    @SerializedName("status")
    @Expose
    public Status status;

    public class Result {

        @SerializedName("created_date")
        @Expose
        public String createdDate;
        @SerializedName("product_name")
        @Expose
        public String productName;
        @SerializedName("price")
        @Expose
        public String price;
        @SerializedName("qty")
        @Expose
        public String qty;
        @SerializedName("serviceFee")
        @Expose
        public String serviceFee;
        @SerializedName("kode_kupon")
        @Expose
        public String kodeKupon;
        @SerializedName("discount")
        @Expose
        public String discount;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("payment_method")
        @Expose
        public String paymentMethod;
        @SerializedName("status_delivery")
        @Expose
        public String statusDelivery;
        @SerializedName("delivery_time")
        @Expose
        public String deliveryTime;
        @SerializedName("send_as_gift")
        @Expose
        public String sendAsGift;
        @SerializedName("recepientName")
        @Expose
        public String recepientName;
        @SerializedName("recepientEmail")
        @Expose
        public String recepientEmail;
        @SerializedName("message")
        @Expose
        public String message;
        @SerializedName("senderName")
        @Expose
        public String senderName;
        @SerializedName("tema")
        @Expose
        public Object tema;
        @SerializedName("gift_card")
        @Expose
        public String giftCard;
        @SerializedName("review_url")
        @Expose
        public String reviewUrl;
        @SerializedName("total")
        @Expose
        public String total;

        public String getCreatedDate() {
            return createdDate;
        }

        public String getProductName() {
            return productName;
        }

        public String getPrice() {
            return price;
        }

        public String getQty() {
            return qty;
        }

        public String getServiceFee() {
            return serviceFee;
        }

        public String getKodeKupon() {
            return kodeKupon;
        }

        public String getDiscount() {
            return discount;
        }

        public String getEmail() {
            return email;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public String getStatusDelivery() {
            return statusDelivery;
        }

        public String getDeliveryTime() {
            return deliveryTime;
        }

        public String getSendAsGift() {
            return sendAsGift;
        }

        public String getRecepientName() {
            return recepientName;
        }

        public String getRecepientEmail() {
            return recepientEmail;
        }

        public String getMessage() {
            return message;
        }

        public String getSenderName() {
            return senderName;
        }

        public Object getTema() {
            return tema;
        }

        public String getGiftCard() {
            return giftCard;
        }

        public String getReviewUrl() {
            return reviewUrl;
        }

        public String getTotal() {
            return total;
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

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
