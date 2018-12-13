package com.example.monicamj1.myplantcare;

import android.app.DatePickerDialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddPlantActivity extends AppCompatActivity {

    // model
    private int birthdate_year, birthdate_month, birthdate_dayOfMonth;
    private int waterdate_year, waterdate_month, waterdate_dayOfMonth;

    //Referencias
    Button addToGarden_btn;
    EditText plantName_edit;
    EditText birthDate_edit;
    EditText wateringNumber_edit;
    EditText lastWateringDate_edit;
    TextView specieName_view;

    //db
    AppDatabase db;
    DAO_myPlant plantDao;

    static Calendar cal = new GregorianCalendar();

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
        waterdate_month = birthdate_month = cal.get(Calendar.MONTH);
        waterdate_year =cal.get(Calendar.YEAR);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);

        setNow();

        addToGarden_btn = findViewById(R.id.addToGarden_btn);
        plantName_edit = findViewById(R.id.plantName_edit);
        birthDate_edit = findViewById(R.id.birthDate_edit);
        wateringNumber_edit = findViewById(R.id.wateringNumber_edit);
        lastWateringDate_edit = findViewById(R.id.lastWateringDate_edit);
        specieName_view = findViewById(R.id.specieName_view);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "AppDatabase").build();
        plantDao = db.plantDao();

        Intent intent = getIntent();

        if(intent != null){
            Toast.makeText(this, "Is not null", Toast.LENGTH_SHORT).show();
            //TODO: Recoger nombre de la especie y/o índice
        }

    }

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

    //formato wattering day
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

    public void addPlant(View view){
        Toast.makeText(this, "Botón añadido", Toast.LENGTH_SHORT).show();

        String name = plantName_edit.getText().toString();
        String specie= specieName_view.getText().toString();
        Date birthday = dia(birthdate_dayOfMonth,birthdate_month,birthdate_year);
        Date lastWatering = dia(waterdate_dayOfMonth, waterdate_month, waterdate_year);
        int reminder = Integer.parseInt(wateringNumber_edit.getText().toString());
        Plant plant = new Plant(name, specie, "",
                birthday, reminder,
                lastWatering, null,
                "http://www.mijardin.es/wp-content/uploads/2017/01/cultivar-la-planta-del-dinero.jpg");

        new AddPlantActivity.InsertTask(plantDao).execute(plant);
        finish();

    }






}
