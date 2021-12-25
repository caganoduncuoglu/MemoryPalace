package com.atm.memoryPalace;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.atm.memoryPalace.databinding.DetailsActivityBinding;
import com.atm.memoryPalace.entity.Memory;
import com.atm.memoryPalace.utils.database.DatabaseHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailsActivity extends FragmentActivity  {

    private GoogleMap mMap;
    private DetailsActivityBinding binding;

    private Button editButton;
    private Button showMapButton;
    private Button deleteButton;
    private Memory memory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DetailsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String memoryId = getIntent().getStringExtra("memoryId");
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        memory = databaseHelper.getMemory(memoryId);


        editButton = findViewById(R.id.details_edit);
        showMapButton = findViewById(R.id.details_map);
        deleteButton = findViewById(R.id.details_delete);

        showMapButton.setOnClickListener(v -> {
            Intent mapIntent = new Intent(this, MapsActivity.class);
            mapIntent.putExtra("insertMode", false);
            startActivity(mapIntent);
        });

    }


}