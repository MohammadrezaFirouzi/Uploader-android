package com.firouzi.uploader;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import okhttp3.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.RequestBody;
import okhttp3.MultipartBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements MyDialog.MyDialogEventListener {

    //---------------------------------------------------//


    private static final int PERMISION_CODE = 100;
    private static final int PICK_FILE_REQUEST = 1;
    private SweetAlertDialog pDialog;
    private File file;
    private MyDialog myDialog;
    private String fileUrl;
    //---------------------------------------------------//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        //--------------------views----------------------//


        Button uploadButton = findViewById(R.id.uploadButton);
        pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        myDialog=new MyDialog();
        UserManager userManager = new UserManager(getApplicationContext());


        //--------------------views----------------------//
        if(!userManager.getShow()){
            new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText("خوش آمدید")
                    .setContentText("لطفا برای حمایت از توسعه دهنده در گروه روبیکا عضو شوید.")
                    .setCustomImage(R.drawable.bot)
                    .setConfirmText("باشه")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://rubika.ir/joing/FJBCBBDF0XVJGBNCAJIJIQPDSZFLTHWX"));
                            startActivity(intent);

                        }
                    })
                    .show();
                    userManager.saveshow(true);
        }



        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkpermision();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, PICK_FILE_REQUEST);
            }
        });
    }








    private void checkpermision() {
        if (Build.VERSION.SDK_INT >= 33){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES,Manifest.permission.READ_MEDIA_VIDEO,Manifest.permission.READ_MEDIA_AUDIO,Manifest.permission.CAMERA},PERMISION_CODE);
        }else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},PERMISION_CODE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                showUploadAlert();
                pDialog.setTitleText("در حال آپلود...");
                pDialog.show();

                try {
                    file = FileUtil.from(MainActivity.this,uri);
                    UploadFileTask task = new UploadFileTask();
                    task.setUploadListener(new UploadFileTask.UploadListener() {
                        @Override
                        public void onUploadResult(String result) {

                            pDialog.dismiss();
                            myDialog.setCancelable(true);
                            myDialog.show(getSupportFragmentManager(),null);
                            fileUrl = "https://dl.up4u.ir/"+result.replace("https://up4u.ir/dl/","");
                            new ClipboardManagerHelper(getApplicationContext()).copyToClipboard(fileUrl);
                        }
                    });
                    task.execute(file);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void showUploadAlert() {
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#0DA0FF"));
        pDialog.setCancelButton("لغو", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                pDialog.dismiss();
            }
        });
    }


    @Override
    public void onOkButtonClicked() {
        myDialog.dismiss();
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(fileUrl));
        startActivity(intent);

    }


}




