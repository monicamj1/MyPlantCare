package com.example.monicamj1.myplantcare;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
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

public class MyGardenActivity extends AppCompatActivity {

    //Modelo
    List<Plant> myplant_list;

    //Referencias
    RecyclerView myplant_recycler;
    Adapter garden_adapter;
    FloatingActionButton searchadd_btn;

    public static final int ADD_PLANT = 1;
    public static final int MY_PLANT = 2;

    static Calendar cal = new GregorianCalendar();

    static Date dia(int dia, int mes, int anyo) {
        cal.set(Calendar.YEAR, anyo);
        cal.set(Calendar.MONTH, mes-1);
        cal.set(Calendar.DAY_OF_MONTH, dia);
        return cal.getTime();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_garden);

        myplant_list = new ArrayList<>();

        myplant_list.add (new Plant("Lola la flora", "Desconocido", "",
                dia(18,11,2018), 5,
                dia(2,12,2018), null,
                "http://www.mijardin.es/wp-content/uploads/2017/01/cultivar-la-planta-del-dinero.jpg"));


        searchadd_btn = findViewById(R.id.searchadd_btn);
        myplant_recycler = findViewById(R.id.myplant_recycler);


        garden_adapter = new Adapter();

        myplant_recycler.setLayoutManager(new LinearLayoutManager(this));
        myplant_recycler.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        );
        myplant_recycler.setAdapter(new Adapter());



        searchadd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(MyGardenActivity.this, AddPlantActivity.class);
                startActivityForResult(intent, ADD_PLANT);
            }
        });



    }

    private void onPlantClick(int pos) {
        Intent intent = new Intent(this, MyPlantActivity.class);
       // intent.putExtra("index", 3);
        startActivityForResult(intent, MY_PLANT);
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
                    //TODO: si no se ha actualizado la lista, indicar que se actualice


                }

        }
    }

}
