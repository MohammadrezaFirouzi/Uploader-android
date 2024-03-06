package com.firouzi.uploader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.File;
import java.io.IOException;

public class UploadFileTask extends AsyncTask<File, Void, String> {

    private static final String TAG = "UploadFileTask";
    private static final String SERVER_URL = "https://up4u.ir/UploadServlet";  // Replace with your actual server URL
    private static final String API_KEY = "YOUR_API_KEY"; // Replace with your actual API key

    private UploadListener listener; // Interface for receiving upload result

    public interface UploadListener {
        void onUploadResult(String result);
    }

    public void setUploadListener(UploadListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(File... files) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)) // Log HTTP requests and responses for debugging
                .build();

        File fileToUpload = files[0];

        try {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", fileToUpload.getName(),
                            RequestBody.create(MediaType.parse("multipart/form-data"), fileToUpload))
                    .build();

            Request request = new Request.Builder()
                    .url(SERVER_URL)
                    .post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                return "Error: " + response.code() + " - " + response.message();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error during file upload", e);
            return "Error: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (listener != null) {
            listener.onUploadResult(result);
        }
    }
}
