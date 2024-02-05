package com.example.statehydrate;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewNamesActivity extends AppCompatActivity {

    private static final String TAG = "ViewNamesActivity";

    DatabaseHelper mDatabaseHelper;
    private ListView nameList;
    private Button returnHomeButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        mDatabaseHelper = new DatabaseHelper(this);
        nameList = findViewById(R.id.nameList);
        returnHomeButton = findViewById(R.id.returnHome);

        populateListView();

        returnHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewNamesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }//END MAIN

    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the ListView.");

        //get data and add to list
        Cursor data = mDatabaseHelper.getData();
        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()){
            //gets value of (technically) column 1 from the database and adds to arrayList
            listData.add(data.getString(1));
        }

        //Create and set List Adapter
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        nameList.setAdapter(adapter);

        //set onItemClickListener for ListView
        nameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                String lifestageExtra = null;
                Boolean biosexExtra = null, isAthleteExtra = null, isPregnantExtra = null, isBreastfeedingExtra = null;
                Log.d(TAG, "onItemClick: You clicked on " + name);

                //Cursor to input rest of data correlated with clicked name
                Cursor data = mDatabaseHelper.getData();
                while(data.moveToNext()){
                    if(name.equals(data.getString(1))){
                        lifestageExtra = data.getString(2);
                        biosexExtra = data.getInt(3) > 0;
                        isAthleteExtra = data.getInt(4) > 0;
                        isPregnantExtra = data.getInt(5) > 0;
                        isBreastfeedingExtra = data.getInt(6) > 0;
                        break;
                    }
                }
                //Cursor to select ID correlated with clicked name
                Cursor dataID = mDatabaseHelper.getItemID(name);
                int itemID = -1;
                while(dataID.moveToNext()){
                    itemID = dataID.getInt(0);
                }
                if(itemID > -1){
                    Log.d(TAG, "onItemClick: The ID is: " + itemID);
                    Intent editScreenIntent = new Intent(ViewNamesActivity.this, EditDataActivity.class);
                    editScreenIntent.putExtra("id", itemID);
                    editScreenIntent.putExtra("name", name);
                    editScreenIntent.putExtra("lifestage", lifestageExtra);//lifestage
                    editScreenIntent.putExtra("bioSex", biosexExtra);//biosex
                    editScreenIntent.putExtra("isAthlete", isAthleteExtra);//isAthlete
                    editScreenIntent.putExtra("isPregnant", isPregnantExtra);//isPregnant
                    editScreenIntent.putExtra("isBreastfeeding", isBreastfeedingExtra);//isBreastfeeding
                    startActivity(editScreenIntent);
                }else{
                    toastMessage("No ID associated with that name");
                }
            }
        });
    }//END populateListView()

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
