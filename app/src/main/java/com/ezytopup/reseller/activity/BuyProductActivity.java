package com.ezytopup.reseller.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ezytopup.reseller.Eztytopup;
import com.ezytopup.reseller.R;
import com.ezytopup.reseller.adapter.Grid_GiftAdapter;
import com.ezytopup.reseller.adapter.RecyclerList_bankoptionAdapter;
import com.ezytopup.reseller.api.PaymentResponse;
import com.ezytopup.reseller.api.DetailProductResponse;
import com.ezytopup.reseller.api.TamplateResponse;
import com.ezytopup.reseller.utility.Constant;
import com.ezytopup.reseller.utility.Helper;
import com.ezytopup.reseller.utility.PreferenceUtils;
import com.zj.btsdk.PrintPic;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyProductActivity extends BaseActivity implements View.OnClickListener,
        RecyclerList_bankoptionAdapter.RecyclerList_bankoptionListener,
        Grid_GiftAdapter.Grid_GiftAdapterListener {

    private static final String PRODUCT_ID = "BuyProductActivity::productid";
    private static final String PRODUCT_NAME = "BuyProductActivity::productname";
    private static final String PRODUCT_IMAGE = "BuyProductActivity::productimage";
    private static final String PRODUCT_BG = "BuyProductActivity::productbackground";
    private static final String PRODUCT_PRICE = "BuyProductActivity::productprice";
    private ArrayList<DetailProductResponse.Result> results;
    private TextView mSubtotal, mTotal, mQty;
    private static final String TAG = "BuyProductActivity";
    private String productId;
    private TextView bt_Detailproduct;
    private View view_desc, view_chartcount;
    private ConstraintLayout view_detailbuy;
    private TextView info1, info2, info3, buy_desc;
    private RelativeLayout option_epayment, option_banktransfer, option_creditcard, option_wallet;
    private ImageView ico_epayment, ico_banktransfer, ico_creditcard, ico_wallet, bank_transferStatus,
            credit_cardStatus, ezy_walletStatus;
    private TextView bank_transferTv, credit_cardTv, ezy_walletTv, mAdminFee, mDiscount, bt_addtochart;
    private GridView gift_grid;
    private LinearLayout view_paymentNote, buy_giftform, buy_redemvoucher;
    private TextView paymentMethodTv, paymentNoteTv, etCouponPromo;
    private Button buynowButton, cancelButton;
    private String productName, productImage, productBackground, productPrice;
    private EditText ed_usermail, gift_receiver, gift_sender, gift_email, gift_message;
    private PaymentResponse.PaymentMethod paymentDetail;
    private TamplateResponse.Result giftDetail;
    private LinearLayout buy_button_container;
    private CheckBox ch_gift;
    private GridLayoutManager lLayout;
    private RadioButton rd_epayment, rd_banktransfer, rd_creditcard, rd_wallet;
    private ConstraintLayout bg_product;
    private RecyclerView rViewListEZ, rViewListCC, rViewListBT, rViewListIB;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private BluetoothDevice con_dev = null;

    public static void start(Activity caller, String id, String name, String image, String bg,
                             String price) {
        Intent intent = new Intent(caller, BuyProductActivity.class);
        intent.putExtra(PRODUCT_ID, id);
        intent.putExtra(PRODUCT_NAME, name);
        intent.putExtra(PRODUCT_IMAGE, image);
        intent.putExtra(PRODUCT_BG, bg);
        intent.putExtra(PRODUCT_PRICE, price);
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (getIntent().getStringExtra(BuyProductActivity.PRODUCT_ID) == null||
                getIntent().getStringExtra(BuyProductActivity.PRODUCT_NAME) == null||
                getIntent().getStringExtra(BuyProductActivity.PRODUCT_IMAGE) == null||
                getIntent().getStringExtra(BuyProductActivity.PRODUCT_BG) == null||
                getIntent().getStringExtra(BuyProductActivity.PRODUCT_PRICE) == null){

            finish();
            return;
        }

        results = new ArrayList<>();
        mQty = (TextView) findViewById(R.id.tvQty);
        info1 = (TextView) findViewById(R.id.buy_info1);
        info2 = (TextView) findViewById(R.id.buy_info2);
        info3 = (TextView) findViewById(R.id.buy_info3);
        buy_desc = (TextView) findViewById(R.id.buy_description);
        view_desc = findViewById(R.id.buy_descview);
        view_detailbuy = (ConstraintLayout) findViewById(R.id.buy_detailview);
        bt_Detailproduct = (TextView) findViewById(R.id.tvDetailProduct);
        ImageView mBackgroundProduct = (ImageView) findViewById(R.id.buy_bgimage);
        ImageView mProductImage = (ImageView) findViewById(R.id.buy_productimages);
        TextView mProductTitle = (TextView) findViewById(R.id.buy_producttitle);
        TextView mProductPrice = (TextView) findViewById(R.id.buy_productprice);
        mTotal = (TextView) findViewById(R.id.buy_total);
        mSubtotal = (TextView) findViewById(R.id.buy_subtotal);
        mAdminFee = (TextView) findViewById(R.id.buy_adminfee);
        view_paymentNote = (LinearLayout) findViewById(R.id.buy_paymentnote);

        productId = getIntent().getStringExtra(BuyProductActivity.PRODUCT_ID);
        productName = getIntent().getStringExtra(BuyProductActivity.PRODUCT_NAME);
        productImage = getIntent().getStringExtra(BuyProductActivity.PRODUCT_IMAGE);
        productBackground = getIntent().getStringExtra(BuyProductActivity.PRODUCT_BG);
        productPrice = getIntent().getStringExtra(BuyProductActivity.PRODUCT_PRICE);

        gift_grid = (GridView) findViewById(R.id.gridTemplate);
        paymentMethodTv = (TextView) findViewById(R.id.tvPaymentCaption);
        paymentNoteTv = (TextView) findViewById(R.id.tvPaymentNote);
        buynowButton = (Button) findViewById(R.id.btnBuyNow);
        cancelButton = (Button) findViewById(R.id.btnCancel);
        ed_usermail = (EditText) findViewById(R.id.buy_entermail);
        buy_button_container = (LinearLayout) findViewById(R.id.buy_paymentbutton);
        buy_giftform = (LinearLayout) findViewById(R.id.buy_giftform);
        buy_redemvoucher = (LinearLayout) findViewById(R.id.buy_redemvoucher);
        ch_gift = (CheckBox) findViewById(R.id.chkSendAsGift);
        gift_sender = (EditText) findViewById(R.id.tvSenderName);
        gift_receiver = (EditText) findViewById(R.id.tvRecepientName);
        gift_email = (EditText) findViewById(R.id.tvRecepientEmail);
        gift_message = (EditText) findViewById(R.id.tvMessage);
        etCouponPromo = (EditText) findViewById(R.id.etCouponPromo);
        mDiscount = (TextView) findViewById(R.id.buy_discount);
        rd_epayment = (RadioButton) findViewById(R.id.rd_epayment);
        rd_banktransfer = (RadioButton) findViewById(R.id.rd_banktransfer);
        rd_creditcard = (RadioButton) findViewById(R.id.rd_creditcard);
        rd_wallet = (RadioButton) findViewById(R.id.rd_wallet);
        ico_epayment = (ImageView) findViewById(R.id.img_statusepayment);
        ico_banktransfer = (ImageView) findViewById(R.id.img_statusbanktransfer);
        ico_creditcard = (ImageView) findViewById(R.id.img_statuscreditcard);
        ico_wallet = (ImageView) findViewById(R.id.img_wallet);
        option_epayment = (RelativeLayout) findViewById(R.id.option_epayment);
        option_banktransfer = (RelativeLayout) findViewById(R.id.option_banktransfer);
        option_creditcard = (RelativeLayout) findViewById(R.id.option_creditcard);
        option_wallet = (RelativeLayout) findViewById(R.id.option_wallet);
        bt_addtochart = (TextView) findViewById(R.id.tvAddtochart);
        bg_product = (ConstraintLayout) findViewById(R.id.buy_bgproduct);
        view_chartcount = findViewById(R.id.view_chartcount);
        rViewListIB = (RecyclerView)findViewById(R.id.rc_banklist);
        rViewListBT = (RecyclerView)findViewById(R.id.rc_banktransferlist);
        rViewListCC = (RecyclerView)findViewById(R.id.rc_creditcardlist);
        rViewListEZ = (RecyclerView)findViewById(R.id.rc_walletlist);

        buynowButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        mProductTitle.setText(productName);
        mProductPrice.setText(productPrice);

        Glide.with(BuyProductActivity.this)
                .load(productBackground).centerCrop()
                .error(R.drawable.ic_error_loadimage)
                .crossFade(Constant.ITEM_CROSSFADEDURATION)
                .into(mBackgroundProduct);
        mBackgroundProduct.setImageAlpha(Constant.DEF_BGALPHA);

        Glide.with(BuyProductActivity.this)
                .load(productImage).centerCrop()
                .error(R.drawable.ic_error_loadimage)
                .crossFade(Constant.ITEM_CROSSFADEDURATION)
                .into(mProductImage);
        mTotal.setText(productPrice);
        mSubtotal.setText(productPrice);

        bt_Detailproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view_detailbuy.isShown()){
                    bt_Detailproduct.setText(R.string.buy);
                    view_detailbuy.setVisibility(View.GONE);
                    view_desc.setVisibility(View.VISIBLE);
                    view_paymentNote.setVisibility(View.GONE);
                    buy_button_container.setVisibility(View.GONE);
                    buy_giftform.setVisibility(View.GONE);
                    buy_redemvoucher.setVisibility(View.GONE);
                    ch_gift.setVisibility(View.GONE);
                }else {
                    bt_Detailproduct.setText(R.string.detail_product);
                    view_detailbuy.setVisibility(View.VISIBLE);
                    view_desc.setVisibility(View.GONE);
                    view_paymentNote.setVisibility(View.VISIBLE);
                    buy_button_container.setVisibility(View.VISIBLE);
                    buy_giftform.setVisibility(View.VISIBLE);
                    buy_redemvoucher.setVisibility(View.VISIBLE);
                    ch_gift.setVisibility(View.VISIBLE);
                }
            }
        });

        bt_addtochart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bg_product.setVisibility(View.GONE);
                view_chartcount.setVisibility(View.VISIBLE);
                if (view_desc.isShown()){
                    view_desc.setVisibility(View.GONE);
                    view_detailbuy.setVisibility(View.VISIBLE);
                }
            }
        });

        rd_epayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rd_wallet.setChecked(false);
                rd_banktransfer.setChecked(false);
                rd_creditcard.setChecked(false);

                rViewListIB.setVisibility(View.VISIBLE);
                rViewListEZ.setVisibility(View.GONE);
                rViewListBT.setVisibility(View.GONE);
                rViewListCC.setVisibility(View.GONE);
            }
        });
        rd_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rd_epayment.setChecked(false);
                rd_banktransfer.setChecked(false);
                rd_creditcard.setChecked(false);

                rViewListIB.setVisibility(View.GONE);
                rViewListEZ.setVisibility(View.VISIBLE);
                rViewListBT.setVisibility(View.GONE);
                rViewListCC.setVisibility(View.GONE);
            }
        });
        rd_banktransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rd_wallet.setChecked(false);
                rd_epayment.setChecked(false);
                rd_creditcard.setChecked(false);

                rViewListIB.setVisibility(View.GONE);
                rViewListEZ.setVisibility(View.GONE);
                rViewListBT.setVisibility(View.VISIBLE);
                rViewListCC.setVisibility(View.GONE);
            }
        });
        rd_creditcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rd_wallet.setChecked(false);
                rd_epayment.setChecked(false);
                rd_banktransfer.setChecked(false);

                rViewListIB.setVisibility(View.GONE);
                rViewListEZ.setVisibility(View.GONE);
                rViewListBT.setVisibility(View.GONE);
                rViewListCC.setVisibility(View.VISIBLE);
            }
        });

        getDetailProduct();
        setActivePayment();
        viewGiftTamplate();
    }

    private void viewGiftTamplate() {
        ArrayList<TamplateResponse.Result> gift_tamplate = Eztytopup.getTamplateActive();
        Grid_GiftAdapter giftAdapter = new Grid_GiftAdapter(this, gift_tamplate, this);
        gift_grid.setAdapter(giftAdapter);
        gift_grid.setVisibility(View.VISIBLE);
    }

    private void setActivePayment() {
        ArrayList<PaymentResponse.PaymentMethod> paymentActive = Eztytopup.getPaymentActive();
        for (int i = 0; i < paymentActive.size(); i++) {
            String paymentid = paymentActive.get(i).getId();
            switch (paymentid){
                case Constant.INTERNET_BANK:
                    rd_epayment.setText(paymentActive.get(i).getPaymentMethod());
                    getImage(paymentActive.get(i).getPaymentLogo(), ico_epayment);
                    ArrayList<PaymentResponse.PaymentMethod> epaymentData = Eztytopup.getPaymentInternet();
                    lLayout = new GridLayoutManager(BuyProductActivity.this, Constant.PAYMENT_GRIDSETTINGS);
                    rViewListIB.setHasFixedSize(true);
                    rViewListIB.setLayoutManager(lLayout);
                    RecyclerList_bankoptionAdapter rcAdapter = new RecyclerList_bankoptionAdapter(this,
                            epaymentData, this);
                    rViewListIB.setAdapter(rcAdapter);
                    option_epayment.setVisibility(View.VISIBLE);
                    break;
                case Constant.BANK_TRANSFER:
                    rd_banktransfer.setText(paymentActive.get(i).getPaymentMethod());
                    getImage(paymentActive.get(i).getPaymentLogo(), ico_banktransfer);
                    ArrayList<PaymentResponse.PaymentMethod> epaymentDataBT = Eztytopup.getPaymentTransfer();
                    lLayout = new GridLayoutManager(BuyProductActivity.this, Constant.PAYMENT_GRIDSETTINGS);
                    rViewListBT.setHasFixedSize(true);
                    rViewListBT.setLayoutManager(lLayout);
                    RecyclerList_bankoptionAdapter rcAdapterBT = new RecyclerList_bankoptionAdapter(this,
                            epaymentDataBT, this);
                    rViewListBT.setAdapter(rcAdapterBT);
                    option_banktransfer.setVisibility(View.VISIBLE);
                    break;
                case Constant.CREADIT_CARD:
                    rd_banktransfer.setText(paymentActive.get(i).getPaymentMethod());
                    getImage(paymentActive.get(i).getPaymentLogo(), ico_banktransfer);
                    ArrayList<PaymentResponse.PaymentMethod> epaymentDataCC = Eztytopup.getPaymentTransfer();
                    lLayout = new GridLayoutManager(BuyProductActivity.this, Constant.PAYMENT_GRIDSETTINGS);
                    rViewListCC.setHasFixedSize(true);
                    rViewListCC.setLayoutManager(lLayout);
                    RecyclerList_bankoptionAdapter rcAdapterCC = new RecyclerList_bankoptionAdapter(this,
                            epaymentDataCC, this);
                    rViewListCC.setAdapter(rcAdapterCC);
                    option_creditcard.setVisibility(View.VISIBLE);
                    break;
                case Constant.EZYTOPUP_WALLET:
                    rd_wallet.setText(paymentActive.get(i).getPaymentMethod());
                    getImage(paymentActive.get(i).getPaymentLogo(), ico_wallet);
                    ArrayList<PaymentResponse.PaymentMethod> epaymentDataEZ = Eztytopup.getPaymentWallet();
                    lLayout = new GridLayoutManager(BuyProductActivity.this, Constant.PAYMENT_GRIDSETTINGS);
                    rViewListEZ.setHasFixedSize(true);
                    rViewListEZ.setLayoutManager(lLayout);
                    RecyclerList_bankoptionAdapter rcAdapterEZ = new RecyclerList_bankoptionAdapter(this,
                            epaymentDataEZ, this);
                    rViewListEZ.setAdapter(rcAdapterEZ);
                    option_wallet.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    private void getImage(String url, ImageView imagePlace){
        if (url == null){
            return;
        }
        Glide.with(BuyProductActivity.this)
                .load(url)
                .error(R.drawable.ic_error_loadimage)
                .crossFade(Constant.ITEM_CROSSFADEDURATION)
                .into(imagePlace);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getDetailProduct(){
        Call<DetailProductResponse> product = Eztytopup.getsAPIService().
                getDetailProduct(productId);
        product.enqueue(new Callback<DetailProductResponse>() {
            @Override
            public void onResponse(Call<DetailProductResponse> call,
                                   Response<DetailProductResponse> response) {
                if (response.isSuccessful() &&
                        response.body().status.getCode()
                                .equals(String.valueOf(HttpURLConnection.HTTP_OK))){
                    results.addAll(response.body().result);
                    DetailProductResponse.Result r = results.get(0);
                    info1.setText(r.getInfo1());
                    info2.setText(r.getInfo2());
                    info3.setText(r.getInfo3());
                    buy_desc.setText(r.getDescription());
                }else {
                    Toast.makeText(BuyProductActivity.this, response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<DetailProductResponse> call, Throwable t) {
                Helper.log(TAG, t.getMessage(), t);
            }
        });

    }

    @Override
    public void initView() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_buyproduct;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBuyNow:
                buyforEndUser();
                break;
            case R.id.btnCancel:
                break;
        }
    }

    @SuppressLint("SdCardPath")
    private Boolean printImage() {
        File file = new File("/mnt/sdcard/Ezytopup/print_logo.jpg");
        if (!file.exists() && !PreferenceUtils.getSinglePrefrenceString(this,
                R.string.settings_def_sellerprintlogo_key).equals(Constant.PREF_NULL)) {
            Helper.downloadFile(this, PreferenceUtils.getSinglePrefrenceString(this,
                    R.string.settings_def_sellerprintlogo_key));
            Toast.makeText(this, R.string.please_wait_imageprint, Toast.LENGTH_SHORT).show();
            return Boolean.FALSE;
        }else {
                byte[] sendData = null;
                PrintPic pg = new PrintPic();
                pg.initCanvas(384);
                pg.initPaint();
                pg.drawImage(100, 0, "/mnt/sdcard/Ezytopup/print_logo.jpg");
                sendData = pg.printDraw();
                Eztytopup.getmBTprintService().write(sendData);
                return Boolean.TRUE;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, R.string.bluetooth_open, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, R.string.failed_open_bluetooth, Toast.LENGTH_LONG).show();
                }
                break;
            case  REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    con_dev = Eztytopup.getmBTprintService().getDevByMac(address);

                    Eztytopup.getmBTprintService().connect(con_dev);
                }
                break;
        }
    }

    private void buyforEndUser(){
        String uid = PreferenceUtils.getSinglePrefrenceString(this, R.string.settings_def_uid_key);
                final String token = PreferenceUtils.getSinglePrefrenceString(this,
                        R.string.settings_def_storeaccess_token_key);
                if (getPaymentDetail().getId() == null){
                    Toast.makeText(this, R.string.select_payment_method, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (uid.equals(Constant.PREF_NULL)){
                    Toast.makeText(this, "Uid problem", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (token.equals(Constant.PREF_NULL)){
                    Toast.makeText(this, "Token null", Toast.LENGTH_SHORT).show();
                    return;
                }
                String getServiceFee = "0";
                String getServiceFeePercentage = "0";
                if (mAdminFee.getText().toString().contains("%")) {
                    getServiceFeePercentage = String.valueOf(getNumberPercentage(mAdminFee.getText().toString()));
                } else {
                    getServiceFee = String.valueOf(getNumber(mAdminFee.getText().toString()));
                }
                String templateId = "";
                String tamplateName = "";
                if (getGiftDetail() != null){
                    templateId = getGiftDetail().getTemplateId();
                    tamplateName = getGiftDetail().getTemplateName();
                }

                Call<PaymentResponse> buy = Eztytopup.getsAPIService().buyNow(
                        token,          // header
                        "1",
                        token,
                        productId,
                        productName,
                        productPrice,
                        mQty.getText().toString(),      // qty for temporary
                        getPaymentDetail().getId(),     // id payment
                        ed_usermail.getText().toString(),
                        uid,
                        templateId,
                        getServiceFee,
                        getServiceFeePercentage,
                        mDiscount.getText().toString(),
                        gift_receiver.getText().toString(),
                        gift_email.getText().toString(),
                        gift_message.getText().toString(),
                        gift_sender.getText().toString(),
                        getPaymentDetail().getPaymentMethod(),
                        getPaymentDetail().getPaymentNote(),
                        tamplateName,
                        etCouponPromo.getText().toString()
                );
                buy.enqueue(new Callback<PaymentResponse>() {
                    @Override
                    public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                        if (response.isSuccessful() && response.body().status.getCode()
                                .equals(String.valueOf(HttpURLConnection.HTTP_OK))){

                            Log.i(TAG, String.format("onResponse: %s %s", response.body().status.getCode(),
                                    response.body().status.getMessage()));

                                PaymentActivity.start(BuyProductActivity.this,
                                        ed_usermail.getText().toString(),
                                        PreferenceUtils.getSinglePrefrenceString(BuyProductActivity.this,
                                                R.string.settings_def_storeidevice_key),
                                        getPaymentDetail().getPaymentUrl());
                        }else {
                            Log.i(TAG, "onResponse: " + response.body().status.toString());
                            Toast.makeText(BuyProductActivity.this,
                                    response.body().status.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<PaymentResponse> call, Throwable t) {
                        Toast.makeText(BuyProductActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //just copy from last developer
    public static double getNumber(String number){
        if (number.equals("")) {return 0;}
        else if (number.equals("null")) {return 0;}
        else {return Double.parseDouble(number.replaceAll("[^\\d.]", "")
                .replaceAll("[,.]", "").replaceAll("\\s+",""));}
    }
    public static double getNumberPercentage(String number){
        if (number.equals("")) {return 0;}
        else if (number.equals("null")) {return 0;}
        else {return Double.parseDouble(number.replaceAll("[^\\d.]", "")
                .replaceAll("\\s+",""));}
    }


    @Override
    public void onCardClick(PaymentResponse.PaymentMethod optionPaymentItem) {
        paymentMethodTv.setText(optionPaymentItem.getPaymentMethod());
        paymentNoteTv.setText(optionPaymentItem.getPaymentNote());
        setPaymentDetail(optionPaymentItem);
    }

    @Override
    public void onGiftClick(TamplateResponse.Result optionPaymentItem) {
        Toast.makeText(this, optionPaymentItem.getTemplateName(),
                Toast.LENGTH_SHORT).show();
        setGiftDetail(optionPaymentItem);
    }

    public PaymentResponse.PaymentMethod getPaymentDetail() {
        return paymentDetail;
    }

    public void setPaymentDetail(PaymentResponse.PaymentMethod paymentDetail) {
        this.paymentDetail = paymentDetail;
    }

    public TamplateResponse.Result getGiftDetail() {
        return giftDetail;
    }

    public void setGiftDetail(TamplateResponse.Result giftDetail) {
        this.giftDetail = giftDetail;
    }
}
