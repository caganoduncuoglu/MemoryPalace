package com.atm.memoryPalace;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;

import com.atm.memoryPalace.utils.database.DatabaseHelper;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

public class InsertMemoryFragment extends Fragment {

    private EditText titleText;
    private EditText descriptionText;
    private EditText locationText;
    private EditText dateText;
    private DatabaseHelper databaseHelper;
    private ImageButton galleryButton;
    private ImageButton takePhotoButton;
    private LinearLayout imageLinearLayout;
    private LinearLayout imageFieldLinearLayout;
    public static final int PICK_IMAGE = 1;
    public static final int TAKE_PHOTO = 2;
    public static final int GET_DATETIME = 3;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Bitmap bitmap;


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
        locationText = view.findViewById(R.id.memoryInsertLocationText);
        dateText = view.findViewById(R.id.memoryInsertDateText);
        galleryButton = view.findViewById(R.id.select_from_gallery);
        takePhotoButton = view.findViewById(R.id.take_from_camera);
        imageLinearLayout = view.findViewById(R.id.image_linear_layout);
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
                   } else if (locationText.getText().toString().equals("")) {
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
                           locationText.getText().toString(),
                           bitmap
                   );
                   Toast.makeText(getActivity(), "Success ! ", Toast.LENGTH_LONG).show();

               }catch(Exception e){
                   System.out.println("insertButton.onClick e: "+e);
                   Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG).show();
               }
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


    public void pickImage() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), PICK_IMAGE);
    }

    public void takePhoto() {
        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, TAKE_PHOTO);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                if (data.getData() != null) {
                    try {
                        InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                        bitmap = BitmapFactory.decodeStream(inputStream);

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
                if (data.getExtras() != null) {
                    final Bundle extras = data.getExtras();
                    bitmap = extras.getParcelable("data");

                    ImageView imageView = new ImageView(getContext());
                    imageView.setImageBitmap(bitmap);

                    imageFieldLinearLayout.removeAllViews();
                    imageFieldLinearLayout.addView(imageView);
                    imageLinearLayout.getLayoutParams().height = 750;
                    imageLinearLayout.requestLayout();
                }
            } else if (requestCode == GET_DATETIME) {
                dateText.setText(data.getStringExtra("date"));
            }
        }
    }


}