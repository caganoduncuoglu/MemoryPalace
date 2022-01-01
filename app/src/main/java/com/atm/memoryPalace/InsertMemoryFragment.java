package com.atm.memoryPalace;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.atm.memoryPalace.utils.database.DatabaseHelper;
import com.atm.memoryPalace.utils.database.LocationUtil;
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

public class InsertMemoryFragment extends Fragment {

    private EditText titleText;
    private EditText descriptionText;
    private EditText dateText;
    private DatabaseHelper databaseHelper;
    private ImageButton galleryButton;
    private ImageButton takePhotoButton;
    private ImageButton locationButton;
    private LinearLayout imageLinearLayout;
    private LinearLayout locationImageLinearLayout;
    private LinearLayout imageFieldLinearLayout;
    public static final int PICK_IMAGE = 1;
    public static final int TAKE_PHOTO = 2;
    public static final int GET_DATETIME = 3;
    public static final int GET_LOCATION = 4;
    private Bitmap bitmap;
    private LatLng selectedLocation;
    private Uri imageUri;


    public InsertMemoryFragment() {

    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        return inflater.inflate(R.layout.insert_memory, container, false);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleText = view.findViewById(R.id.memoryInsertTitleText);
        descriptionText = view.findViewById(R.id.memoryInsertDescriptionText);
        locationButton = view.findViewById(R.id.setLocationButton);
        dateText = view.findViewById(R.id.memoryInsertDateText);
        galleryButton = view.findViewById(R.id.select_from_gallery);
        takePhotoButton = view.findViewById(R.id.take_from_camera);
        imageLinearLayout = view.findViewById(R.id.image_linear_layout);
        locationImageLinearLayout = view.findViewById(R.id.location_image_linear_layout);
        imageFieldLinearLayout = view.findViewById(R.id.image_field_linear_layout);
        databaseHelper = new DatabaseHelper(getContext());

        dateText.setFocusable(false);
        dateText.setText(DatabaseHelper.format.format(new Date()));

        Button insertButton = view.findViewById(R.id.insert_button);
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               try{
                   if (titleText.getText().toString().equals("")) {
                       Toast.makeText(getActivity(), "Title should not be blank!", Toast.LENGTH_LONG).show();
                       return;
                   } else if (descriptionText.getText().toString().equals("")) {
                       Toast.makeText(getActivity(), "Description should not be blank!", Toast.LENGTH_LONG).show();
                       return;
                   } else if (selectedLocation == null) {
                       Toast.makeText(getActivity(), "Location should not be blank!", Toast.LENGTH_LONG).show();
                       return;
                   } else if (bitmap == null) {
                       Toast.makeText(getActivity(), "Image should not be blank!", Toast.LENGTH_LONG).show();
                       return;
                   }

                   databaseHelper.addMemory(
                           titleText.getText().toString(),
                           descriptionText.getText().toString(),
                           dateText.getText().toString(),
                           selectedLocation,
                           bitmap
                   );
                   Toast.makeText(getActivity(), "Success ! ", Toast.LENGTH_LONG).show();

               }catch(Exception e){
                   System.out.println("insertButton.onClick e: "+e);
                   Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG).show();
               }
            }
        });

       locationButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               getLocation();
           }
       });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DateTimePickerActivity.class);
                startActivityForResult(intent, GET_DATETIME);
            }
        });
    }


    public void getLocation(){
        Intent mapIntent = new Intent(getContext(), MapsActivity.class);
        mapIntent.putExtra("insertMode", true);
        startActivityForResult(mapIntent, GET_LOCATION);

    }

    public void pickImage() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), PICK_IMAGE);
    }

    public void takePhoto() {
      /*  requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);*/
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 110);
        } else {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
            imageUri = getContext().getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

            startActivityForResult(cameraIntent, TAKE_PHOTO);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == TAKE_PHOTO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            }
        }}

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                if (data.getData() != null) {
                    try {
                        InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                        bitmap = getResizedBitmap(BitmapFactory.decodeStream(inputStream),1000);

                        ImageView imageView = new ImageView(getContext());
                        imageView.setImageBitmap(bitmap);

                        imageFieldLinearLayout.removeAllViews();
                        imageFieldLinearLayout.addView(imageView);
                        imageLinearLayout.getLayoutParams().height = 750;
                        imageLinearLayout.requestLayout();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == TAKE_PHOTO) {
                try {
                    bitmap = getResizedBitmap(MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri),1000);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                ImageView imageView = new ImageView(getContext());
                imageView.setImageBitmap(bitmap);

                imageFieldLinearLayout.removeAllViews();
                imageFieldLinearLayout.addView(imageView);
                imageLinearLayout.getLayoutParams().height = 750;
                imageLinearLayout.requestLayout();
            } else if (requestCode == GET_DATETIME) {
                dateText.setText(data.getStringExtra("date"));
            }else if(requestCode == GET_LOCATION) {

                selectedLocation = data.getParcelableExtra("maps_location");
                new DownloadImageTask().execute(LocationUtil.getMapImageUrl(selectedLocation));

            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private class DownloadImageTask extends AsyncTask<String,Void,Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            return loadBitmap(urls[0]);
        }

        Bitmap loadBitmap(String url) {
            Bitmap bitmap = null;
            try {
                InputStream in = new BufferedInputStream(new URL(url).openStream());
                bitmap = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                System.out.println("Could not load Bitmap from: " + url);
                System.out.println(".loadBitmap e: "+ e);
            }

            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageBitmap(result);
            locationImageLinearLayout.removeAllViews();
            locationImageLinearLayout.addView(imageView);
            locationImageLinearLayout.getLayoutParams().height = 600;
            locationImageLinearLayout.requestLayout();
        }
    }

}


