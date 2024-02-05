package com.example.statehydrate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //INSTANTIATE VARIABLES
    DatabaseHelper mDatabaseHelper;
    private TextView app_title;
    private DigitalClock display_time;
    private Button addData;
    private Button viewNames;
    private Switch sexInput; //Using switches as true/false booleans by using the isChecked() method
    private Switch athleteInput;
    private Switch pregnantInput;
    private Switch breastfeedingInput;
    private EditText nameInput;
    private EditText lifestageInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //LINK VARIABLES TO THEIR ID's
        app_title = findViewById(R.id.appTitle);
        display_time = findViewById(R.id.displayTime);
        addData = findViewById(R.id.addData);
        viewNames = findViewById(R.id.viewNames);
        nameInput = findViewById(R.id.nameInput);
        lifestageInput = findViewById(R.id.lifestageInput);
        sexInput = findViewById(R.id.sexInput);
        athleteInput = findViewById(R.id.athleteInput);
        pregnantInput = findViewById(R.id.pregnantInput);
        breastfeedingInput = findViewById(R.id.breastfeedingInput);
        mDatabaseHelper = new DatabaseHelper(this);

        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Assign and check name for correct input
                String newEntry = nameInput.getText().toString();
                String lifestageEntry = lifestageInput.getText().toString().toLowerCase();
                Boolean bioSexEntry = sexInput.isChecked();
                Boolean athleteEntry = athleteInput.isChecked();
                Boolean pregnantEntry = pregnantInput.isChecked();
                Boolean breastFeedingEntry = breastfeedingInput.isChecked();
                Double curWaterEntry = 0.0;
                Double waterPerHourEntry = 0.0;

                Double calcWaterEntry = 0.0;
                //Sets water requirement based on lifestage
                if(lifestageEntry.equals("teen")){
                    calcWaterEntry = 2.6;
                }else if(lifestageEntry.equals("adult")){
                    calcWaterEntry = bioSexEntry ? 2.7 : 3.7;
                }else{
                    calcWaterEntry = bioSexEntry ? 1.6 : 2.0;
                }
                if(pregnantEntry){
                    calcWaterEntry += 0.3;
                }
                if(breastFeedingEntry){
                    calcWaterEntry += 0.8;
                }
                if(athleteEntry){
                    calcWaterEntry += 0.5;
                }


                //Must check String values for correct input, Boolean values do not need checking
                if(nameInput.length() == 0){
                    toastMessage("You must insert a name first!");
                }else{
                    if((lifestageEntry.equals("teen") || lifestageEntry.equals("adult") || lifestageEntry.equals("elderly"))
                            && lifestageInput.length() != 0){

                        addData2(newEntry, lifestageEntry, bioSexEntry, athleteEntry, pregnantEntry, breastFeedingEntry, calcWaterEntry,
                                curWaterEntry, waterPerHourEntry);
                        nameInput.setText("");
                        lifestageInput.setText("");
                    }else{
                        toastMessage("You can only enter 'teen', 'adult', or 'elderly'!");
                    }
                }
            }
        });//END addName Button

        viewNames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewNamesActivity.class);
                startActivity(intent);
            }
        });//END viewNames Button

    }//END OF onCreate aka MAIN

    public void addData2(String name, String lifestage, Boolean bioSex, Boolean isAthlete, Boolean isPregnant, Boolean isBreastfeeding,
                         Double calcWater, Double curWater, Double waterPerHour){
        boolean insertData = mDatabaseHelper.addData2(name, lifestage, bioSex, isAthlete, isPregnant, isBreastfeeding, calcWater,
                curWater, waterPerHour);

        if(insertData){
            toastMessage("Data successfully inserted!");
        }else{
            toastMessage("Data insertion failed!");
        }
    }

    public void addData(String newEntry){
        boolean insertData = mDatabaseHelper.addData(newEntry);

        if(insertData){
            toastMessage("Data successfully inserted!");
        }else{
            toastMessage("Data insertion failed!");
        }
    }//END addData

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }//END toastMessage
}