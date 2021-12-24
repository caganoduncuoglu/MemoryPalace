package com.atm.memoryPalace;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.atm.memoryPalace.entity.Memory;
import com.atm.memoryPalace.utils.database.DatabaseHelper;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    private LinearLayout mainLinearLayout;
    private DatabaseHelper databaseHelper;


    public MainFragment() {

    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        return inflater.inflate(R.layout.home_fragment, container, false);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseHelper = new DatabaseHelper(getContext());
        mainLinearLayout = view.findViewById(R.id.home_linear_layout);

        WindowManager w = getActivity().getWindowManager();
        Display d = w.getDefaultDisplay();
        int screenWidth = d.getWidth();

        ArrayList<Memory> memories = databaseHelper.getMemoryList();
        for (int i = 0; i < memories.size(); i++) {
            Memory memory = memories.get(i);
            Log.i("MainFragment", memory.toString());

            TextView title = new TextView(getContext());
            title.setText(memory.getTitle());
            TextView description = new TextView(getContext());
            description.setText(memory.getDescription());
            ImageView imageView = new ImageView(getContext());
            imageView.setImageBitmap(memory.bitmap);
            imageView.setAdjustViewBounds(false);

            int width = (int) (screenWidth * 0.8);
            double aspectRatio = 4;
            int height = (int) (width * (1 / aspectRatio));
            imageView.setMaxWidth(width);
            imageView.setMaxHeight(height);
            imageView.setScaleType(ImageView.ScaleType.FIT_START);
            System.out.println("imageView.onViewCreated width:" + width + " height: " + height);

            LinearLayout linearLayout = new LinearLayout(getContext());
            LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            linearLayoutParams.setMargins(20, 0, 20, 100);
            linearLayout.setLayoutParams(linearLayoutParams);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(imageView);
            linearLayout.addView(title);
            linearLayout.addView(description);

            linearLayout.setOnClickListener(v -> {
                        System.out.println(".onViewCreated you clicked the " + v);
                        Intent myIntent = new Intent(getContext(), DetailsActivity.class);
                        myIntent.putExtra("memory", memory.toString());
                        this.startActivity(myIntent);
                    }
            );
            View viewDivider = new View(getContext());
            int dividerHeight = (int) (getResources().getDisplayMetrics().density * 1);
            viewDivider.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dividerHeight));
            viewDivider.setBackgroundColor(Color.parseColor("#000000"));

            linearLayout.addView(viewDivider);
           /* linearLayout.addView(new View(

            ));
            <View
            android:id="@+id/line"
            android:layout_width="match_parent"
            android:layout_height="1dp"
            android:background="@color/black"/>*/

            mainLinearLayout.addView(linearLayout);

        }
    }


}