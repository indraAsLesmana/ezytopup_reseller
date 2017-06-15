package com.ezytopup.reseller.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ezytopup.reseller.Eztytopup;
import com.ezytopup.reseller.R;
import com.ezytopup.reseller.api.DetailProductResponse;
import com.ezytopup.reseller.api.VoucherprintResponse;
import com.ezytopup.reseller.printhelper.ThreadPoolManager;
import com.ezytopup.reseller.utility.Constant;
import com.ezytopup.reseller.utility.Helper;
import com.ezytopup.reseller.utility.PermissionHelper;
import com.ezytopup.reseller.utility.PreferenceUtils;
import com.google.gson.Gson;
import com.zj.btsdk.PrintPic;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by indraaguslesmana on 5/17/17.
 */

public class BuyResellerActivity extends BaseActivity implements View.OnClickListener{

    private static final String PRODUCT_ID = "BuyResellerActivity::productid";
    private static final String PRODUCT_NAME = "BuyResellerActivity::productname";
    private static final String PRODUCT_IMAGE = "BuyResellerActivity::productimage";
    private static final String PRODUCT_BG = "BuyResellerActivity::productbackground";
    private static final String PRODUCT_PRICE = "BuyResellerActivity::productprice";
    private static final String TAG = "BuyResellerActivity";
    private Button buynowButton, cancelButton;
    private String productId, productName, productImage, productBackground, productPrice;
    private TextView info1, info2, info3, buy_desc, textView4;
    private EditText resellerPassword;
    private ArrayList<DetailProductResponse.Result> results;
    private RelativeLayout containerResellerbuy;
    private TextView invoice_word1, invoice_word2, invoice_word3, invoice_word4, invoice_word5,
            invoice_word6, invoice_word7, invoice_word8, invoice_word9, invoice_word10,
            invoice_word11, invoice_word12, invoice_word13, invoice_word14, invoice_word15,
            invoice_word16, invoice_word17, invoice_word18, invoice_word19, invoice_word20,
            invoice_word21, invoice_word22, invoice_word23, invoice_word24, invoice_word25,
            invoice_word26, invoice_word27, invoice_word28, invoice_word29, invoice_word30,
            invoice_word31, invoice_word32, invoice_word33, invoice_word34, invoice_word35;
    private ImageView invoice_beforebuy;
    private LinearLayout invoice_subcontainer;

    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    private static BuynowInterface listener ;
    private Response responseResult;

    public static void buynowListener(BuynowInterface listener) {
        BuyResellerActivity.listener = listener;
    }

    public static void start(Activity caller, String id, String name, String image, String bg,
                             String price) {
        Intent intent = new Intent(caller, BuyResellerActivity.class);
        intent.putExtra(PRODUCT_ID, id);
        intent.putExtra(PRODUCT_NAME, name);
        intent.putExtra(PRODUCT_IMAGE, image);
        intent.putExtra(PRODUCT_BG, bg);
        intent.putExtra(PRODUCT_PRICE, price);
        caller.startActivity(intent);
    }
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (getIntent().getStringExtra(BuyResellerActivity.PRODUCT_ID) == null||
                getIntent().getStringExtra(BuyResellerActivity.PRODUCT_NAME) == null||
                getIntent().getStringExtra(BuyResellerActivity.PRODUCT_IMAGE) == null||
                getIntent().getStringExtra(BuyResellerActivity.PRODUCT_BG) == null||
                getIntent().getStringExtra(BuyResellerActivity.PRODUCT_PRICE) == null){

            finish();
            return;
        }else {
            productId = getIntent().getStringExtra(BuyResellerActivity.PRODUCT_ID);
            productName = getIntent().getStringExtra(BuyResellerActivity.PRODUCT_NAME);
            productImage = getIntent().getStringExtra(BuyResellerActivity.PRODUCT_IMAGE);
            productBackground = getIntent().getStringExtra(BuyResellerActivity.PRODUCT_BG);
            productPrice = getIntent().getStringExtra(BuyResellerActivity.PRODUCT_PRICE);
        }
        results = new ArrayList<>();

        buynowButton = (Button) findViewById(R.id.btnBuyNow);
        cancelButton = (Button) findViewById(R.id.btnCancel);
        ImageView mBackgroundProduct = (ImageView) findViewById(R.id.buy_bgimage);
        ImageView mProductImage = (ImageView) findViewById(R.id.buy_productimages);
        TextView mProductTitle = (TextView) findViewById(R.id.buy_producttitle);
        TextView mProductPrice = (TextView) findViewById(R.id.buy_productprice);
        textView4 = (TextView) findViewById(R.id.textView4);
        info1 = (TextView) findViewById(R.id.buy_info1);
        info2 = (TextView) findViewById(R.id.buy_info2);
        info3 = (TextView) findViewById(R.id.buy_info3);
        buy_desc = (TextView) findViewById(R.id.buy_description);
        resellerPassword = (EditText) findViewById(R.id.reseller_password);
        containerResellerbuy = (RelativeLayout) findViewById(R.id.container_resellerbuy);
        invoice_word1 = (TextView) findViewById(R.id.invoice_word1);
        invoice_word2 = (TextView) findViewById(R.id.invoice_word2);
        invoice_word3 = (TextView) findViewById(R.id.invoice_word3);
        invoice_word4 = (TextView) findViewById(R.id.invoice_word4);
        invoice_word5 = (TextView) findViewById(R.id.invoice_word5);
        invoice_word6 = (TextView) findViewById(R.id.invoice_word6);
        invoice_word7 = (TextView) findViewById(R.id.invoice_word7);
        invoice_word8 = (TextView) findViewById(R.id.invoice_word8);
        invoice_word9 = (TextView) findViewById(R.id.invoice_word9);
        invoice_word10 = (TextView) findViewById(R.id.invoice_word10);
        invoice_word11 = (TextView) findViewById(R.id.invoice_word11);
        invoice_word12 = (TextView) findViewById(R.id.invoice_word12);
        invoice_word13 = (TextView) findViewById(R.id.invoice_word13);
        invoice_word14 = (TextView) findViewById(R.id.invoice_word14);
        invoice_word15 = (TextView) findViewById(R.id.invoice_word15);
        invoice_word16 = (TextView) findViewById(R.id.invoice_word16);
        invoice_word17 = (TextView) findViewById(R.id.invoice_word17);
        invoice_word18 = (TextView) findViewById(R.id.invoice_word18);
        invoice_word19 = (TextView) findViewById(R.id.invoice_word19);
        invoice_word20 = (TextView) findViewById(R.id.invoice_word20);
        invoice_word21 = (TextView) findViewById(R.id.invoice_word21);
        invoice_word22 = (TextView) findViewById(R.id.invoice_word22);
        invoice_word23 = (TextView) findViewById(R.id.invoice_word23);
        invoice_word24 = (TextView) findViewById(R.id.invoice_word24);
        invoice_word25 = (TextView) findViewById(R.id.invoice_word25);
        invoice_word26 = (TextView) findViewById(R.id.invoice_word26);
        invoice_word27 = (TextView) findViewById(R.id.invoice_word27);
        invoice_word28 = (TextView) findViewById(R.id.invoice_word28);
        invoice_word29 = (TextView) findViewById(R.id.invoice_word29);
        invoice_word30 = (TextView) findViewById(R.id.invoice_word30);
        invoice_word31 = (TextView) findViewById(R.id.invoice_word31);
        invoice_word32 = (TextView) findViewById(R.id.invoice_word32);
        invoice_word33 = (TextView) findViewById(R.id.invoice_word33);
        invoice_word34 = (TextView) findViewById(R.id.invoice_word34);
        invoice_word35 = (TextView) findViewById(R.id.invoice_word35);

        invoice_beforebuy = (ImageView) findViewById(R.id.invoice_beforebuy);
        invoice_subcontainer = (LinearLayout) findViewById(R.id.invoice_subcontainer);

        buynowButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        mProductTitle.setText(productName);
        mProductPrice.setText(productPrice);
        Glide.with(BuyResellerActivity.this)
                .load(productBackground).centerCrop()
                .error(R.drawable.ic_error_loadimage)
                .crossFade(Constant.ITEM_CROSSFADEDURATION)
                .into(mBackgroundProduct);
        mBackgroundProduct.setImageAlpha(Constant.DEF_BGALPHA);

        Glide.with(BuyResellerActivity.this)
                .load(productImage).centerCrop()
                .error(R.drawable.ic_error_loadimage)
                .crossFade(Constant.ITEM_CROSSFADEDURATION)
                .into(mProductImage);

        getDetailProduct();
    }

    private void buyNowReseller(){
        Helper.showProgressDialog(this, R.string.load_vouchercode);

        String deviceId = PreferenceUtils
                .getSinglePrefrenceString(BuyResellerActivity.this, R.string.settings_def_storeidevice_key);
        String email = PreferenceUtils
                .getSinglePrefrenceString(BuyResellerActivity.this, R.string.settings_def_storeemail_key);
        String customerId = PreferenceUtils
                .getSinglePrefrenceString(BuyResellerActivity.this, R.string.settings_def_uid_key);
        String token = PreferenceUtils
                .getSinglePrefrenceString(BuyResellerActivity.this, R.string.settings_def_storeaccess_token_key);
        String sellerId = PreferenceUtils
                .getSinglePrefrenceString(BuyResellerActivity.this, R.string.settings_def_sellerid_key);
        String sellerShopName = PreferenceUtils
                .getSinglePrefrenceString(BuyResellerActivity.this, R.string.settings_def_sellershopname_key);
        String sellerKasirName = PreferenceUtils
                .getSinglePrefrenceString(BuyResellerActivity.this, R.string.settings_def_sellerkasirname_key);
        
        String password = resellerPassword.getText().toString();

        if (password.isEmpty() || password.equals("")){
            Helper.snacbarError(R.string.please_fill_password, containerResellerbuy);
            Helper.hideProgressDialog();
            return;
        }

        HashMap<String, String> data = new HashMap<>();
        data.put("device_id", deviceId);
        data.put("product_id", "3169"); //TODO : if testing done, change to productid
        data.put("email", email);
        data.put("customerId", customerId);
        data.put("session_name", token);
        data.put("seller_id", sellerId);
        data.put("seller_password", password);
        data.put("seller_shop_name", sellerShopName);
        data.put("seller_kasir_name", sellerKasirName);

        Call<VoucherprintResponse> buyProduct = Eztytopup.getsAPIService().getBuyreseller(data);
        buyProduct.enqueue(new Callback<VoucherprintResponse>() {
            @Override
            public void onResponse(Call<VoucherprintResponse> call,
                                   Response<VoucherprintResponse> response) {

                if (response.isSuccessful()) {

                    if (response.body().status.code
                            .equals(String.valueOf(HttpURLConnection.HTTP_OK))) {

                        // inform history fragment, to call getHistory API
                        listener.buyNowClick();

                        invoice_word1.setText(response.body().result.baris01.trim());
                        invoice_word2.setText(response.body().result.baris02.trim());
                        invoice_word3.setText(response.body().result.baris03.trim());
                        invoice_word4.setText(response.body().result.baris04.trim());
                        invoice_word5.setText(response.body().result.baris05.trim());
                        invoice_word6.setText(response.body().result.baris06.trim());
                        invoice_word7.setText(response.body().result.baris07.trim());
                        invoice_word8.setText(response.body().result.baris08.trim());
                        invoice_word9.setText(response.body().result.baris09.trim());
                        invoice_word10.setText(response.body().result.baris10.trim());
                        invoice_word11.setText(response.body().result.baris11.trim());
                        invoice_word12.setText(response.body().result.baris12.trim());
                        invoice_word13.setText(response.body().result.baris13.trim());
                        invoice_word14.setText(response.body().result.baris14.trim());
                        invoice_word15.setText(response.body().result.baris15.trim());
                        invoice_word16.setText(response.body().result.baris16.trim());
                        invoice_word17.setText(response.body().result.baris17.trim());
                        invoice_word18.setText(response.body().result.baris18.trim());
                        invoice_word19.setText(response.body().result.baris19.trim());
                        invoice_word20.setText(response.body().result.baris20.trim());
                        invoice_word21.setText(response.body().result.baris21.trim());
                        invoice_word22.setText(response.body().result.baris22.trim());
                        invoice_word23.setText(response.body().result.baris23.trim());
                        invoice_word24.setText(response.body().result.baris24.trim());
                        invoice_word25.setText(response.body().result.baris25.trim());
                        invoice_word26.setText(response.body().result.baris26.trim());
                        invoice_word27.setText(response.body().result.baris27.trim());
                        invoice_word28.setText(response.body().result.baris28.trim());
                        invoice_word29.setText(response.body().result.baris29.trim());
                        invoice_word30.setText(response.body().result.baris30.trim());
                        invoice_word31.setText(response.body().result.baris31.trim());
                        invoice_word32.setText(response.body().result.baris32.trim());
                        invoice_word33.setText(response.body().result.baris33.trim());
                        invoice_word34.setText(response.body().result.baris34.trim());
                        invoice_word35.setText(response.body().result.baris35.trim());

                        //temp if user want to print, on this layout
                        responseResult = response;

                        //save last object product buy, to SharedPreference
                        VoucherprintResponse.setUserInstance(response.body().result);
                        Gson gson = new Gson();
                        String json = gson.toJson(VoucherprintResponse.getInstance());
                        PreferenceUtils.saveLastProduct(json);

                        invoice_beforebuy.setVisibility(View.GONE);
                        invoice_subcontainer.setVisibility(View.VISIBLE);

                        resellerPassword.getText().clear();
                        resellerPassword.setKeyListener(null);
                        buynowButton.setVisibility(View.GONE);
                        cancelButton.setText(R.string.done);

                    } else if (response.body().status.code
                            .equals(String.valueOf(HttpURLConnection.HTTP_UNAUTHORIZED))) {

                        Toast.makeText(BuyResellerActivity.this, response.body().status.getMessage(),
                                Toast.LENGTH_SHORT).show();

                        PreferenceUtils.destroyUserSession(BuyResellerActivity.this);
                        Login.start(BuyResellerActivity.this);

                    } else {
                        Helper.snacbarError(response.body().status.getMessage(),
                                containerResellerbuy);
                    }

                    Helper.hideProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<VoucherprintResponse> call, Throwable t) {
                Helper.apiSnacbarError(BuyResellerActivity.this, t, containerResellerbuy);
                Helper.hideProgressDialog();
            }
        });
    }


    private void getDetailProduct() {
        Helper.showProgressDialog(this, R.string.getting_productdetail);

        Call<DetailProductResponse> product = Eztytopup.getsAPIService().
                getDetailProduct(productId);
        product.enqueue(new Callback<DetailProductResponse>() {
            @Override
            public void onResponse(Call<DetailProductResponse> call,
                                   Response<DetailProductResponse> response) {
                if (response.isSuccessful() &&
                        response.body().status.getCode()
                                .equals(String.valueOf(HttpURLConnection.HTTP_OK))) {

                    results.addAll(response.body().result);
                    DetailProductResponse.Result r = results.get(0);

                    if (r.getInfo1() != null && !r.getInfo1().equals("")) {
                          info1.setVisibility(View.VISIBLE);
                          info1.setText(r.getInfo1());
                    }
                    if (r.getInfo2() != null && !r.getInfo2().equals("")) {
                          info2.setVisibility(View.VISIBLE);
                          info2.setText(r.getInfo2());
                    }
                    if (r.getInfo3() != null && !r.getInfo3().equals("")) {
                          info3.setVisibility(View.VISIBLE);
                          info3.setText(r.getInfo3());
                    }
                    if (r.getDescription() != null && !r.getDescription().equals("")) {
                          buy_desc.setVisibility(View.VISIBLE);
                          buy_desc.setText(r.getDescription());
                    }


                }else {
                    Toast.makeText(BuyResellerActivity.this, response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }

                Helper.hideProgressDialog();
            }
            @Override
            public void onFailure(Call<DetailProductResponse> call, Throwable t) {
                Helper.apiSnacbarError(BuyResellerActivity.this, t, containerResellerbuy);
                Helper.hideProgressDialog();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_act_buynowreseller, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_print:
                if (responseResult != null) {
                    if (!Eztytopup.getmBTprintService().isAvailable()) {
                        Toast.makeText(BuyResellerActivity.this, R.string.bluetooth_notfound,
                                Toast.LENGTH_SHORT).show();

                    } else if (!Eztytopup.getmBTprintService().isBTopen()) {

                        // is blutooth Enable on that device?
                        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);

                    } else if (!Eztytopup.getIsPrinterConnected()) {

                        // is bluetooth connected to printer?
                        Intent serverIntent = new Intent(BuyResellerActivity.this,
                                DeviceListActivity.class);
                        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);

                    } else {
                        makeProgressprint(false);
                        bluetoothPrint(responseResult);
                    }

                } else {
                    Helper.snacbarError(R.string.buy_first, containerResellerbuy);
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void makeProgressprint(boolean isSunmi) {
        if (PreferenceUtils.getLastProduct() != null) {
            Handler handler = new Handler();
            Helper.showProgressDialog(this, R.string.print_onprogress);
            handler.postDelayed(new Runnable() {
                public void run() {
                    Helper.hideProgressDialog();
                }
            }, isSunmi ? Constant.SUNMI_PRINT_DILAOGTIME : Constant.BT_PRINT_DILAOGTIME);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBuyNow:
                buyNowReseller();
                break;
            case R.id.btnCancel:
                finish();
                break;
        }
        
    }

    @SuppressLint("SdCardPath")
    private Boolean printImage() {
        File file = new File(Constant.DEF_PATH_IMAGEPRINT);
        if (!file.exists()
                && !PreferenceUtils.getSinglePrefrenceString(this,
                R.string.settings_def_sellerprintlogo_key).equals(Constant.PREF_NULL)
                && PermissionHelper.isPermissionGranted(
                        BuyResellerActivity.this, new String[]
                        {Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE})) {

            Helper.downloadFile(this, PreferenceUtils.getSinglePrefrenceString(this,
                    R.string.settings_def_sellerprintlogo_key));

            Toast.makeText(this, R.string.please_wait_imageprint,
                    Toast.LENGTH_SHORT).show();

            return Boolean.FALSE;
        } else {
            byte[] sendData = null;
            PrintPic pg = new PrintPic();
            pg.initCanvas(384);
            pg.initPaint();
            pg.drawImage(100, 0, Constant.DEF_PATH_IMAGEPRINT);
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
                    Eztytopup.setCon_dev(Eztytopup.getmBTprintService().getDevByMac(address));
                    Eztytopup.getmBTprintService().connect(Eztytopup.getCon_dev());
                }
                break;
        }
    }

    @Override
    public void initView() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_buyreseller;
    }

    private boolean validatePrint(String word) {
        if (!word.isEmpty()) {
            Eztytopup.getmBTprintService().sendMessage(word, "ENG");
            return true;
        } else {
            return false;
        }
    }

    /**
     * 1. if you use looping, you cant expect what data perbaris is, becaouse response
     * data not consitent.
     * like now baris '5' respone is line, like this ("===="). 19 may 2017
     * mybe in future isnot.
     * this best way i have.
     * 2. kesepakatan dgn ezytopup jika response string == "" maka print berhenti
     *    jika response string == " " atau isEmpety() print lanjut
     * */
    private void bluetoothPrint(Response<VoucherprintResponse> response){
        // logo print
//        printImage();
        byte[] cmd = new byte[5];
        cmd[0] = 0x1b;
        cmd[1] = 0x21;
        cmd[2] |= 0x10;
        Eztytopup.getmBTprintService().write(cmd);
        if (!validatePrint(response.body().result.baris01)) return;
        cmd[2] &= 0xEF;
        Eztytopup.getmBTprintService().write(cmd);
        if (!validatePrint(response.body().result.baris02)) return;
        if (!validatePrint(response.body().result.baris03)) return;
        if (!validatePrint(response.body().result.baris04)) return;
        cmd[2] |= 0x10;
        Eztytopup.getmBTprintService().write(cmd);
        if (!validatePrint(response.body().result.baris05)) return;
        cmd[2] &= 0xEF;
        Eztytopup.getmBTprintService().write(cmd);
        if (!validatePrint(response.body().result.baris06)) return;
        if (!validatePrint(response.body().result.baris07)) return;
        if (!validatePrint(response.body().result.baris08)) return;
        if (!validatePrint(response.body().result.baris09)) return;
        if (!validatePrint(response.body().result.baris10)) return;
        if (!validatePrint(response.body().result.baris11)) return;
        if (!validatePrint(response.body().result.baris12)) return;
        if (!validatePrint(response.body().result.baris13)) return;
        cmd[2] |= 0x10;
        Eztytopup.getmBTprintService().write(cmd);
        if (!validatePrint(response.body().result.baris14)) return;
        cmd[2] &= 0xEF;
        Eztytopup.getmBTprintService().write(cmd);
        if (!validatePrint(response.body().result.baris15)) return;
        if (!validatePrint(response.body().result.baris16)) return;
        if (!validatePrint(response.body().result.baris17)) return;
        if (!validatePrint(response.body().result.baris18)) return;
        if (!validatePrint(response.body().result.baris19)) return;
        if (!validatePrint(response.body().result.baris20)) return;
        if (!validatePrint(response.body().result.baris21)) return;
        if (!validatePrint(response.body().result.baris22)) return;
        if (!validatePrint(response.body().result.baris23)) return;
        if (!validatePrint(response.body().result.baris24)) return;
        if (!validatePrint(response.body().result.baris25)) return;
        if (!validatePrint(response.body().result.baris26)) return;
        if (!validatePrint(response.body().result.baris27)) return;
        if (!validatePrint(response.body().result.baris28)) return;
        if (!validatePrint(response.body().result.baris29)) return;
        if (!validatePrint(response.body().result.baris30)) return;
        if (!validatePrint(response.body().result.baris31)) return;
        if (!validatePrint(response.body().result.baris32)) return;
        if (!validatePrint(response.body().result.baris33)) return;
        if (!validatePrint(response.body().result.baris34)) return;
        if (!validatePrint(response.body().result.baris35)) ;

    }

    public interface BuynowInterface {
        void buyNowClick();
    }
}
