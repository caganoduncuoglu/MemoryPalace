package com.atm.memoryPalace;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.atm.memoryPalace.databinding.InsertMemoryBinding;
import com.atm.memoryPalace.utils.database.DatabaseHelper;

public class InsertMemoryFragment extends Fragment {

    private InsertMemoryBinding binding;
    private EditText titleText;
    private EditText descriptionText;
    private EditText locationText;
    private EditText dateText;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = InsertMemoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleText = view.findViewById(R.id.memoryInsertTitleText);
        descriptionText = view.findViewById(R.id.memoryInsertDescriptionText);
        locationText = view.findViewById(R.id.memoryInsertLocationText);
        dateText = view.findViewById(R.id.memoryInsertDateText);
        databaseHelper = new DatabaseHelper(getContext());

        binding.insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelper.addMemory(
                        titleText.getText().toString(),
                        descriptionText.getText().toString(),
                        dateText.getText().toString(),
                        locationText.getText().toString()
                );
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}