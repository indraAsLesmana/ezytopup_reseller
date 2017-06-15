package com.ezytopup.reseller.utility;

/**
 * Created by indraaguslesmana on 3/31/17.
 */

public class Constant {
    /********
     * Printer configuration
     * "width" count by each character
     * "algn" is font align
     ********/

    private static final int algn_left = 0;
    private static final int algn_center = 1;
    private static final int algn_right = 2;

    private static final int name_width = 12;
    private static final int quanity_width = 3;
    private static int price_width = 6;
    private static final int total_width = 9;

    /* header table*/
    public static final String ROW_NAME = "Name";
    public static final String ROW_QUANTITY = "Qty";
    public static final String ROW_PRICE = "Price";
    public static final String ROW_TOTAL_PRICE = "Total";

    /* default width for table*/
    public static final int[] width = new int[]{name_width, quanity_width, price_width, total_width};
    /*default align for header table*/
    public static final int[] align_header = new int[]{ algn_left, algn_center, algn_right, algn_right };
    /* default align for content table*/
    public static final int[] align = new int[]{algn_left, algn_right, algn_right, algn_right};

    /*****
     * end printer configuration
     *
     * ***/

    public static final String API_APIARY_ENDPOINT = "http://private-fc734-ezytopup.apiary-mock.com/";

    public static final String LOGIN_PROVIDER_EMAIL = "email";

    public static final  String API_ENDPOINT = "https://www.gsshop.co.id/";

    public static final String API_URL_PARAM1 = "H5c30S5aHa9c45J297a680beB0795f87W131556M9b82e471ee1c2baS82699e9eWa218G2c05fG3e259G971562052D03a78c81D6afdb5e92Gb=";
    public static final String API_URL_PARAM1_VALUE = "d61H266a2d1c5ca33475257S13fb490efba4f17ee0013596Dc03da1372e31c6a94f0eb608fbc834af00e4G6141715b4981c41f0L6d2959b43a71b4576fSa66285cf486423fa70fa5b66f1d28b95O24f3d7b8465b0Gf19122d75Ubbe828aF3Tdd8845cc1a7W9bS";
    public static final String API_URL_PARAM2 = "Kb0a176e5O7efbaf58OaKaFdfd0C79e353e1dbFCcf2328Ic6477922S5b4a47b79120D0U5WddV0d2b225W0fdf5Q809Vc=";
    public static final String API_URL_PARAM2_VALUE = "Ezy_Apps_WGS";
    public static final String API_URL_GENERALUSAGE = "H5c30S5aHa9c45J297a680beB0795f87W131556M9b82e471ee1c2baS82699e9eWa218G2c05fG3e259G971562052D03a78c81D6afdb5e92Gb=d61H266a2d1c5ca33475257S13fb490efba4f17ee0013596Dc03da1372e31c6a94f0eb608fbc834af00e4G6141715b4981c41f0L6d2959b43a71b4576fSa66285cf486423fa70fa5b66f1d28b95O24f3d7b8465b0Gf19122d75Ubbe828aF3Tdd8845cc1a7W9bS&Kb0a176e5O7efbaf58OaKaFdfd0C79e353e1dbFCcf2328Ic6477922S5b4a47b79120D0U5WddV0d2b225W0fdf5Q809Vc=Ezy_Apps_WGS";

    public static final String temporaryToken = "4d0d9a51f6d19eed7aceccbdee98440e94543b6edf9c152c8365a2fee60a1ed030437bf1786d8674360fac4dab60eb06";
    /**
     * slider option
     * */
    public static final long HEADER_DURATION = 5000;
    public static final long TUTORIAL_DURATION = 8000;
    public static final int DEF_BGALPHA = 230;
    public static final int ITEM_CROSSFADEDURATION = 600;
    public static final String PREF_NULL = "-";
    public static final int SUNMI_PRINT_DILAOGTIME = 3000;
    public static final int BT_PRINT_DILAOGTIME = 5000;

    /**
     * */
    public static final String INTERNET_BANK = "101";
    public static final String BANK_TRANSFER = "102";
    public static final String CREADIT_CARD = "103";
    public static final String EZYTOPUP_WALLET = "104";

    /**
     * App settings
     * */
    public static final int PAYMENT_GRIDSETTINGS = 3;
    public static final boolean ENABLE_LOG = true;
    public static final String APP_NAME = "Ezytopup";
    public static boolean ENABLE_HOME_SWIPE = false;
    public static String DEF_PATH_IMAGEPRINT = "/mnt/sdcard/Ezytopup/print_logo.jpg";
    public static final String APP_FONT_PRINT = "fonts/lmmonocaps10-regular.otf";


    /**
     * Message log tag
     */
    public static final String TAG_LOG_API = APP_NAME + "_api";
    public static final String TAG_LOG_VERBOSE = APP_NAME + "_verbose";
    public static final String TAG_LOG_ERROR = APP_NAME + "_error";

    public static class PermissionCode {

        public static int STORAGE = 1;
        public static int LOCATION = 2;
        public static int TELEPHONY = 3;
    }
}
