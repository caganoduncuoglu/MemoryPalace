package com.atm.memoryPalace;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.atm.memoryPalace.databinding.MainBinding;
import com.atm.memoryPalace.entity.Memory;
import com.atm.memoryPalace.utils.database.DatabaseHelper;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    private MainBinding binding;
    private LinearLayout mainLinearLayout;
    private DatabaseHelper databaseHelper;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = MainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseHelper = new DatabaseHelper(getContext());
        mainLinearLayout = view.findViewById(R.id.main_linear_layout);


        ArrayList<Memory> memories = databaseHelper.getMemoryList();
        for (int i = 0; i < memories.size() ; i++) {
            Memory memory = memories.get(i);
            Log.i("MainFragment",memory.toString());

            TextView title = new TextView(getContext());
            title.setText(memory.getTitle());
            TextView description = new TextView(getContext());
            description.setText(memory.getDescription());
            ImageView imageView = new ImageView(getContext());
            imageView.setImageBitmap(memory.bitmap);

            LinearLayout linearLayout = new LinearLayout(getContext());
            LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            linearLayoutParams.setMargins(20, 20, 20, 20);
            linearLayout.setLayoutParams(linearLayoutParams);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(title);
            linearLayout.addView(description);
            linearLayout.addView(imageView);

            mainLinearLayout.addView(linearLayout);

        }


        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(MainFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}