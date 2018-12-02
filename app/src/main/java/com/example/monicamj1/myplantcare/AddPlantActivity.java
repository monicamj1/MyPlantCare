package com.example.monicamj1.myplantcare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddPlantActivity extends AppCompatActivity {

    //Referencias
    Button addToGarden_btn;
    EditText plantName_edit;
    EditText birthDate_edit;
    EditText wateringNumber_edit;
    EditText lastWateringDate_edit;
    TextView specieName_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);

        addToGarden_btn = findViewById(R.id.addToGarden_btn);
        plantName_edit = findViewById(R.id.plantName_edit);
        birthDate_edit = findViewById(R.id.birthDate_edit);
        wateringNumber_edit = findViewById(R.id.wateringNumber_edit);
        lastWateringDate_edit = findViewById(R.id.lastWateringDate_edit);
        specieName_view = findViewById(R.id.specieName_view);

        Intent intent = getIntent();

        if(intent != null){
            Toast.makeText(this, "Is not null", Toast.LENGTH_SHORT).show();
            //TODO: Recoger nombre de la especie y/o índice
        }


    }

    public void addPlant(View view){
        Toast.makeText(this, "Botón añadido", Toast.LENGTH_SHORT).show();

        Intent data = new Intent();
        data.putExtra("name", plantName_edit.getText());
        data.putExtra("specie", specieName_view.getText());
        data.putExtra("webUrl", "");
        data.putExtra("birthday", birthDate_edit.getText());
        data.putExtra("reminder", wateringNumber_edit.getText());
        data.putExtra("lastday", lastWateringDate_edit.getText());
        setResult(RESULT_OK, data);
        finish();

    }
}
