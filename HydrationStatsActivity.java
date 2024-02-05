package com.example.statehydrate;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

public class HydrationStatsActivity extends AppCompatActivity {

    private static final String TAG = "HydrationStatsActivity";

    DecimalFormat f = new DecimalFormat("##.00");
    DatabaseHelper mDataBaseHelper;
    private TextView nameText;
    private TextView progressText;
    private TextView waterPerHourText;
    private TextView divyText;
    private TextView goalText;
    private Button btnHydroLog;

    private String name;
    private int ID;
    private Double calcWater;
    private Double curWater;
    private Double waterPerHour;
    //Get variables for water calculation from database using name as the key

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hydration_stats_activity);

        //assigning items to ids
        mDataBaseHelper = new DatabaseHelper(this);
        nameText = findViewById(R.id.nameText);
        progressText = findViewById(R.id.progressText);
        waterPerHourText = findViewById(R.id.waterPerHourText);
        divyText = findViewById(R.id.divyText);
        goalText = findViewById(R.id.goalText);
        btnHydroLog = findViewById(R.id.btnHydroLog);

        //assigning values to variables
        Intent receivedIntent = getIntent();
        name = receivedIntent.getStringExtra("name");
        Cursor data = mDataBaseHelper.getData();
        while(data.moveToNext()){
            if(name.equals(data.getString(1))){
                ID = data.getInt(0);
                calcWater = data.getDouble(7);
                curWater = data.getDouble(8);
                waterPerHour = data.getDouble(9);
                break;
            }
        }

        //Set text to nameText
        nameText.setText(new StringBuilder().append(name).append("'(s) Hydration Log").toString());
        progressText.setText(new StringBuilder().append(f.format(curWater) + " liters").append(" of ").append(calcWater).append(" liters consumed").toString());
        waterPerHourText.setText(new StringBuilder().append(f.format(waterPerHour)).append(" liters/hour consumed today").toString());
        divyText.setText(new StringBuilder().append("Press to log 1/3 of hydration goal (").append(f.format(calcWater / 3)).append(" liters)").toString());

        if(curWater >= calcWater){
            goalText.setVisibility(View.VISIBLE);
        }

        btnHydroLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double oldCurWater = curWater;
                Double oldWaterPerHour = waterPerHour;
                Double added = calcWater/3.0;
                Date currentTime = Calendar.getInstance().getTime();
                Integer hoursPassed = currentTime.getHours();

                if(curWater < calcWater){
                    curWater += added;

                    waterPerHour = curWater/hoursPassed;
                    //UPDATE curWater and waterPerHour in DATABASE
                    mDataBaseHelper.updateCurWater(curWater, ID, oldCurWater);
                    mDataBaseHelper.updateWaterPerHour(waterPerHour, ID, oldWaterPerHour);

                    //SET TEXTS WITH NEW VALUES
                    progressText.setText(new StringBuilder().append(f.format(curWater) + " liters").append(" of ").append(calcWater).append(" liters consumed").toString());
                    waterPerHourText.setText(new StringBuilder().append(f.format(waterPerHour)).append(" liters/hour consumed today").toString());
                }

                if(curWater >= calcWater){
                    goalText.setVisibility(View.VISIBLE);
                }

            }
        });//END btnHydroLog

    }//END onCreate/MAIN



}
