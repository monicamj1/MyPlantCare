package com.example.monicamj1.myplantcare;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class ShowImageActivity extends AppCompatActivity {

    //Model
    Plant myPlant;
    int pos;
    int id_plant;
    List<String> gallery_images = new ArrayList<>();

    ImageView bigImage_view;
    Button setProfile_btn;
    Button deleteImage_btn;
    Button previous_btn;
    Button following_btn;

    AppDatabase db;
    DAO_myPlant plantDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        bigImage_view = findViewById(R.id.bigImage_view);
        setProfile_btn = findViewById(R.id.setProfile_btn);
        deleteImage_btn = findViewById(R.id.deleteImage_btn);
        previous_btn = findViewById(R.id.previous_btn);
        following_btn = findViewById(R.id.following_btn);

        Intent intent = getIntent();

        if(intent != null){
            pos = intent.getIntExtra("pos", -1);
            id_plant = intent.getIntExtra("id", -1);
        }

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "AppDatabase").build();

        plantDao = db.plantDao();

        new ShowImageActivity.GetPlant(this, plantDao).execute(id_plant);
    }

    public static class GetPlant extends AsyncTask<Integer, Void, Plant> {
        private ShowImageActivity activity;
        private DAO_myPlant plantDao;

        GetPlant(ShowImageActivity activity, DAO_myPlant dao) {
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
            activity.setPlant(plant);
        }
    }

    private void setPlant(Plant plant){
        myPlant = plant;
        gallery_images = myPlant.getImages_url();
        setImage();
    }

    private void setImage(){
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(5));
        Glide.with(this)
                .load(gallery_images.get(pos))
                .apply(requestOptions)
                .into(bigImage_view);
    }

    //Update Plant in DB
    public static class UpdatePlant extends AsyncTask<Plant, Void, Void> {
        private ShowImageActivity activity;
        private DAO_myPlant plantDao;

        UpdatePlant(ShowImageActivity activity, DAO_myPlant dao) {
            this.activity = activity;
            this.plantDao = dao;

        }

        @Override
        protected Void doInBackground(Plant... plants) {
            plantDao.updatePlant(plants[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
           activity.finishActivity();
        }
    }

    public void finishActivity(){
       setResult(RESULT_OK);
       finish();
    }

    public void setNewProfile(View view){
        myPlant.setProfile(gallery_images.get(pos));
        new ShowImageActivity.UpdatePlant(this, plantDao).execute(myPlant);

    }

    public void deleteImage(View view){
        List<String> list = new ArrayList<>();
        for(int i=0; i<gallery_images.size();i++){
            if(i != pos) {
                list.add(gallery_images.get(i));
            }
        }
        myPlant.setImages_url(list);
        new ShowImageActivity.UpdatePlant(this, plantDao).execute(myPlant);

    }

    public void previousPic(View view){
        if(pos > 1){
            pos--;
        }else{
            Toast.makeText(this, "No more images", Toast.LENGTH_SHORT).show();
        }
        setImage();
    }

    public void followingPic(View view){
        if(pos < gallery_images.size()-1){
            pos++;
        }else{
            Toast.makeText(this, "No more images", Toast.LENGTH_SHORT).show();
        }
        setImage();
    }
}
