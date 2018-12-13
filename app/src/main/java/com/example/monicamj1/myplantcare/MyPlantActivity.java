package com.example.monicamj1.myplantcare;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.request.RequestOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
    private Button addImage_btn;

    AppDatabase db;
    DAO_myPlant plantDao;

    public static final int EDIT_PLANT = 1;

    static final int REQUEST_IMAGE_CAPTURE = 10;


    static Date dia(int dia, int mes, int anyo) {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, anyo);
        cal.set(Calendar.MONTH, mes - 1);
        cal.set(Calendar.DAY_OF_MONTH, dia);
        return cal.getTime();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_plant);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "AppDatabase").build();

        plantDao = db.plantDao();
        //db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "AppDatabase").build();


        //db.plantDao().loadPlantById(1);
        myPlant = new Plant("Lola la flora", "Desconocido", "",
                dia(18, 11, 2018), 5,
                dia(18, 11, 2018), null,
                "http://www.mijardin.es/wp-content/uploads/2017/01/cultivar-la-planta-del-dinero.jpg");

        // db.plantDao().insertPnat(myPlant);

        // db.plantDao().loadPlantById(1);

        Intent intent = getIntent();

        if (intent != null) {
            //TODO: recibir ID de la planta y recoger todos los campos necesarios
            //namePlant = intent.getStringArrayExtra("index");
        }

        namePlant = findViewById(R.id.name_view);
        specieName = findViewById(R.id.cientific_view);
        birthday = findViewById(R.id.birthday_view);
        watering = findViewById(R.id.watering_view);
        waterDays = findViewById(R.id.waterDays_view);
        addImage_btn = findViewById(R.id.addImage_btn);


        profileImage = findViewById(R.id.profileImage_view);

        Glide.with(this)
                .load(myPlant.getProfile())
                .apply(RequestOptions.circleCropTransform())
                .into(profileImage);


        namePlant.setText(myPlant.getName());
        specieName.setText(myPlant.getScientific_name());
        birthday.setText(String.format("%1$tm-%1$te-%1$tY", myPlant.getBirthday()));
        Date now = new Date();
        Date last = myPlant.getLast_watering_day();
        long diffTime = now.getTime() - last.getTime();
        long diffDays = diffTime / (1000 * 60 * 60 * 24);
        int days = myPlant.getReminder() - (int) diffDays;
        if (days <= 0) {
            days = 0;
        }
        watering.setText("Watering in " + days + " days");
        waterDays.setText(Integer.toString(myPlant.getReminder()));

        if (intent != null) {
            //namePlant = intent.getStringArrayExtra("index");
        }
    }

    public void updateWatering(View view) {
        //Actualizar recordatorio de riego
    }


}
    public void openCamera(View view) {
        if(checkCameraHardware(this)==true){
            //getCameraInstance();
            dispatchTakePictureIntent();
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }




    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.myplant_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.edit_plant:
                Intent intent = new Intent(this, AddPlantActivity.class);
                //TODO: Enviar ID de la planta
                intent.putExtra("index", 3);
                startActivityForResult(intent, EDIT_PLANT);
                break;
            case R.id.delete_plant:
                //TODO: borrar planta de la lista de plantas
                break;
        }
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case EDIT_PLANT:
                if(resultCode == RESULT_OK) {
                    //TODO: Añadir planta a la base de datos
                }
                    break;
            case REQUEST_IMAGE_CAPTURE:
                if(resultCode == RESULT_OK){
                    //TODO: recoger imagen
                    Bitmap image = (Bitmap) data.getExtras().get("data");
                    ImageView imageview = findViewById(R.id.image1_view);
                    imageview.setImageBitmap(image);
                }
                break;

        }
    }


}
