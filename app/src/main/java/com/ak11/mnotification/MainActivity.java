package com.ak11.mnotification;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ak11.mnotification.databinding.ActivityMainBinding;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    static  final int IMAGE_REQUEST_CODE = 5000;

    EditText edtTitle, edtBody;
    ImageView imageView;
    Button btnPost;
    ActivityMainBinding binding;
    RadioGroup radioGroup;
    static Bitmap imgBitmap;
    private mNotification notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        edtBody = binding.include.edtBody;
        edtTitle = binding.include.edtTitle;
        imageView = binding.include.imgNotif;
        btnPost = binding.include.btnPost;
        radioGroup = binding.include.radioGroup;

        notification = new mNotification(this);

        imgBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.notification_image);

        btnPost.setOnClickListener(MainActivity.this);
        imageView.setOnClickListener(MainActivity.this);



    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnPost:
                notifyTheUser();
                break;
            case R.id.imgNotif:
                getChosenImage();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_REQUEST_CODE && data != null && resultCode == Activity.RESULT_OK){
            try{
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                imgBitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(imgBitmap);
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void notifyTheUser(){
        switch (radioGroup.getCheckedRadioButtonId()){
            case R.id.rbBasic:
                notification.createNotification(111,edtTitle.getText()+"",edtBody.getText()+"");
                break;
            case R.id.rbSmallImage:
                notification.createNotificationWithSmallImage(222,edtTitle.getText()+"",edtBody.getText()+"",imgBitmap);
                break;
            case R.id.rbBigImage:
                notification.createNotificationWithBigImage(333,edtTitle.getText()+"",edtBody.getText()+"",imgBitmap);
                break;
        }
    }

    private void getChosenImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,IMAGE_REQUEST_CODE);
    }
}