package com.wk.guestpass.guard.apputils;

/**
 * Created by osx on 14/11/17.
 */

public interface AppConstants {


    String regBean = "reg_bean";
    String TERM_URL = "http://www.hollame.org/terms-of-service.html";
    String POLICY_URL = "http://www.hollame.org/privacy-policy.html";
    String HELP_URL = "https://www.google.com";

    //User table
    String USER_TABLE_NAME = "_User";
    String NAME = "name";
    String USER_NAME = "username";
    String USER_PASSWORD = "password";
    String USER_EMAIL = "email";
    String USER_DP = "dp";
    String USER_BALANCE = "Balance";

    //ServiceInfo table
    String SERVICE_INFO_TABLE_NAME = "ServicesInfo";

    String SERVICE_INFO_NAME = "service_name";
    String SERVICE_CATEGORY_TYPE = "service_category_type";
    String SERVICE_CATEGORY = "service_category";
    String SERVICE_LIST = "service_list";

    //ReqLog Table
    String REQUEST_TABLE_NAME = "ReqLog";
    String REQUEST_SERVICE_NAME = "serviceName";
    String REQUEST_USER_ID = "userID";
    String REQUEST_PRICE  = "price";
    String REQUEST_LONG_DESC = "longDesc";
    String REQUEST_DATE = "date";
    String REQUEST_STATUS = "status";
    String REQUEST_SERVICE_ID = "serviceRequestId";
    String REQUEST_USER_NAME = "username";
    String REQUEST_ICON = "icon";
    String REQUEST_TYPE = "type";
    String REQUEST_LONG = "long";
    String REQUEST_LOG_TYPE= "logType";
    String REQUEST_LAT = "lat";


    //Log table
    String LOG_TABLE_NAME = "Log";

    //Review Table
    String REVIEW_TABLE_NAME = "Review";
    String REVIEW_USER_ID = "UserID";
    String REVIEW_RATING = "Rating";
    String REVIEW_LOG_ID = "LogId";
    String REVIEW_COMMENT = "Comment";
    String REVIEW_DATE = "Date";
    String REVIEW_PROVIDER_ID = "ProviderID";


    //Payout Table
    String PAYOUT_TABLE_NAME = "Payout";
    String PAYOUT_PROVIDER_ID = "ProviderID";
    String PAYOUT_AMOUNT = "amount";
    String PAYOUT_SORT_CODE = "sortcode";
    String PAYOUT_ACCOUNT_NO = "accNo";
    String PAYOUT_DATE = "Date";

    //Service Table
    String SERVICE_TABLE_NAME = "Service";
    String SERVICE_PROVIDER_ID = "ProviderID";
    String SERVICE_NAME = "ServiceName";

    //CLOUD FXN
    String FXN_COMPLETE_JOB = "completeJob";
    String FXN_PAY_USER = "payUser";
    String FXN_SEND_PUSH = "sendPush";

    // SOME COMMAN KEYS
    String CREATED_AT = "createdAt";
    String _OBJECT_ID = "objectID";
    String _USER_ID = "userID";
    String _PROVIDER_ID = "providerID";
    String _AMOUNT = "amount";
    String _RATING = "rating";
    String _EMAIL = "email";
    String _MESSAGE = "msg";
    String _LAT = "lat";
    String _LONG = "long";
    String _ICON ="Icon";





    //---------------FIREBASE NODES----------------------
    String NODE_USERS = "users";
    String NODE_CONVERSATIONS = "conversations";
    String NODE_LOCATION = "location";
    String NODE_CREDENTIALS = "credentials";
    String USER_DETAILS = "userDetails";


    //--------------- all other constants --------------
    String CONST_SERVICE = "service";

    String CONST_REQUESTED = "requested";
    String CONST_ACCEPTED = "accepted";
    String CONST_INCOMPLETE = "incomplete";
    String CONST_REJECTED = "rejected";
    String CONST_OFFERED = "offerred";
    String CONST_CANCELLED = "cancelled";

    String CONST_SENT = "sent";
    String CONST_NAME = "name";
    String CONST_SELECTED_TAB = "selected_tab";
    String CONST_LANGUAGE = "en";
    String CONST_CURRENCY_LOCALE = "GB";

}
