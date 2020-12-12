package com.uj.myapplications.utility

import com.uj.myapplications.pojo.ComResLoginPojo
import com.uj.myapplications.pojo.CommonRegisterResponsePojo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Author : Umesh Jangid
 * Version: 1.0
 * 21/2/2019
 */
interface RestInterface {
    @POST(RestUrls.LOGIN_URL)
    fun login(@Body params: HashMap<String, String>): Call<ComResLoginPojo>

    @POST(RestUrls.SIGN_UP_URL)
    fun signUp(@Body params: HashMap<String, String>): Call<CommonRegisterResponsePojo>

    /*  @POST(RestUrls.IDN_INFO_URL)
  fun getIDNInfo(@Body params: HashMap<String, String>): Call<IdnInfoPojo>

  @POST(RestUrls.BOOK_DOMAIN_URL)
  fun getDomainSearch(@Body params: HashMap<String, String>): Call<ResponseBody>

  @POST(RestUrls.WALLET_URL)
  fun walletBalance(@Body params: HashMap<String, String>): Call<WalletBalancePojo>*/

    /*  @POST(RestUrls.WHOIS_INFO_URL)
      fun whoIs(@Body params: HashMap<String, String>): Call<WhoIsPojo>

      @POST(RestUrls.DOMAIN_LIST_URL)
      fun myDomainList(@Body params: HashMap<String, String>): Call<MyDomainsPojo>

      @POST(RestUrls.LOGIN_URL)
      fun forgetPassword(@Body params: HashMap<String, String>): Call<ForgetPasswordPojo>

      @POST(RestUrls.DNS_RECORD_URL)
      fun dnsRecords(@Body params: HashMap<String, String>): Call<DnsRecordPojo>

      @POST(RestUrls.DOMAIN_INFO_URL)
      fun updateNameServer(@Body params: HashMap<String, String>): Call<UpdateNameServerPojo>

      @POST(RestUrls.DOMAIN_INFO_URL)
      fun updateDomainInfo(@Body params: HashMap<String, String>): Call<UpdateDomainInfoPojo>

      @POST(RestUrls.DOMAIN_INFO_URL)
      fun getDomainInfo(@Body params: HashMap<String, String>): Call<GetDomainInfoPojo>

      @POST(RestUrls.DNS_RECORD_URL)
      fun insertUpdateDns(@Body params: HashMap<String, String>): Call<InsertUpdateDnsPojo>

      @POST(RestUrls.DNS_RECORD_URL)
      fun deleteDns(@Body params: HashMap<String, String>): Call<DeleteDnsPojo>

      @POST(RestUrls.LOGIN_URL)
      fun changePassword(@Body params: HashMap<String, String>): Call<ChangePasswordPojo>

      @POST(RestUrls.BOOK_DOMAIN_URL)
      fun registerDomain(@Body params: HashMap<String, String>): Call<RegisterDomainPojo>

      @POST(RestUrls.COMMON_URL)
      fun getAddress(@Body params: HashMap<String, String>): Call<DomainAddressPojo>

      @POST(RestUrls.DOMAIN_RENEW_URL)
      fun getRenewInfo(@Body params: HashMap<String, String>): Call<RenewDomainInfoPojo>*/
}