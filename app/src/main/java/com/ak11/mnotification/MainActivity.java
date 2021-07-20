package com.ak11.mnotification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Switch;
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
    Switch swCallback;
    static Bitmap imgBitmap;
    private mNotification notification;
    Vibrator vibrator;


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
        swCallback = binding.include.switchCallback;

        notification = new mNotification(this);

        imgBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.notification_image);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        btnPost.setOnClickListener(MainActivity.this);
        imageView.setOnClickListener(MainActivity.this);

        swCallback.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                    notification.setCallable(true);
                else
                    notification.setCallable(false);

                vibrator.vibrate(100);
            }
        });


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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menu_settings){
            notification.openNotificationSettings();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}