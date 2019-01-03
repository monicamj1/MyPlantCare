package com.example.monicamj1.myplantcare;

import android.app.DatePickerDialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class AddPlantActivity extends AppCompatActivity {

    // model
    private int birthdate_year, birthdate_month, birthdate_dayOfMonth;
    private int waterdate_year, waterdate_month, waterdate_dayOfMonth;
    int id_plant;
    List<String> url = new ArrayList<>();
    String profile;

    Plant plant;

    //Referencias
    Button addToGarden_btn;
    EditText plantName_edit;
    TextView birthDate_edit;
    EditText wateringNumber_edit;
    TextView lastWateringDate_edit;
    TextView specieName_view;

    //db
    AppDatabase db;
    DAO_myPlant plantDao;

    static Calendar cal = new GregorianCalendar();
    SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");

    static Date dia(int dia, int mes, int anyo) {
        cal.set(Calendar.YEAR, anyo);
        cal.set(Calendar.MONTH, mes-1);
        cal.set(Calendar.DAY_OF_MONTH, dia);
        return cal.getTime();
    }

    private void setNow() {
        cal.setTime(new Date());
        birthdate_year = cal.get(Calendar.YEAR);
        birthdate_month = cal.get(Calendar.MONTH);
        birthdate_dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        waterdate_dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        waterdate_month = cal.get(Calendar.MONTH);
        waterdate_year =cal.get(Calendar.YEAR);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);

        addToGarden_btn = findViewById(R.id.addToGarden_btn);
        plantName_edit = findViewById(R.id.plantName_edit);
        birthDate_edit = findViewById(R.id.birthDate_view);
        wateringNumber_edit = findViewById(R.id.wateringNumber_edit);
        lastWateringDate_edit = findViewById(R.id.lastWateringDate_view);
        specieName_view = findViewById(R.id.specieName_view);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "AppDatabase").build();
        plantDao = db.plantDao();

        Intent intent = getIntent();

        if(intent != null) {
            id_plant = intent.getIntExtra("index", -1);
            if (id_plant == -1) {
                setNow();
                addToGarden_btn.setText("Add to my garden");
                Date now = new Date();
                birthDate_edit.setText(fmt.format(now));
                lastWateringDate_edit.setText(fmt.format(now));
            } else {
                addToGarden_btn.setText("Save changes");
                new AddPlantActivity.GetFields(this, plantDao).execute(id_plant);
            }
        }

    }

    //Insert new Plant in DB
    public static class InsertTask extends AsyncTask<Plant, Void, Void> {
        private DAO_myPlant plantDao;

        InsertTask(DAO_myPlant dao) {
            this.plantDao = dao;
        }

        @Override
        protected Void doInBackground(Plant... plants) {
            plantDao.insertPnat(plants[0]);
            return null;
        }
    }

    //Get Plant from DB
    public static class GetFields extends AsyncTask<Integer, Void, Plant> {
        private AddPlantActivity activity;
        private DAO_myPlant plantDao;

        GetFields(AddPlantActivity activity, DAO_myPlant dao) {
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

    private void setPlantFields(Plant plant){
        setNow();
        plantName_edit.setText(plant.getName());
        wateringNumber_edit.setText(Integer.toString(plant.getReminder()));
        birthDate_edit.setText(fmt.format(plant.getBirthday()));
        lastWateringDate_edit.setText(fmt.format(plant.getLast_watering_day()));
        url.addAll(plant.getImages_url());
        profile = plant.getProfile();

    }

    //Update plant in DB
    public static class UpdatePlant extends AsyncTask<Plant, Void, Void> {
        private DAO_myPlant plantDao;
        private AddPlantActivity activity;

        UpdatePlant(AddPlantActivity activity, DAO_myPlant dao) {
            this.plantDao = dao;
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Plant... plants) {
            plantDao.updatePlant(plants[0]);
            return null;
        }
    }

    //formato Birthday
    public void clickDate(View view) {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                birthdate_year = year;
                birthdate_month = month;
                birthdate_dayOfMonth = dayOfMonth;
                birthDate_edit.setText(String.format("%02d/%02d/%04d", dayOfMonth, month+1, year));
            }
        }, birthdate_year, birthdate_month, birthdate_dayOfMonth).show();
    }

    //formato watteringDay
    public void clickWater(View view) {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                waterdate_year = year;
                waterdate_month = month;
                waterdate_dayOfMonth = dayOfMonth;
                lastWateringDate_edit.setText(String.format("%02d/%02d/%04d", dayOfMonth, month+1, year));
            }
        }, waterdate_year, waterdate_month, waterdate_dayOfMonth).show();
    }

    public void getFields (){
        String name = plantName_edit.getText().toString();
        //String specie= specieName_view.getText().toString();
        String specie = "Unkown";
        Date birthday = dia(birthdate_dayOfMonth+0,birthdate_month+1,birthdate_year+0);
        Date lastWatering = dia(waterdate_dayOfMonth+0, waterdate_month+1, waterdate_year+0);
        int reminder = Integer.parseInt(wateringNumber_edit.getText().toString());

        if(id_plant == -1){
            url.add("file:///android_asset/btn.png");
            profile = "file:///android_asset/icon.png";
        }

        plant = new Plant(name, specie, "",
                birthday, reminder,
                lastWatering, url,
                profile);

        if (id_plant != -1) {
            plant.setMyPlantID(id_plant);
        }
    }

    public void addPlant(View view){
        String reminder = wateringNumber_edit.getText().toString();
        String name = plantName_edit.getText().toString();
        if(reminder.matches("") ||  reminder.length() == 0 || name.matches("" ) || name.length() == 0){
            Toast.makeText(this, "Reminder is empty", Toast.LENGTH_SHORT).show();
        } else {
            if (id_plant == -1) {

                getFields();
                new AddPlantActivity.InsertTask(plantDao).execute(plant);
                setResult(RESULT_OK);
                finish();
            } else {
                getFields();
                new AddPlantActivity.UpdatePlant(this, plantDao).execute(plant);
                setResult(RESULT_OK);
                finish();
            }
        }
    }


}
