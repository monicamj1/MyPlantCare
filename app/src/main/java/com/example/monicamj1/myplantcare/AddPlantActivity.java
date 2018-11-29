package com.example.monicamj1.myplantcare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AddPlantActivity extends AppCompatActivity {

    //Referencias
    Button addToGarden_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);

        addToGarden_btn = findViewById(R.id.addToGarden_btn);
        Intent intent = getIntent();

        if(intent != null){
            Toast.makeText(this, "Is not null", Toast.LENGTH_SHORT).show();
            //TODO: Recoger nombre de la especie
        }


    }

    void addPlant(View view){
        Toast.makeText(this, "Botón añadido", Toast.LENGTH_SHORT).show();

        Intent data = new Intent();
        data.putExtra("name", "Nombre Planta");
        data.putExtra("specie", "Desconocido");
        data.putExtra("webUrl", "url");
        data.putExtra("birthday", "fecha");
        data.putExtra("reminder", "5");
        data.putExtra("lastday", "5/2016");
        setResult(RESULT_OK, data);


    }
}
