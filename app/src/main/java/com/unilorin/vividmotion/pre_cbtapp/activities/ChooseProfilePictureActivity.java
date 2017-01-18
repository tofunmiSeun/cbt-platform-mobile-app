package com.unilorin.vividmotion.pre_cbtapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.unilorin.vividmotion.pre_cbtapp.R;
import com.unilorin.vividmotion.pre_cbtapp.managers.data.SharedPreferenceContract;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class ChooseProfilePictureActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean pictureChosen;

    ImageView imageView;
    Button selectPictureButton;
    Button noThanksButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_profile_picture);

        initialiseViewObjects();
    }

    private void initialiseViewObjects(){
        imageView = (ImageView) findViewById(R.id.imageHolder);
        selectPictureButton = (Button) findViewById(R.id.setProfilePictureButton);
        noThanksButton = (Button) findViewById(R.id.continueButton);

        selectPictureButton.setOnClickListener(this);
        noThanksButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == noThanksButton){
            if(!pictureChosen){
                SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceContract.FILE_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(SharedPreferenceContract.PROFILE_PICTURE_STRING, "EMPTY");
                editor.apply();
            }
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        }else if (v == selectPictureButton){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);

            startActivityForResult(intent, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            Bitmap bitmap;
            try
            {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                imageView.setImageBitmap(bitmap);
                pictureChosen = true;
                new StorePictureAsync().execute(bitmap);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private class StorePictureAsync extends AsyncTask<Bitmap,Void,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            noThanksButton.setText("Proceed");
        }

        @Override
        protected Void doInBackground(Bitmap... bitmap)
        {
            try {
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                try {
                    bitmap[0].compress(Bitmap.CompressFormat.JPEG, 100, bao);
                } catch (Exception e) {
                    bitmap[0].compress(Bitmap.CompressFormat.PNG, 100, bao);
                }
                byte[] b = bao.toByteArray();
                String temp = Base64.encodeToString(b, Base64.DEFAULT);
                SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceContract.FILE_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(SharedPreferenceContract.PROFILE_PICTURE_STRING, temp);
                editor.apply();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
