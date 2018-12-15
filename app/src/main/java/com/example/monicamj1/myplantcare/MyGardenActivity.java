package com.example.monicamj1.myplantcare;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ViewTarget;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static com.example.monicamj1.myplantcare.AddPlantActivity.dia;

public class MyGardenActivity extends AppCompatActivity {

    //Modelo
    static List<Plant> myplant_list;

    //Referencias
    RecyclerView myplant_recycler;
    Adapter garden_adapter;
    FloatingActionButton searchadd_btn;

    public static final int ADD_PLANT = 1;
    public static final int MY_PLANT = 2;

    //db
    AppDatabase db;
    DAO_myPlant plantDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_garden);

        myplant_list = new ArrayList<>();

        searchadd_btn = findViewById(R.id.searchadd_btn);
        myplant_recycler = findViewById(R.id.myplant_recycler);


        garden_adapter = new Adapter();

        myplant_recycler.setLayoutManager(new LinearLayoutManager(this));
        myplant_recycler.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        );
        myplant_recycler.setAdapter(garden_adapter);


        searchadd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(MyGardenActivity.this, AddPlantActivity.class);
                startActivityForResult(intent, ADD_PLANT);
            }
        });


        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "AppDatabase").build();
        plantDao = db.plantDao();

        new MyGardenActivity.GetAllPlants(this, plantDao).execute();


    }




    //Get Plant Lis from DB
    public static class GetAllPlants extends AsyncTask<Void, Void, List<Plant>> {
        private MyGardenActivity activity;
        private DAO_myPlant plantDao;

        GetAllPlants(MyGardenActivity activity, DAO_myPlant dao) {
            this.plantDao = dao;
            this.activity = activity;
        }

        @Override
        protected List<Plant> doInBackground(Void... voids) {
            return plantDao.loadAllMyPlants();
        }

        @Override
        protected void onPostExecute(List<Plant> plants) {
            super.onPostExecute(plants);
            activity.setPlantList(plants);
        }
    }

    private void setPlantList(List<Plant> plants) {
        myplant_list = plants;
        garden_adapter.notifyDataSetChanged();
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

    private void onPlantClick(int pos) {
        Intent intent = new Intent(this, MyPlantActivity.class);
        int index = myplant_list.get(pos).myPlant_id;
        intent.putExtra("index", index);
        startActivityForResult(intent, MY_PLANT);
    }

    public void updateWatering(int pos){
        Date now = new Date();
        myplant_list.get(pos).setLast_watering_day(now);
        Plant watered_plant = myplant_list.get(pos);
        new MyGardenActivity.UpdatePlant(plantDao).execute(watered_plant);
        new MyGardenActivity.GetAllPlants(this, plantDao).execute();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView plantimage_view;
        ConstraintLayout plant_layout;
        TextView name_view;
        TextView reminder_view;
        TextView days_view;
        Button watered_btn;

        public ViewHolder(View itemView) {
            super(itemView);
            this.plantimage_view = itemView.findViewById(R.id.plantimage_view);
            this.name_view = itemView.findViewById(R.id.name_view);
            this.reminder_view = itemView.findViewById(R.id.reminder_view);
            this.days_view = itemView.findViewById(R.id.days_view);
            this.plant_layout = itemView.findViewById(R.id.plant_layout);
            this.watered_btn = itemView.findViewById(R.id.watered_btn);
            watered_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateWatering(getAdapterPosition());
                }
            });

            plant_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPlantClick(getAdapterPosition());
                }
            });
        }

        public void bind(Plant item) {
            name_view.setText(item.getName());

           Glide.with(MyGardenActivity.this)
                    .load(item.getProfile())
                    .apply(RequestOptions.circleCropTransform())
                    .into(plantimage_view);

            name_view.setText(item.getName());
            reminder_view.setText("Watering in:");

            Date now = new Date();
            Date last = item.getLast_watering_day();
            long diffTime = now.getTime() - last.getTime();
            long diffDays = diffTime / (1000 * 60 * 60 * 24);
            int days = item.getReminder() - (int)diffDays;
            if(days <= 0){
                days = 0;
            }
            days_view.setText( days + " days");
            watered_btn.setText("Watered");
        }
    }

    class Adapter extends RecyclerView.Adapter<ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = getLayoutInflater().inflate(R.layout.myplant_item, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind(myplant_list.get(position));
        }

        @Override
        public int getItemCount() {
            return myplant_list.size();
        }

    }




    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case ADD_PLANT:
                if(resultCode == RESULT_OK){
                   new MyGardenActivity.GetAllPlants(this, plantDao).execute();
                }
                break;
            case MY_PLANT:
                if(resultCode == RESULT_OK){
                    new MyGardenActivity.GetAllPlants(this, plantDao).execute();
                }

        }
    }

}
