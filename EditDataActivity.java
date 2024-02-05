package com.example.statehydrate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.function.BinaryOperator;

public class EditDataActivity extends AppCompatActivity {

    private static final String TAG = "EditDataActivity";

    private Button btnSave, btnDelete, btnHydrationStats;
    private EditText editable_name;
    private EditText editable_lifestage;
    private Switch editable_bioSex;
    private Switch editable_isAthlete;
    private Switch editable_isPregnant;
    private Switch editable_isBreastfeeding;
    DatabaseHelper mDataBaseHelper;

    private int selectedID;
    private String selectedName;
    private String selectedLifestage;
    private Boolean selectedBioSex;
    private Boolean selectedIsAthlete;
    private Boolean selectedIsPregnant;
    private Boolean selectedIsBreastfeeding;


    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_data_layout);

        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        btnHydrationStats = findViewById(R.id.btnHydrationStats);
        editable_name = findViewById(R.id.editable_name);
        editable_lifestage = findViewById(R.id.editable_lifestage);
        editable_bioSex = findViewById(R.id.editable_sexInput);
        editable_isAthlete = findViewById(R.id.editable_athleteInput);
        editable_isPregnant = findViewById(R.id.editable_pregnantInput);
        editable_isBreastfeeding = findViewById(R.id.editable_breastfeedingInput);
        mDataBaseHelper = new DatabaseHelper(this);

        //Get intent data from clicking on item in ViewNamesActivity
        Intent receivedIntent = getIntent();
        selectedID = receivedIntent.getIntExtra("id", -1);
        selectedName = receivedIntent.getStringExtra("name");
        selectedLifestage = receivedIntent.getStringExtra("lifestage");
        selectedBioSex = receivedIntent.getBooleanExtra("bioSex", false);
        selectedIsAthlete = receivedIntent.getBooleanExtra("isAthlete", false);
        selectedIsPregnant = receivedIntent.getBooleanExtra("isPregnant", false);
        selectedIsBreastfeeding = receivedIntent.getBooleanExtra("isBreastfeeding", false);

        Log.d(TAG, "onCreate EditDataActivity: Lifestage: " + selectedLifestage + " bioSex: " +  selectedBioSex + " isAthlete: " +
                selectedIsAthlete + " isPregnant: " + selectedIsPregnant + " isBreastfeeding: " + selectedIsBreastfeeding);

        //set text to editView
        editable_name.setText(selectedName);
        editable_lifestage.setText(selectedLifestage);

        //set switches to their positions
        if(selectedBioSex){
            editable_bioSex.setChecked(true);
        }
        if(selectedIsAthlete){
            editable_isAthlete.setChecked(true);
        }
        if(selectedIsPregnant){
            editable_isPregnant.setChecked(true);
        }
        if(selectedIsBreastfeeding){
            editable_isBreastfeeding.setChecked(true);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item = editable_name.getText().toString();
                String lifestageEntry = editable_lifestage.getText().toString().toLowerCase();
                Boolean bioSexEntry = editable_bioSex.isChecked();
                Boolean athleteEntry = editable_isAthlete.isChecked();
                Boolean pregnantEntry = editable_isPregnant.isChecked();
                Boolean breastFeedingEntry = editable_isBreastfeeding.isChecked();

                if(item.length() == 0){
                    toastMessage("You must enter a name");
                }else{
                    if((lifestageEntry.equals("teen") || lifestageEntry.equals("adult") || lifestageEntry.equals("elderly"))
                            && editable_lifestage.length() != 0){

                        mDataBaseHelper.updateName(item, selectedID, selectedName);
                        mDataBaseHelper.updateLifestage(lifestageEntry, selectedID, selectedLifestage);
                        mDataBaseHelper.updateBioSex(bioSexEntry, selectedID, selectedBioSex);
                        mDataBaseHelper.updateIsAthlete(athleteEntry, selectedID, selectedIsAthlete);
                        mDataBaseHelper.updateIsPregnant(pregnantEntry, selectedID, selectedIsPregnant);
                        mDataBaseHelper.updateIsBreastfeeding(breastFeedingEntry, selectedID, selectedIsBreastfeeding);
                        toastMessage("Data saved!");
                    }else{
                        toastMessage("You can only enter 'teen', 'adult', or 'elderly'!");
                    }
                }
            }
        });//END btnSave()

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDataBaseHelper.deleteName(selectedID, selectedName);
                editable_name.setText("");
                toastMessage("Account removed from database");
            }
        });//END btnDelete()

        //Need intent for name
        btnHydrationStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent hydrationStatsIntent = new Intent(EditDataActivity.this, HydrationStatsActivity.class);
                hydrationStatsIntent.putExtra("name", selectedName);
                startActivity(hydrationStatsIntent);
            }
        });

    }//END MAIN/onCreate


    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }//END toastMessage









}
