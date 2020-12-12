package com.uj.myapplications.utility;

import android.util.Log;

import butterknife.internal.Utils;
import okhttp3.*;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.*;

/**
 * Created by Umesh on 5/24/2017.
 */
public class OkhttpRequestUtils {
    public String TAG = "RESPONSE :";
    public static int TIMEOUT = 60000;
    // public static OkHttpClient client = new OkHttpClient();
    public static OkHttpClient client = getUnsafeOkHttpClient();

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    // GET code request code here
    public static Response doGetRequest(String url) throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        try {
            Request request = new Request.Builder()
                    .url(url).addHeader("Content-Type", "text/plain")
                    .build();
            Response response = client.newCall(request).execute();
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // GET code request code here
    public static Response doGetRequest(String url, String header_base64encoded) throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        try {
            Request request = new Request.Builder()
                    .url(url).addHeader("cache-control", "no-cache").addHeader("authorization", header_base64encoded)
                    .build();
            Response response = client.newCall(request).execute();
            Log.e("RESPONSE:", response.body() + "Code:" + response.code() + "Message:" + response.message());
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Post Request
    public static Response doPostRequest(String url, JSONObject json) throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        try {
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, json.toString());
            Request request = new Request.Builder()
                    .url(url).addHeader("cache-control", "no-cache").addHeader("content-type", "application/json")
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            Log.e("RESPONSE:", response.body() + "Code:" + response.code() + "Message:" + response.message());
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // GET code request code here
    public static Response doGetRequest(String url, String token, String app_type) throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        try {
            Request request = new Request.Builder()
                    .url(url).addHeader("cache-control", "no-cache").
                            addHeader("Authorization", token).addHeader("app_type", app_type).
                            addHeader("access_token", RestTags.PUBLIC_KEY)
                    .build();
            Response response = client.newCall(request).execute();
            Log.e("RESPONSE:", response.body() + "Code:" + response.code() + "Message:" + response.message());
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Response doPostRequest(String url, JSONObject json, String header_base64encoded) throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        try {
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, json.toString());
            Request request = new Request.Builder()
                    .url(url).addHeader("cache-control", "no-cache").addHeader("authorization", header_base64encoded)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            Log.e("RESPONSE:", response.body() + "Code:" + response.code() + "Message:" + response.message());
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Response doPostRequest(String url, JSONObject json, String token, String appType, String accessToken) throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        try {
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, json.toString());
            Request.Builder request1 = new Request.Builder();
            if (token != null && token.length() > 0) {
                request1.addHeader("authorization", token);
            }
            if (appType != null && appType.length() > 0) {
                request1.addHeader(RestTags.USER_TYPE, appType);
            }
            if (accessToken != null && accessToken.length() > 0) {
                request1.addHeader(RestTags.ACCESS_TOKEN, accessToken);
            }
            Request request = request1
                    .url(url).addHeader("cache-control", "no-cache")
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            Log.e("RESPONSE:", response.body() + "Code:" + response.code() + "Message:" + response.message());
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.connectTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES).build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Maintaing cache in this method.
    public static Response doGetRequest(String url, String header_base64encoded, boolean reportOnly) throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        if (reportOnly) {
            Request request = new Request.Builder()
                    .url(url).addHeader("cache-control", "no-cache").addHeader("authorization", header_base64encoded).cacheControl(new CacheControl.Builder()
                            .onlyIfCached()
                            .build())
                    .build();
            Response response = client.newCall(request).execute();
            Log.e("RESPONSE:", response.body() + "Code:" + response.code() + "Message:" + response.message());
            return response;
        }
        return null;
    }

    /**
     * Upload Image
     *
     * @param sourceImageFile
     * @return @flag 1= Add 2= Update
     */
    public static JSONObject menuResponse(Integer flag, String token, ArrayList<String> sourceImageFile, String jsonObjectAsString, String menuId) {
        try {
            String filename = "";
            MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
            multipartBuilder.setType(MultipartBody.FORM).addFormDataPart("menu_details", jsonObjectAsString);
            for (int i = 0; i < sourceImageFile.size(); i++) {
                File file = new File(sourceImageFile.get(i));
                if (file.exists()) {
                    Log.d("TAG", "File...::::" + file.getPath());
                    filename = UtilityClass.getTimeINMills() + "";
                    multipartBuilder.addFormDataPart("pics", filename + ".jpg", RequestBody.create(MediaType.parse("image/png"), sourceImageFile.get(i)));
                }
            }
            /**
             * OKHTTP3
             */
            RequestBody requestBody = multipartBuilder
                    .build();
            Request request = null;
            if (flag == 1) {
                request = new Request.Builder()
                        .addHeader("Authorization", token)
                        .addHeader("access_token", RestTags.PUBLIC_KEY)
                        .addHeader("app_type", RestTags.S_FLAG)
                        .url(RestUrls.ADD_MENU)
                        .post(requestBody)
                        .build();
            } else {
                request = new Request.Builder()
                        .addHeader("Authorization", token)
                        .addHeader("access_token", RestTags.PUBLIC_KEY)
                        .addHeader("app_type", RestTags.S_FLAG)
                        .addHeader("menu_id", menuId)
                        .url(RestUrls.UPDATE_MENU)
                        .post(requestBody)
                        .build();
            }

            //   OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.e("TAG", "Error: " + res);
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e("TAG", "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e("TAG", "Other Error: " + e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * Registration and Additnal Details
     *
     * @param
     * @return
     */
    public static JSONObject userAddtionalDetailsResponse(String token, String supplier_id, String fssaiImagePath, String panImage, String adhaarImage, String dlImage, String bikeRC, String jsonObjectAsString) {
        try {
            MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
            // FSSAI
            if (fssaiImagePath != null && fssaiImagePath.length() > 0 && !(UtilityClass.checkURL(fssaiImagePath))) {
                File file_fssai = new File(fssaiImagePath);
                Log.d("TAG", "File Fssai...::::" + file_fssai.getPath() + " : " + new File(file_fssai.getPath()).exists());
                multipartBuilder.addFormDataPart("fssai_doc", "fssaidoc.jpg", RequestBody.create(MediaType.parse("image/png"), file_fssai));
            } else {
                multipartBuilder.addFormDataPart("fssai_doc", "");
            }
            // PAN
            if (panImage != null && panImage.length() > 0 && !(UtilityClass.checkURL(panImage))) {
                File file = new File(panImage);
                Log.d("TAG", "File Pan...::::" + file.getPath() + " : " + new File(file.getPath()).exists());
                multipartBuilder.addFormDataPart("pan", "pan.jpg", RequestBody.create(MediaType.parse("image/png"), file));
            } else {
                multipartBuilder.addFormDataPart("pan", "");
            }
            //*************
            //Adhaar
            if (adhaarImage != null && adhaarImage.length() > 0 && !(UtilityClass.checkURL(adhaarImage))) {
                File file = new File(adhaarImage);
                Log.d("TAG", "File Adhaar...::::" + file.getPath() + " : " + new File(file.getPath()).exists());
                multipartBuilder.addFormDataPart("adhaar", "adhaar.jpg", RequestBody.create(MediaType.parse("image/png"), file));
            } else {
                multipartBuilder.addFormDataPart("adhaar", "");
            }
            //***************************
            //DL
            if (dlImage != null && dlImage.length() > 0 && !(UtilityClass.checkURL(dlImage))) {
                File file = new File(dlImage);
                Log.d("TAG", "File DL...::::" + file.getPath() + " : " + new File(file.getPath()).exists());
                multipartBuilder.addFormDataPart("driving_license", "dl.jpg", RequestBody.create(MediaType.parse("image/png"), file));
            } else {
                multipartBuilder.addFormDataPart("driving_license", "");
            }
            //***************************
            //Bike RC
            if (bikeRC != null && bikeRC.length() > 0 && !(UtilityClass.checkURL(bikeRC))) {
                File file = new File(bikeRC);
                Log.d("TAG", "File bikeRC...::::" + file.getPath() + " : " + new File(file.getPath()).exists());
                multipartBuilder.addFormDataPart("bike_rc", "bikeRC.jpg", RequestBody.create(MediaType.parse("image/png"), file));
            } else {
                multipartBuilder.addFormDataPart("bike_rc", "");
            }
            //***************************
            multipartBuilder.setType(MultipartBody.FORM).addFormDataPart("additional_details", jsonObjectAsString)
                    .addFormDataPart("supplier_id", supplier_id);
            /**
             * OKHTTP3
             */
            RequestBody requestBody = multipartBuilder
                    .build();
            Request request = new Request.Builder().addHeader("Authorization", token).addHeader("access_token", RestTags.PUBLIC_KEY).addHeader("app_type", RestTags.S_FLAG)
                    .addHeader("supplier_id", supplier_id).url(RestUrls.USER_ADDITIONAL_DETAILS)
                    .post(requestBody)
                    .build();

            //   OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.e("TAG", "Error: " + res);
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e("TAG", "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e("TAG", "Other Error: " + e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * Registration and Profile Updations
     *
     * @param
     * @return
     */
    public static JSONObject userProfileUpdateResponse(String token, ArrayList<File> dineInPics, ArrayList<File> kitchenPics, String profilePic, String jsonObjectAsString, String jsonobjectPicsDeletion) {
        try {
            MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
            // Kitchen Photos
            if (kitchenPics.size() > 0) {
                for (int i = 0; i < kitchenPics.size(); i++) {
                    Log.d("TAG", "Kitchen File...::::" + kitchenPics.get(i).getPath() + " : " + new File(kitchenPics.get(i).getPath()).exists());
                    String filename = UtilityClass.getTimeINMills() + "";
                    multipartBuilder.addFormDataPart("kitchen_pics", filename + ".jpg", RequestBody.create(MediaType.parse("image/png"), kitchenPics.get(i)));
                }
            } else {
                multipartBuilder.addFormDataPart("kitchen_pics", "");
            }
            // Dine In Photos
            if (dineInPics.size() > 0) {
                for (int i = 0; i < dineInPics.size(); i++) {
                    Log.d("TAG", "Dine_in File...::::" + dineInPics.get(i).getPath() + " : " + new File(dineInPics.get(i).getPath()).exists());
                    String filename = UtilityClass.getTimeINMills() + "";
                    multipartBuilder.addFormDataPart("dinein_pics", filename + ".jpg", RequestBody.create(MediaType.parse("image/png"), dineInPics.get(i)));
                }
            } else {
                multipartBuilder.addFormDataPart("dinein_pics", "");
            }

            // Profile Picture
            if (profilePic != null && profilePic.length() > 0) {
                File file_fssai = new File(profilePic);
                Log.d("TAG", "File profilePic...::::" + file_fssai.getPath() + " : " + new File(file_fssai.getPath()).exists());
                multipartBuilder.addFormDataPart("profile_pic", "profilePic.jpg", RequestBody.create(MediaType.parse("image/png"), file_fssai));
            } else {
                multipartBuilder.addFormDataPart("profile_pic", "");
            }
            multipartBuilder.setType(MultipartBody.FORM).addFormDataPart("profile_details", jsonObjectAsString).addFormDataPart("deleted_pics_urls", jsonobjectPicsDeletion);

            /**
             * OKHTTP3
             */
            RequestBody requestBody = multipartBuilder
                    .build();
            Request request = new Request.Builder().addHeader("Authorization", token).addHeader("access_token", RestTags.PUBLIC_KEY).addHeader("app_type", RestTags.S_FLAG)
                    .url(RestUrls.UPDATE_PROFILE)
                    .post(requestBody)
                    .build();

            //   OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.e("TAG", "Error: " + res);
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e("TAG", "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e("TAG", "Other Error: " + e.getLocalizedMessage());
        }
        return null;
    }


}
