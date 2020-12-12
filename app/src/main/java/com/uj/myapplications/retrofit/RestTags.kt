package com.uj.myapplications.utility

/**
 * Author : Umesh Jangid
 * Version: 1.0
 * 21/2/2019
 */
class RestTags {
    companion object {

        const val LOCATION_REQUEST = 1000
        const val GPS_REQUEST = 1001

        //Menu Types
        const val CURRENT: String = "current"
        const val RECENT: String = "recent"
        const val SCHEDULED: String = "scheduled"


        // Order Types
        const val COMPLETED: Int = 1
        const val PENDING: Int = 2
        const val MISSED: Int = 3

        // Service Type Constants

        const val DINE_IN: Int = 0 // Right now Work for all
        const val SELF_PICKUP: Int = 1
        const val SELF_DELIVERY: Int = 2
        const val MYMA_DELIVERY: Int = 3

        const val DINE_IN_STR = "dine_in"
        const val SELF_PICKUP_STR = "self_pickup"
        const val SELF_DELIVERY_STR = "self_delivery"
        const val MYMA_DELIVERY_STR = "myma_delivery"


        const val PAGE_MIN_LENGTH: Int = 1
        const val PAGE_MAX_LENGTH: Int = 10

        const val S_FLAG: String = "supplier"
        const val ORDER_PROGRESS_STATUS: String = "order_progress_status"
        const val SERVICE_TYPE: String = "service_type"
        const val TYPE: String = "type"
        const val PAGE: String = "page"
        const val PER_PAGE: String = "per_page"
        const val NO_INTERNET: String = "Please check your internet connection"
        const val PUBLIC_KEY: String = "f98b9125-782d-451b-bc64-109bcf757a0b"
        const val SOURCE: String = "source"
        const val KEY: String = "key"
        const val USER_NAME: String = "username"
        const val USER_TYPE: String = "app_type"
        const val USER_ID: String = "user_id"
        const val ACCESS_TOKEN: String = "access_token"
        const val PASSWORD: String = "password"
        const val NEW_PASSWORD: String = "new_password"
        const val CONFIRM_PASSWORD: String = "confirm_password"
        const val EMAIL: String = "email"
        const val FIRST_NAME: String = "firstname"
        const val LAST_NAME: String = "lastname"
        const val MOBILE: String = "mobile"

        const val SOURCE_VALUE: String = "mo"
        const val KEY_VALUE: String = "key"
        const val S_IDN_FLAG: String = "getIDNInfo"
        const val IDN_LANG: String = "idnLang"
        const val YEAR: String = "year"
        const val INVOICE: String = "invoice"
        const val CHECK_DOMAIN_FLAG: String = "checkdomain"

        const val NAME_SERVER1 = "nameserver1"
        const val NAME_SERVER2 = "nameserver2"

        /*Keys In Registration*/
        /*Keys In LoginClass*/

        /*Keys In ForgotClass*/
        const val OTP_TYPE: String = "otp_type"
        const val OTP: String = "otp"
        const val OTP_TYPE_REGISTER: String = "register"
        const val OTP_TYPE_RESET_PASS: String = "resetpass"

        @JvmField
        val DATE_OUTPUT_FORMAT = "dd MMM yyyy hh:mm"
        val DATE_OUTPUT_FORMAT_DAY = "EEE dd MMMM yyyy"
        @JvmField
        val DATE_INPUT_FORMAT = "yyyy-MM-dd"
        @JvmField
        val DATE_INPUT_FORMAT_HOURS = "yyyy-mm-ddThh:mi:ss.mmmZ"
        @JvmField
        val DATE_FORMAT_dd_MMM = "dd MMM"
        val DISPLAY_NAME: String? = "display_name"
        val ADDRESS: String? = "address"
        val BANK_NAME: String? = "bank_name"
        val IFSC_CODE: String? = "ifsc_code"
        val ACC_NUMBER: String? = "account_no"
        val FROM: String? = "FROM"
        val DEVICE_TOKEN: String? = "device_token"
        val DEVICE_TYPE: String? = "device_platform"
        val PLATFORM: Int? = 1 // 1= Android 2= iOS
        val ACTION: String = "EDIT"
    }
}