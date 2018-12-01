package com.example.monicamj1.myplantcare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.request.RequestOptions;

import java.util.Date;

public class MyPlantActivity extends AppCompatActivity {

    //Modelo
    Plant myPlant;

    //referencias
    private ImageView profileImage;
    private TextView namePlant;
    private TextView specieName;
    private TextView birthday;
    private TextView watering;
    private TextView waterDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_plant);


        myPlant = new Plant("Lola la flora", "Desconocido", "",
                new Date(2018,11,18), 5,
                new Date(2018,11,18), null,
                "http://www.mijardin.es/wp-content/uploads/2017/01/cultivar-la-planta-del-dinero.jpg");


        Intent intent = getIntent();

        namePlant = findViewById(R.id.name_view);
        specieName = findViewById(R.id.cientific_view);
        birthday = findViewById(R.id.birthday_view);
        watering = findViewById(R.id.watering_view);
        waterDays = findViewById(R.id.waterDays_view);


       profileImage = findViewById(R.id.profileImage_view);

        Glide.with(this)
                .load(myPlant.getProfile()) //problemas con el get de la imagen
                .apply(RequestOptions.circleCropTransform())
                .into(profileImage);

        //TODO:calcular dias que faltan para regar
        //TODO: fechas bien!!

        namePlant.setText(myPlant.getName());
        specieName.setText(myPlant.getScientific_name());
       // birthday.setText(myPlant.getBirthday().toString());
        //watering.setText(myPlant.getReminder());
       // waterDays.setText(myPlant.getLast_watering_day().toString());



        if(intent != null){
            //namePlant = intent.getStringArrayExtra("number");
        }
    }




}
