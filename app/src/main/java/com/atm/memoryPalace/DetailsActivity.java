package com.atm.memoryPalace;

import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.memoryPalace.databinding.DetailsActivityBinding;
import com.atm.memoryPalace.entity.Memory;
import com.atm.memoryPalace.utils.database.DatabaseHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

public class DetailsActivity extends FragmentActivity  {

    private GoogleMap mMap;
    private DetailsActivityBinding binding;

    private Button editButton;
    private ImageButton showMapButton;
    private ImageButton deleteButton;
    private ImageButton backButton;
    private Memory memory;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DetailsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String memoryId = getIntent().getStringExtra("memoryId");
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        memory = databaseHelper.getMemory(memoryId);



        showMapButton = findViewById(R.id.details_map);

        ImageView image = (ImageView) findViewById(R.id.details_image);
        int width = memory.bitmap.getWidth();
        int height = memory.bitmap.getHeight();
        image.setImageBitmap(Bitmap.createBitmap(memory.bitmap, 0,0,width, height));

        TextView title = (TextView) findViewById(R.id.title_text);
        title.setText(memory.getTitle());

        TextView date = (TextView) findViewById(R.id.date_text);
        date.setText(memory.getDate());

        TextView details = (TextView) findViewById(R.id.details_text);
        details.setText(memory.getDescription());


        deleteButton = findViewById(R.id.details_delete);
        backButton = findViewById(R.id.return_home);


        backButton.setOnClickListener(v -> {
            Intent homeIntent = new Intent(this, MainActivity.class);
         //   homeIntent.putExtra("insertMode", false);
            startActivity(homeIntent);
        });

        showMapButton.setOnClickListener(v -> {
            Intent mapIntent = new Intent(this, MapsActivity.class);
            mapIntent.putExtra("insertMode", false);
            startActivity(mapIntent);
        });

        deleteButton.setOnClickListener(v -> {
            Intent homeIntent = new Intent(this, MainActivity.class);
            startActivity(homeIntent);
            databaseHelper.deleteMemory(memoryId);
            Toast.makeText(getApplicationContext(), "Deleted ! ", Toast.LENGTH_LONG).show();
        });

    }


}