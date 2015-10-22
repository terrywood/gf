package com.gt.bmf;

public class BmfConstants {

    /**
     * Attribute Name
     */
    public static final String GLOBAL_EDITING = "E";
    public static final String GLOBAL_VALID = "V";
    public static final String GLOBAL_ACTIVE = "A";
    public static final String GLOBAL_INVALID = "I";
    public static final String GLOBAL_DELETE = "D";
    public static final String GLOBAL_UPDATE = "U";
    public static final String GLOBAL_YES = "Y";
    public static final String GLOBAL_NO = "N";

    public static final String METHOD_GET_COMPANY = "getCompanyId";
    public static final String METHOD_SET_COMPANY = "setCompanyId";

    public static final String BMF_CURRENT_USER_PERMISSION = "bmf_user_permission";
    public static final String BMF_CURRENT_USER = "currentUser";
    public static final String BMF_APPLICATION_NAME_SETTINGS = "bmf_settings";

    public static int DEFAULT_PAGE_SIZE = 10;

    public static final String SUBMIT_FORM = "submitForm";
    public static final String SAVE_FORM = "save";
    public static final String UPDATE_FORM = "update";

    public static final int SAVE_CHECK = 0;
    public static final int UPDATE_CHECK = 1;

    public static final String PASSWORD_SALT = "bmf";

    public static final String DOCUMENT_SUBMISSION_CODE = "D";


    /**
     * Api
     */
    public static String API_STATUS_SUCCESS = "0";
    public static String API_STATUS_FAIL = "1";
    public static String SUCCESS_MSG = "success";
    public static String ERROR_MSG = "error";
    public static final String MSG = "msg";
    public static final String ALERT_MSG_NAME = "alertMessage";


    /**
     * android
     */
    public static final Integer PUSH_GCM_TRY_AGENT = 5;
    public static final String ANDROID = "a";

    /**
     * ios
     */
    public static final String IOS = "i";

    /**
     * Push Message status
     */
    public static final String PUSH_MSG_STATUS_WAITING = "W";
    public static final String PUSH_MSG_STATUS_PROCESSING = "P";
    public static final String PUSH_MSG_STATUS_ERROR = "E";
    public static final String PUSH_MSG_STATUS_FINISHED = "F";
    public static final String PUSH_MSG_STATUS_FEATURE = "X";
    public static final String PUSH_MSG_STATUS_EDITING = "M";

    /**
     * push message type
     */
    public static final String PUSH_MSG_TYPE_ALL = "A";
    public static final String PUSH_MSG_TYPE_GROUP = "G";
    public static final String PUSH_MSG_TYPE_USER = "U";
    public static final int BATCH_SEND_NUM = 50;

    /**
     * File Upload
     */
    public static final String PDF = ".pdf";
    public static final String EXCEL = ".xls";
    public static final String IMG = ".jpg.png.gif.bmp.JPG.PNG.GIF.BMP";
    public static final String ZIP = ".zip.ZIP";
    public static final String XML = ".xml.XML";

    public static final String EXCEL_FOLDER = "/excel/";

    public static final String ADMIN_USER_EXCEL = "AdminUser";
    public static final String ENQUIRY_FORM_EXCEL = "EnquiryForm";
    public static final String LOAN_FORM_EXCEL = "LoanForm";
    public static final String MGM_FORM_EXCEL = "MgmForm";
    public static final String REQUEST_LIMIT_UP_RECORD_EXCEL = "RequestLimitUpRecord";

    /*
     * version
     */
    public static final String VER_ACTION_NONE = "NONE";
    public static final String VER_ACTION_FORCE_UPDATE = "FORCE";
    public static final String VER_ACTION_ALERT = "ALERT";
    public static final String VER_ACTION_NOT_FOUND = "NOT_FOUND";

    public static final String SUPPORT_LANGUAGES = "supportLanguages";


    public static final String UPLOAD_TYPE_MASTER_FIXTURE= "MF";
    public static final String UPLOAD_TYPE_MASTER_SUPPLIER = "MS";

    public static final String UPLOAD_TYPE_STORE_FIXTURE = "SF";
    public static final String UPLOAD_TYPE_STORE_SUPPLIER = "SS";

    public static final String UPLOAD_TYPE_BUYER= "BUYER";
    public static final String UPLOAD_TYPE_EXPORT= "E";


    //language
    public static final String LANG_EN = "en";
    public static final String LANG_TC = "tc";
    public static final String LANG_SC = "sc";

    public static final String FIXTURE_TYPE_THUMBNAIL_PATH = "/fixtureType/thumbnail";


    public static final String SUPPLIER_BOOTH_LIGHT_BOX_SB = "SB";

    public static final String SUPPLIER_BOOTH_LIGHT_BOX_LB = "LB";



    public static final String AUDIT_STATUS_ASSIGNED = "Assigned";
    public static final String AUDIT_STATUS_FINISHED = "Finished";
    public static final String AUDIT_STATUS_MISSED = "Missed";

    public static final String csvEncryptKey = "This_is_fortress_key";



}
