package com.ezytopup.reseller.api;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

import static com.ezytopup.reseller.utility.Constant.API_URL_GENERALUSAGE;

/**
 * Created by indraaguslesmana on 3/31/17.
 */

public interface EzytopupAPI {

    /**
     * Apiary Documentation for ezytoptup
     */
    @POST("/v1/auth/login")
    Call<Authrequest> login_request1(@Body Authrequest authrequest);

    /**
     * Live version
     */

    @POST("WGS_API_login.php?" + API_URL_GENERALUSAGE)
    Call<Authrequest> login_request(@Body Authrequest authrequest);

    @GET("WGS_API_products.php?" + API_URL_GENERALUSAGE)
    Call<ProductResponse> getProduct();

    @GET("WGS_API_getHeaderImages.php?" + API_URL_GENERALUSAGE)
    Call<HeaderimageResponse> getImageHeader();

    @GET("WGS_API_best_seller_products.php?" + API_URL_GENERALUSAGE)
    Call<BestSellerResponse> getBestSeller();

    @GET("WGS_API_search_products.php?" + API_URL_GENERALUSAGE)
    Call<SearchResponse> getSearch(@Query("name") String productName);

    @GET("WGS_API_get_detail_products.php?" + API_URL_GENERALUSAGE)
    Call<DetailProductResponse> getDetailProduct(@Query("id") String productid);

    @GET("WGS_API_panduan_awal.php?" + API_URL_GENERALUSAGE)
    Call<TutorialResponse> getTutorial();

    @GET("WGS_API_getTutorial.php?" + API_URL_GENERALUSAGE)
    Call<TutorialStepResponse> getTutorialStep();

    @GET("WGS_API_getFaq.php?" + API_URL_GENERALUSAGE)
    Call<FaqResponse> getFaq();

    @GET("WGS_API_contact_us.php?" + API_URL_GENERALUSAGE)
    Call<ContactUsResponse> getContactUs();

    @GET("WGS_API_getTerm.php?" + API_URL_GENERALUSAGE)
    Call<TermResponse> getTerm();

    @GET("WGS_API_categorized_products.php?" + API_URL_GENERALUSAGE)
    Call<CategoryResponse> getCategory(@Query("category_id") String productId);

    @GET("WGS_API_payment_method.php?" + API_URL_GENERALUSAGE)
    Call<PaymentResponse> getCheckactivePayment();

    @GET("WGS_API_payment_method_internet_banking.php?" + API_URL_GENERALUSAGE)
    Call<PaymentResponse> getPaymentInetBanking();

    @GET("WGS_API_payment_method_bank_transfer.php?" + API_URL_GENERALUSAGE)
    Call<PaymentResponse> getPaymentBankTransfer();

    @GET("WGS_API_payment_method_credit_card.php?" + API_URL_GENERALUSAGE)
    Call<PaymentResponse> getPaymentCreditcard();

    @GET("WGS_API_payment_method_wallet.php?" + API_URL_GENERALUSAGE)
    Call<PaymentResponse> getPaymentEzyWallet();

    @GET("WGS_API_categories.php?" + API_URL_GENERALUSAGE)
    Call<ListCategoryResponse> getListCategory();

    @GET("WGS_API_get_order_transaction_history.php?" + API_URL_GENERALUSAGE)
    Call<TransactionHistoryResponse> getHistory(@Header("Authorize") String token, @Query("customerId") String customerId);
    /**
     * this API i implement with object PaymentResponse have no object data, just get Message for checking result 200 or not
     */
    @POST("WGS_API_abuyNow.php?" + API_URL_GENERALUSAGE)
    Call<PaymentResponse> buyNow(@Header("Authorize") String token,
                                 @Query("id") String id,
                                 @Query("session_name") String session_name,
                                 @Query("product_id") String product_id,
                                 @Query("product_name") String product_name,
                                 @Query("price") String price,
                                 @Query("qty") String qty,
                                 @Query("payment_method_id") String payment_method_id,
                                 @Query("email") String email,
                                 @Query("customerId") String customerId,
                                 @Query("templateId") String templateId,
                                 @Query("serviceFee") String serviceFee,
                                 @Query("serviceFeePercentage") String serviceFeePercentage,
                                 @Query("discount") String discount,
                                 @Query("recepientName") String recepientName,
                                 @Query("recepientEmail") String recepientEmail,
                                 @Query("message") String message,
                                 @Query("senderName") String senderName,
                                 @Query("paymentCaption") String paymentCaption,
                                 @Query("paymentNote") String paymentNote,
                                 @Query("templateCaption") String templateCaption,
                                 @Query("coupon_promo") String coupon_promo);

    @GET("WGS_API_template_gift.php?" + API_URL_GENERALUSAGE)
    Call<TamplateResponse> getTamplateGift();

    @GET("WGS_API_verify_access_token.php?" + API_URL_GENERALUSAGE)
    Call<TokencheckResponse> checkToken(@Header("Authorize") String token);

    @POST("WGS_API_signup_tanpa_login.php?" + API_URL_GENERALUSAGE)
    Call<Authrequest> setLoginskip(@Query("provider") String provider,
                                   @Query("reg_gcm_id") String regfcmid,
                                   @Query("device_id") String deviceid);

    @POST("WGS_API_login_reg_gcm_id.php?" + API_URL_GENERALUSAGE)
    Call<RegfcmResponse> setRegFcm(@Query("provider") String provider,
                                   @Query("reg_gcm_id") String regfcmid,
                                   @Query("device_id") String deviceid);

    @POST("WGS_API_logout.php?" + API_URL_GENERALUSAGE)
    Call<TokencheckResponse> setLogout(@Header("Authorize") String headerToken,
                                       @Query("device_id") String device_id,
                                       @Query("token") String token);

    @GET("WGS_API_get_current_user_wallet.php?" + API_URL_GENERALUSAGE)
    Call<WalletbalanceResponse> getWalletbalance(@Header("Authorize") String headerToken);

    @Multipart
    @POST("WGS_API_change_password.php?" + API_URL_GENERALUSAGE)
    Call<ChangepasswordResponse> setChangePassword(@Header("Authorize") String tokenHeader,
                                                   @Part("pass_lama") String oldpassword,
                                                   @Part("pass_baru1") String newpassword,
                                                   @Part("pass_baru2") String confirmpassword,
                                                   @Part("token") String token);

    @POST("WGS_API_feedback_form.php?" + API_URL_GENERALUSAGE)
    Call<SendFeedBackResponse> sendFeedBack(@Header("Authorize") String headerToken,
                                            @Query("token") String token,
                                            @Query("name") String name,
                                            @Query("email") String email,
                                            @Query("phone") String phone,
                                            @Query("subject") String subject,
                                            @Query("message") String message);
    @Multipart
    @POST("WGS_API_forget_password.php?" + API_URL_GENERALUSAGE)
    Call<ForgotpasswordResponse> setForgotpassword(@Header("Authorize") String headerToken,
                                                   @Part("email") String email,
                                                   @Part("phone") String phone);

    @POST("WGS_API_buyNow_reseller.php?" + API_URL_GENERALUSAGE)
    Call<VoucherprintResponse> getBuyreseller(@Body HashMap<String, String> data);

    @GET("WGS_API_getHeaderImages_reseller.php?" + API_URL_GENERALUSAGE)
    Call<HeaderimageResponse> getImageHeaderReseller();

    @GET("WGS_API_server_time.php?" + API_URL_GENERALUSAGE)
    Call<ServertimeResponse> getServertime();

    @Multipart
    @POST("WGS_API_edit_current_user_profile.php?" + API_URL_GENERALUSAGE)
    Call<UpdateProfileResponse> setUpdateProfile(@Header("Authorize") String token,
                                                 @Part ("first_name") String firstName,
                                                 @Part ("phone") String phone,
                                                 @Part ("password") String password,
                                                 @Part ("avatar") String avatar);

    @GET("WGS_API_get_current_user_wallet_reseller.php?" + API_URL_GENERALUSAGE)
    Call<WalletbalanceResponse> getWalletResellerbalance(@Header("Authorize") String headerToken);

    @GET("WGS_API_get_order_transaction_history_reseller.php?" + API_URL_GENERALUSAGE)
    Call<TransactionHistoryResponse> getHistoryReseller(@Header("Authorize") String token,
                                                        @Query("customerId") String customerId);

}
