package com.jemmy.calak.camart.view;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.jemmy.calak.camart.Config;
import com.jemmy.calak.camart.R;
import com.jemmy.calak.camart.util.API;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UploadBuktiPembayaran extends AppCompatActivity {

    private SubsamplingScaleImageView imageView;
    private TextView pilih;
    private Button upload;
    private static final int CODE_GALERY = 1;
    private API api;
    private int status_image = 0;
    private String id_order, id_user;
    Uri uriImage;
    Bitmap imageBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_bukti_pembayaran);
        initXml();
        getUserData();
    }

    private void getUserData() {
        Intent intent = getIntent();
        id_order = intent.getStringExtra("id_order");
        id_user = intent.getStringExtra("id_user");
    }

    private void initXml() {
        getSupportActionBar().setTitle("UPLOAD PEMBAYARAN");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        api = new API();

        imageView = (SubsamplingScaleImageView)findViewById(R.id.imageUpload);
        pilih = (TextView)findViewById(R.id.pilihFoto);
        upload = (Button)findViewById(R.id.btUpload);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentGallery, CODE_GALERY);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(status_image == 0){
                    Toast.makeText(UploadBuktiPembayaran.this, "Anda belum memilih foto.", Toast.LENGTH_SHORT).show();
                }else {
                    uploadImage();
                }
            }
        });
    }

    private void uploadImage() {

        String imageString = imageToString(imageBitmap);
        api.uploadBuktiPembayaran(id_user, id_order, imageString, this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("code", "==>" + requestCode + " , " + resultCode);

        if (requestCode == CODE_GALERY && resultCode == RESULT_OK && data != null) {
            status_image = 1;
            //cara pertama
            uriImage = data.getData();
            imageView.setImage(ImageSource.uri(uriImage));
            pilih.setVisibility(View.GONE);

            //set bitmap to upload
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        String imageString = Base64.encodeToString(imageByte, Base64.DEFAULT);

        return imageString;
    }

}
