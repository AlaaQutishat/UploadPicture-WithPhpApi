package com.example.uploadpicture;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
 ImageView picture;
 String encode="";
    private static final int CAMERA_REQUEST = 10;
    private static final int GalleryREQUEST = 20;
    ProgressDialog progressDialog ;
    Bitmap bitmap;
    LinearLayout cameralayout,gallerylayout,uploadlayout;
    FloatingActionButton main,gallery,upload,camera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Upload Image");
        actionBar.setDisplayShowHomeEnabled(true);
        picture = findViewById(R.id.picture);
        // camera=findViewById(R.id.camera);
        // gallery=findViewById(R.id.gallery);
        //   upload=findViewById(R.id.upload);
        main = findViewById(R.id.mainfab);
        gallery = findViewById(R.id.gallery);
        camera = findViewById(R.id.camera);
        upload = findViewById(R.id.upload);
        cameralayout = (LinearLayout) findViewById(R.id.cameralayout);
        gallerylayout = (LinearLayout) findViewById(R.id.gallerylayout);
        uploadlayout = (LinearLayout) findViewById(R.id.uploadlayout);
        cameralayout.setVisibility(View.GONE);
        gallerylayout.setVisibility(View.GONE);
        uploadlayout.setVisibility(View.GONE);
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cameralayout.getVisibility() == View.VISIBLE && gallerylayout.getVisibility() == View.VISIBLE && uploadlayout.getVisibility() == View.VISIBLE) {
                    cameralayout.setVisibility(View.GONE);
                    gallerylayout.setVisibility(View.GONE);
                    uploadlayout.setVisibility(View.GONE);
                } else {

                    cameralayout.setVisibility(View.VISIBLE);
                    gallerylayout.setVisibility(View.VISIBLE);
                    uploadlayout.setVisibility(View.VISIBLE);
                }
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameralayout.setVisibility(View.GONE);
                gallerylayout.setVisibility(View.GONE);
                uploadlayout.setVisibility(View.GONE);
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST);

                }
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameralayout.setVisibility(View.GONE);
                gallerylayout.setVisibility(View.GONE);
                uploadlayout.setVisibility(View.GONE);
                //   Creating intent.
                Intent intent = new Intent();

                // Setting intent type as image to select image from phone storage.
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), GalleryREQUEST);

            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameralayout.setVisibility(View.GONE);
                gallerylayout.setVisibility(View.GONE);
                uploadlayout.setVisibility(View.GONE);
                progressDialog = ProgressDialog.show(MainActivity.this, "Image is Uploading", "Please Wait", false, false);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://testpicture010.000webhostapp.com/uploadimage.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parms = new HashMap<>();
                        String imagedata = encode;
                        parms.put("image", imagedata);

                        return parms;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                requestQueue.add(stringRequest);

            }
        });


//        camera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                    startActivityForResult(takePictureIntent, CAMERA_REQUEST);
//                }
//            }
//        });
//
//
//
//
//        gallery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Creating intent.
//                Intent intent = new Intent();
//
//                // Setting intent type as image to select image from phone storage.
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Please Select Image"),  GalleryREQUEST);
//            }
//        });
//        upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                progressDialog = ProgressDialog.show(MainActivity.this,"Image is Uploading","Please Wait",false,false);
//                StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://testpicture010.000webhostapp.com/uploadimage.php", new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        progressDialog.dismiss();
// Toast.makeText(getApplicationContext() ,  response, Toast.LENGTH_SHORT).show();
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
//                        Toast.makeText(getApplicationContext() ,  "Error" + error.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }) {
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map <String ,String> parms=new HashMap<>();
//                        String imagedata=encode;
//                        parms.put("image",imagedata);
//
//                        return parms;
//                    }
//                };
//                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
//                requestQueue.add(stringRequest);
//            }
//        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {


            Uri filepath=data.getData();
         try {
             Bitmap photo = (Bitmap) data.getExtras().get("data");
             encode=imagetostring(photo);

             picture.setImageBitmap(photo);

         } catch (Exception e) {
             e.printStackTrace();
         }

    }

        if ( requestCode ==GalleryREQUEST && resultCode ==RESULT_OK &&data!= null && data.getData()!= null) {
            Uri filepath=data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                encode=imagetostring(bitmap);
                picture.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
    private   String imagetostring( Bitmap bitmap){
 ByteArrayOutputStream byteArrayOutputStream =new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imagebyte=byteArrayOutputStream.toByteArray();
        String encodeimage=android.util.Base64.encodeToString(imagebyte,Base64.DEFAULT);
        return  encodeimage;
    }





    
}