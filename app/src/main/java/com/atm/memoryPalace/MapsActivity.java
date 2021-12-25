package com.atm.memoryPalace;

import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.atm.memoryPalace.databinding.DetailsActivityBinding;
import com.atm.memoryPalace.databinding.MapsActivityBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean insertMode = false;
    /*
    initialLng and Lng will be expected to be not null if the insertMode is false
        InsertMode will be false in 'insert new memory' page
        it will be true in the details page of an existing memory.
     */
    private double initialLat;
    private double initialLng;
    private Marker marker;
    private  FloatingActionButton mapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity);

        insertMode = getIntent().getBooleanExtra("insertMode", false);


            initialLat = getIntent().getDoubleExtra("initialLat", 41.11266);
            initialLng = getIntent().getDoubleExtra("initialLng", 29.02133);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

         mapButton = findViewById(R.id.mapsButton);
        if(insertMode){
            mapButton.setOnClickListener(
                    v -> {
                        System.out.println("v.onCreate v: "+ v);
                        Intent intent = getIntent();
                        intent.putExtra("maps_location",new LatLng(initialLat,initialLng));
                        this.setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
            );
        }else{
            mapButton.setVisibility(View.INVISIBLE);
        }

        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng initialLoc = new LatLng(initialLat, initialLng);
        marker = mMap.addMarker(new MarkerOptions().position(initialLoc));
        mMap.setMinZoomPreference(8);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(initialLoc));
        mMap.setOnMapClickListener(v -> {

            if(insertMode){
                LatLng newLoc = new LatLng(v.latitude,v.longitude);
                marker.remove();
                marker = mMap.addMarker(new MarkerOptions().position(newLoc));
                initialLat = v.latitude;
                initialLng = v.longitude;
            mapButton.setVisibility(View.VISIBLE);
            }
        });
    }

  /*  public void ReturnMapData(LatLng arg) {
        mCodingActivity.GetLocationFromMap(mLatLng);
    }*/
}