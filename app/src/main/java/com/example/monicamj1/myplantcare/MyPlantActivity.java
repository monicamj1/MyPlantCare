package com.example.monicamj1.myplantcare;

import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MyPlantActivity extends AppCompatActivity {

    //Modelo
    Plant myPlant;
    List<String> gallery_images = new ArrayList<>();
    int id_plant = -1;

    //referencias
    private ImageView profileImage;
    private TextView namePlant;
    private TextView specieName;
    private TextView birthday;
    private TextView watering;
    private TextView waterDays;
    private Button about_button;
    RecyclerView gallery;
    Adapter gallery_adapter;

    AppDatabase db;
    DAO_myPlant plantDao;

    SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");

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

        gallery = findViewById(R.id.gallery_view);

        gallery_adapter = new Adapter();

        gallery.setLayoutManager(new GridLayoutManager(this,3));

        gallery.setAdapter(gallery_adapter);

        Intent intent = getIntent();


        if(intent != null){
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
        profileImage = findViewById(R.id.profileImage_view);
        about_button = findViewById(R.id.about_button);
        about_button.setVisibility(View.GONE);


        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "AppDatabase").build();

        plantDao = db.plantDao();

        new MyPlantActivity.GetPlant(this, plantDao).execute(id_plant);

    }

    //Get Plant from DB
    public static class GetPlant extends AsyncTask<Integer, Void, Plant> {
        private MyPlantActivity activity;
        private DAO_myPlant plantDao;

        GetPlant(MyPlantActivity activity, DAO_myPlant dao) {
            this.plantDao = dao;
            this.activity = activity;
        }

        @Override
        protected Plant doInBackground(Integer... ids) {
            return plantDao.loadPlantById(ids[0]);
        }

        @Override
        protected void onPostExecute(Plant plant) {
            super.onPostExecute(plant);
            activity.setPlantFields(plant);
        }
    }

    private void setPlantFields(Plant plant) {
        myPlant = plant;
        Glide.with(this)
                .load(plant.getProfile())
                .apply(RequestOptions.circleCropTransform())
                .into(profileImage);


        namePlant.setText(plant.getName());
        specieName.setText(plant.getScientific_name());
        birthday.setText(fmt.format(plant.getBirthday()));
        Date now = new Date();
        Date last = plant.getLast_watering_day();
        long diffTime = now.getTime() - last.getTime();
        long diffDays = diffTime / (1000 * 60 * 60 * 24);
        int days = plant.getReminder() - (int)diffDays;
        if(days <= 0){
            days = 0;
            watering.setTextColor(ContextCompat.getColor(MyPlantActivity.this, R.color.Rosita));
        }else{
            watering.setTextColor(ContextCompat.getColor(MyPlantActivity.this, R.color.Verde_Oscuro));
        }
        watering.setText("Watering in "+days+" days");
        waterDays.setText(Integer.toString(plant.getReminder()));

        gallery_images.clear();
        gallery_images.addAll(myPlant.getImages_url());
        gallery_adapter.notifyDataSetChanged();
    }

    //Delete Plant from DB
    public static class DeletePlant extends AsyncTask<Plant, Void, Void> {
        private MyPlantActivity activity;
        private DAO_myPlant plantDao;

        DeletePlant(MyPlantActivity activity, DAO_myPlant dao) {
            this.plantDao = dao;
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Plant... plants) {
            plantDao.deletePlant(plants[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            activity.finishActivity();
        }

    }

    //Update Plant in DB
    public static class UpdatePlant extends AsyncTask<Plant, Void, Void> {
        private DAO_myPlant plantDao;

        UpdatePlant(DAO_myPlant dao) {
            this.plantDao = dao;
        }

        @Override
        protected Void doInBackground(Plant... plants) {
            plantDao.updatePlant(plants[0]);
            return null;
        }
    }


    public void finishActivity(){
        setResult(RESULT_OK);
        finish();
    }


    //ACTUALIZAR RIEGO
    public void updateWatering(View view){
        Date now = new Date();
        myPlant.setLast_watering_day(now);
        new MyPlantActivity.UpdatePlant( plantDao).execute(myPlant);
        new MyPlantActivity.GetPlant(this, plantDao).execute(id_plant);
    }

    //CAMARA
    public void openCamera() {
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
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }


    public void onImageClick(int pos){
        String img = gallery_images.get(pos);
        if (pos == 0){
            openCamera();
        }else {
            openGalleryImage(pos);
        }
    }

    String myCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",    /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        myCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //RECYCLERVIEW TIPO GRID
    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageItem_view;
        TextView addImage_view;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageItem_view = itemView.findViewById(R.id.imageItem_view);
            this.addImage_view = itemView.findViewById(R.id.addImage_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onImageClick(getAdapterPosition());
                }
            });
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
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
            if (gallery_images.get(position) == "file:///android_asset/btn.png") {
                holder.addImage_view.setVisibility(View.VISIBLE);
            } else {
                holder.addImage_view.setVisibility(View.GONE);
            }
            Glide.with(MyPlantActivity.this)
                    .load(gallery_images.get(position))
                    .apply(requestOptions)
                    .into(holder.imageItem_view);
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
                intent.putExtra("index", id_plant);
                startActivityForResult(intent, EDIT_PLANT);
                break;
            case R.id.delete_plant:
                alertDelete();
                break;
        }
        return true;
    }

    //POP-UP DELETE
    public void alertDelete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.Confirm)
                .setMessage("Do yo want to delete the plant?")
                .setPositiveButton(R.string.Delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new MyPlantActivity.DeletePlant(MyPlantActivity.this, plantDao).execute(myPlant);
                    }
                })
                .setNegativeButton(R.string.Cancel, null);
        builder.create().show();
    }



    public void openGalleryImage(int pos) {
        Intent intent = new Intent(this,ShowImageActivity.class);
        intent.putExtra("pos", pos);
        intent.putExtra("id", id_plant);
        startActivityForResult(intent, EDIT_PLANT);
    }

    //RESULTADO DE ACTIVIDADES
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case EDIT_PLANT:
                if(resultCode == RESULT_OK) {
                    new MyPlantActivity.GetPlant(this, plantDao).execute(id_plant);
                    gallery_adapter.notifyDataSetChanged();
                }
                    break;
            case REQUEST_IMAGE_CAPTURE:
                if(resultCode == RESULT_OK){
                    gallery_images.add(myCurrentPhotoPath);
                    myPlant.setImages_url(gallery_images);
                    new MyPlantActivity.UpdatePlant(plantDao).execute(myPlant);
                    new MyPlantActivity.GetPlant(this, plantDao).execute(id_plant);

                }
                break;

        }
    }


}
