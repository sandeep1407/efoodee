package com.uj.myapplications.utility;

/**
 * Created by Umesh on 7/7/2018.
 * Teammates : Umesh and ${User}
 */

import android.content.Context;
import android.util.Log;
import okhttp3.*;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;


public class MultipartRequest {
    public Context caller;
    public MultipartBody.Builder builder;
    private OkHttpClient client;


    public MultipartRequest(Context caller) {

        this.caller = caller;
        this.builder = new MultipartBody.Builder();
        // this.builder.type(MultipartBody.FORM);
        this.client = new OkHttpClient();
        builder.setType(MultipartBody.FORM);
    }

    public void addString(String name, String value) {
        this.builder.addFormDataPart(name, value);
    }

    public void addFile(String name, String id, String filePath, String fileName) {
        builder.addFormDataPart("profile_photo", fileName);
        builder.addFormDataPart("user_self_id", id);
        String[] arr = fileName.split("\\.");
        if (arr.length > 0) {
            if (arr[arr.length - 1].equalsIgnoreCase("png")) {
                Log.e("format is :", "png");
                this.builder.addFormDataPart(name, fileName, RequestBody.create(
                        MediaType.parse("image/png"), new File(filePath)));

            } else if (arr[arr.length - 1].equalsIgnoreCase("jpg")) {
                Log.e("format is :", "jpg");
                this.builder.addFormDataPart(name, fileName, RequestBody.create(
                        MediaType.parse("image/jpg"), new File(filePath)));

            } else if (arr[arr.length - 1].equalsIgnoreCase("jpeg")) {
                Log.e("format is :", "jpeg");
                this.builder.addFormDataPart(name, fileName, RequestBody.create(
                        MediaType.parse("image/jpeg"), new File(filePath)));

            }

        }
    }

    public void addFile(String name, String id, String filePath, String fileName, String date, String galleryTitle) {
        builder.addFormDataPart("cover_image", fileName);
        builder.addFormDataPart("user_self_id", id);
        builder.addFormDataPart("gallery_title", galleryTitle);
        builder.addFormDataPart("Gallery_date", date);
        String[] arr = fileName.split("\\.");
        if (arr.length > 0) {
            if (arr[arr.length - 1].equalsIgnoreCase("png")) {
                Log.e("format is :", "png");
                this.builder.addFormDataPart(name, fileName, RequestBody.create(
                        MediaType.parse("image/png"), new File(filePath)));

            } else if (arr[arr.length - 1].equalsIgnoreCase("jpg")) {
                Log.e("format is :", "jpg");
                this.builder.addFormDataPart(name, fileName, RequestBody.create(
                        MediaType.parse("image/jpg"), new File(filePath)));

            } else if (arr[arr.length - 1].equalsIgnoreCase("jpeg")) {
                Log.e("format is :", "jpeg");
                this.builder.addFormDataPart(name, fileName, RequestBody.create(
                        MediaType.parse("image/jpeg"), new File(filePath)));

            }

        }
    }

    public void addTXTFile(String name, String filePath, String fileName) {
        this.builder.addFormDataPart(name, fileName, RequestBody.create(
                MediaType.parse("text/plain"), new File(filePath)));
    }

    public void addZipFile(String name, String filePath, String fileName) {
        this.builder.addFormDataPart(name, fileName, RequestBody.create(
                MediaType.parse("application/zip"), new File(filePath)));
    }

    public Response execute(String url, JSONObject jsonObject) {
        RequestBody requestBody = null;
        Request request = null;
        Response response = null;
        int code = 200;
        String strResponse = null;
        try {
            requestBody = this.builder.build();
            request = new Request.Builder().header("cache-control", "no-cache").url(url).post(requestBody).build();
            Log.e("::::::: REQ :: ", request.toString());
            response = client.newCall(request).execute();
            Log.e("::::::: REQ :: ", response.toString());
            // FamilyApplication.profile_photo = response.body().string();
            Log.e("::::::: REQ :: ", response.body().string());

            if (!response.isSuccessful())
                throw new IOException();

            code = response.networkResponse().code();

            if (response.isSuccessful()) {
                strResponse = response.body().string();
            } else if (code == 400) {
                // ** "Invalid URL or Server not available, please try again"
                // strResponse = caller.getResources().getString(
                //       R.string.error_invalid_URL);
            } else if (code == 500) {
                // * "Connection timeout, please try again",
                // strResponse = caller.getResources().getString(
                //       R.string.error_timeout);
            }
        } catch (Exception e) {
            //  Log.error("Exception", e);
            //Log.print(e);
            e.printStackTrace();
        } finally {
            requestBody = null;
            request = null;
            builder = null;
            if (client != null)
                client = null;
            System.gc();
        }
        return response;
    }
}
