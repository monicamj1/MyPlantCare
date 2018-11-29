package com.example.monicamj1.myplantcare;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyGardenActivity extends AppCompatActivity {

    //Modelo
    List<Plant> myplant_list;

    //Referencias
    RecyclerView myplant_recycler;
    Adapter garden_adapter;
    FloatingActionButton searchadd_btn;

    public static final int ADD_PLANT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_garden);

        myplant_list = new ArrayList<>();

        myplant_list.add(new Plant("Lola la flora", "Desconocido", "",
                new Date(2018,11,18), 5,
                new Date(2018,11,18), null,
                "http://www.mijardin.es/wp-content/uploads/2017/01/cultivar-la-planta-del-dinero.jpg"));

        myplant_list.add(new Plant("Pepe el cactus", "Desconocido", "",
                new Date(2018,11,18), 4,
                new Date(2018,11,18), null,
                "http://www.mijardin.es/wp-content/uploads/2017/01/cultivar-la-planta-del-dinero.jpg"));

        myplant_list.add(new Plant("Paco el cocotero", "Desconocido", "",
                new Date(2018,11,18), 4,
                new Date(2018,11,18), null,
                "http://www.mijardin.es/wp-content/uploads/2017/01/cultivar-la-planta-del-dinero.jpg"));

        myplant_list.add(new Plant("Pepe el cactus", "Desconocido", "",
                new Date(2018,11,18), 4,
                new Date(2018,11,18), null,
                "http://www.mijardin.es/wp-content/uploads/2017/01/cultivar-la-planta-del-dinero.jpg"));

        myplant_list.add(new Plant("Pepe el cactus", "Desconocido", "",
                new Date(2018,11,18), 4,
                new Date(2018,11,18), null,
                "http://www.mijardin.es/wp-content/uploads/2017/01/cultivar-la-planta-del-dinero.jpg"));

        myplant_list.add(new Plant("Pepe el cactus", "Desconocido", "",
                new Date(2018,11,18), 4,
                new Date(2018,11,18), null,
                "http://www.mijardin.es/wp-content/uploads/2017/01/cultivar-la-planta-del-dinero.jpg"));

        myplant_list.add(new Plant("Pepe el cactus", "Desconocido", "",
                new Date(2018,11,18), 4,
                new Date(2018,11,18), null,
                "http://www.mijardin.es/wp-content/uploads/2017/01/cultivar-la-planta-del-dinero.jpg"));

        myplant_list.add(new Plant("Pepe el cactus", "Desconocido", "",
                new Date(2018,11,18), 4,
                new Date(2018,11,18), null,
                "http://www.mijardin.es/wp-content/uploads/2017/01/cultivar-la-planta-del-dinero.jpg"));

        myplant_list.add(new Plant("Pepe el cactus", "Desconocido", "",
                new Date(2018,11,18), 4,
                new Date(2018,11,18), null,
                "http://www.mijardin.es/wp-content/uploads/2017/01/cultivar-la-planta-del-dinero.jpg"));

        searchadd_btn = findViewById(R.id.searchadd_btn);
        myplant_recycler = findViewById(R.id.myplant_recycler);


        garden_adapter = new Adapter();

        myplant_recycler.setLayoutManager(new LinearLayoutManager(this));
        myplant_recycler.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        );
        myplant_recycler.setAdapter(new Adapter());


        /*
        searchadd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add_intent = new Intent(this, AddPlantActivity.class);
                startActivityForResult(add_intent, ADD_PLANT);
            }
        });
    */

    }

    private void onPlantClick(int pos) {
        Toast.makeText(this, String.format("Has clicado la planta %d", pos), Toast.LENGTH_SHORT).show();
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
           // plantimage_view.setImageBitmap(item.getProfile());
            name_view.setText(item.getName());
            reminder_view.setText("Watering in:");

            //TODO: Calcular los días que quedan para regar
            /*
            Date last_date = item.getLast_watering_day();
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date();
            System.out.println(dateFormat.format(date));

            long diffTime = date.getTime() - last_date.getTime();
            long diffDays = diffTime / (1000 * 60 * 60 * 24);
            int days = item.getReminder() - (int)diffDays;
            if(days <= 0){
                days = 0;
            }
            ////////////////////////////////////////////////////
            days_view.setText( days + " days");*/
            days_view.setText( item.getReminder() + " days");
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




}
