package com.example.monicamj1.myplantcare;

import android.arch.persistence.room.Room;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.request.RequestOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MyPlantActivity extends AppCompatActivity {

    //Modelo
    List<Plant> myPlant;
    List<Bitmap> gallery_images = new ArrayList<>();
    int id_plant;

    //referencias
    private ImageView profileImage;
    private TextView namePlant;
    private TextView specieName;
    private TextView birthday;
    private TextView watering;
    private TextView waterDays;
    private Button addImage_btn;
    RecyclerView gallery;
    Adapter gallery_adapter;

    AppDatabase db;
    DAO_myPlant plantDao;

    public static final int EDIT_PLANT = 1;

    static final int REQUEST_IMAGE_CAPTURE = 10;



    static Date dia(int dia, int mes, int anyo) {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, anyo);
        cal.set(Calendar.MONTH, mes-1);
        cal.set(Calendar.DAY_OF_MONTH, dia);
        return cal.getTime();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_plant);

        gallery_images.add(null);

       // gallery = findViewById(R.id.myplant_recycler);

        //gallery_adapter = new Adapter();

       // gallery.setLayoutManager(new GridLayoutManager(this,3));

       // gallery.setAdapter(gallery_adapter);

        Intent intent = getIntent();


        if(intent != null){
            //TODO: recibir ID de la planta y recoger todos los campos necesarios
            id_plant = intent.getIntExtra("index", -1);
            if (id_plant == -1) {
                // ERROR: Ese id no existe.
            } else {
                Log.i("MyPlantCare", "Id Planta: " + id_plant);
            }
        }



        namePlant = findViewById(R.id.name_view);
        specieName = findViewById(R.id.cientific_view);
        birthday = findViewById(R.id.birthday_view);
        watering = findViewById(R.id.watering_view);
        waterDays = findViewById(R.id.waterDays_view);
        addImage_btn = findViewById(R.id.addImage_btn);


       profileImage = findViewById(R.id.profileImage_view);


        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "AppDatabase").build();

        plantDao = db.plantDao();

        new MyPlantActivity.GetPlant(this, plantDao).execute();

    }

    //Get Plant from DB
    public static class GetPlant extends AsyncTask<Void, Void, List<Plant>> {
        private MyPlantActivity activity;
        private DAO_myPlant plantDao;

        GetPlant(MyPlantActivity activity, DAO_myPlant dao) {
            this.plantDao = dao;
            this.activity = activity;
        }

        @Override
        protected List<Plant> doInBackground(Void... voids) {
            //TODO: Buscar manera de pasar la variable id_plant dentro de una public static class
            return plantDao.loadPlantById(1);
        }

        @Override
        protected void onPostExecute(List<Plant> plant) {
            super.onPostExecute(plant);
            activity.setPlantFields(plant);
        }
    }

    private void setPlantFields(List<Plant> plant) {
        myPlant = plant;
        Glide.with(this)
                .load(myPlant.get(0).getProfile())
                .apply(RequestOptions.circleCropTransform())
                .into(profileImage);


        namePlant.setText(myPlant.get(0).getName());
        specieName.setText(myPlant.get(0).getScientific_name());
        birthday.setText(String.format("%1$tm-%1$te-%1$tY", myPlant.get(0).getBirthday()));
        Date now = new Date();
        Date last = myPlant.get(0).getLast_watering_day();
        long diffTime = now.getTime() - last.getTime();
        long diffDays = diffTime / (1000 * 60 * 60 * 24);
        int days = myPlant.get(0).getReminder() - (int)diffDays;
        if(days <= 0){
            days = 0;
        }
        watering.setText("Watering in "+days+" days");
        waterDays.setText(Integer.toString(myPlant.get(0).getReminder()));


    }

    //ACTUALIZAR RIEGO
    public void updateWatering(View view){
        //Actualizar recordatorio de riego
    }

    //CAMARA
    public void openCamera(View view) {
        if(checkCameraHardware(this)==true){
            dispatchTakePictureIntent();
        }
    }

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

    }


    public void onImageClick(int pos){
        //TODO: Abrir actividad fotografía
    }



    //RECYCLERVIEW TIPO GRID
    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageItem_view;
        TextView addImage_view;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageItem_view = itemView.findViewById(R.id.imageItem_view);
            this.addImage_view = itemView.findViewById(R.id.addImage_view);


            imageItem_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onImageClick(getAdapterPosition());
                }
            });
        }

        public void bind(Bitmap item) {
            if(item == null){
                addImage_view.setText("");
            }

        }
    }

    class Adapter extends RecyclerView.Adapter<ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = getLayoutInflater().inflate(R.layout.image_item, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind(gallery_images.get(position));
        }

        @Override
        public int getItemCount() {
            return gallery_images.size();
        }

    }

    // CREAR MENÚ
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

    //RESULTADO DE ACTIVIDADES
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case EDIT_PLANT:
                if(resultCode == RESULT_OK) {
                    //TODO: Actualizar planta en la base de datos
                }
                    break;
            case REQUEST_IMAGE_CAPTURE:
                if(resultCode == RESULT_OK){
                    //TODO: recoger imagen
                    Bitmap image = (Bitmap) data.getExtras().get("data");
                    // ImageView imageview = findViewById(R.id.image1_view);
                    // imageview.setImageBitmap(image);
                    gallery_images.add(image);
                }
                break;

        }
    }


}
