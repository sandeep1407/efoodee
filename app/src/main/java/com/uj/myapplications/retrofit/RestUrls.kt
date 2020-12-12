package com.uj.myapplications.utility

/**
 * Author : Umesh Jangid
 * Version: 1.0
 * 21/2/2019
 */
class RestUrls {
    companion object {
        //onst val BASE_URL = "https://ec2-54-225-35-190.compute-1.amazonaws.com"
        // const val BASE_URL = "https://www.profuseonline.com:31000/api/"
        const val BASE_URL = "https://ec2-54-225-35-190.compute-1.amazonaws.com:31000/api/"
        //const val BASE_URL ="https://www.profuseonline.com:31000/api/"
        const val LOGIN_URL = BASE_URL + "auth/signin"
        const val SIGN_UP_URL = BASE_URL + "auth/signup"
        const val VERIFY_OTP_URL = BASE_URL + "auth/verify/otp"
        const val FORGOT_PASSWORD_URL = BASE_URL + "auth/forgotpassword"
        const val CHANGE_PASSWORD_URL = BASE_URL + "auth/change/password"
        const val UPDATE_BANKDETAILS = BASE_URL + "suppliers/updatebankdetails"
        const val NEED_FSSAI_HELP = BASE_URL + "suppliers/need/help"
        const val GET_MENU = BASE_URL + "suppliers/getyourmenu"
        const val GET_MENU_Detail = BASE_URL + "suppliers/getmenudetails?menu_id="
        const val ADD_MENU = BASE_URL + "suppliers/addmenu"
        const val UPDATE_MENU = BASE_URL + "suppliers/updatemenu"
        const val IS_SEESION_VALID = BASE_URL + "auth/is_session_valid"
        const val GET_ORDERS = BASE_URL + "suppliers/get/orders"
        const val GET_ORDER_DETAIL = BASE_URL + "suppliers/order/96?app_type=supplier" // Query Parameter
        const val UPDATE_ORDER_PROGRESS = BASE_URL + "suppliers/order/96?app_type=supplier" // Query Parameter
        const val USER_ADDITIONAL_DETAILS = BASE_URL + "suppliers/updateadditionaldetails"
        const val ASK_FOR_REVIEW = BASE_URL + "suppliers/askforreview"
        const val CONTACT_MYMA = BASE_URL + "suppliers/contact/mymaa"
        const val GET_USER_PROFILE = BASE_URL + "suppliers/getprofile?user_id="
        const val GET_BANK_DETAIL = BASE_URL + "suppliers/getbankdetails?mobile="
        const val UPDATE_PROFILE = BASE_URL + "suppliers/update/profile"

        /* const val  GET_MENU= BASE_URL + "/suppliers/askforreview"
         const val  GET_MENU= BASE_URL + "suppliers/getyourmenu"
         https://www.profuseonline.com:31000/api/suppliers/update/profile
         https://www.profuseonline.com:31000/api/suppliers/getprofile?user_id=5c7e95ec19443abe6e2190e2
         https://www.profuseonline.com:31000/api/suppliers/contact/mymaa
         const val  GET_MENU= BASE_URL + "suppliers/getyourmenu"
         const val  GET_MENU= BASE_URL + "suppliers/getyourmenu"*/
    }
}
