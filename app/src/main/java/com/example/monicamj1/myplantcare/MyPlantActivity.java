package com.example.monicamj1.myplantcare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class MyPlantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_plant);
    }

    ImageView profileImage = findViewById(R.id.profileImage_view);

   /* Glide.with(this)
            //.load("file:///android_asset/profile.png")
            .apply(RequestOptions.circleCropTransform())
            .into(profileImage);*/

}
