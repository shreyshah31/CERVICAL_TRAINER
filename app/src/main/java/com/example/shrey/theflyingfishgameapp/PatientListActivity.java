package com.example.shrey.theflyingfishgameapp;

import android.bluetooth.BluetoothSocket;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PatientListActivity extends AppCompatActivity {

    public BluetoothSocket socket=BluetoothActivity.socket;
    public static databasehelper mDatabaseHelper = DatabaseActivity.mDatabaseHelper;
    private ListView mListView;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        tvTitle = findViewById(R.id.tvTitle);
        mListView = findViewById(R.id.listView);

        populateListView();
    }

    private void populateListView() {

        //get the data and append to a list
        Cursor data = mDatabaseHelper.getData("patient_table");
        ArrayList<String> listData = new ArrayList<>();
        while (data.moveToNext()) {
            if(!(data.getString(0).equals("sqlite_sequence") || data.getString(0).equals("android_metadata")))
                listData.add(data.getString(1).replace('_',' '));
        }

        //create the list adapter and set the adapter
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        mListView.setAdapter(adapter);



        //set an onItemClickListener to the ListView
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String name = adapterView.getItemAtPosition(i).toString().replace(' ','_');
                Cursor data = mDatabaseHelper.getItemID("patient_table", name);

                int itemID = -1;
                while(data.moveToNext())
                    itemID = data.getInt(0);

                if(itemID > -1) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(PatientListActivity.this);
                    alert.setTitle(name);
                    final int iID = itemID;

                    alert.setPositiveButton("Select", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(PatientListActivity.this, SplashActivity.class);
                            intent.putExtra("patientName", name);
                            startActivity(intent);
                           finish();
                        }
                    });

                    alert.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            databasehelper mDB = DatabaseActivity.mDatabaseHelper;
                            try {
//                                mDB.deleteTable(name+"_Left_Knob_range");
//                                mDB.deleteTable(name+"_Left_Key_range");
//                                mDB.deleteTable(name+"_Left_Flexion_range");
//                                mDB.deleteTable(name+"_Left_Pronation_range");
//                                mDB.deleteTable(name+"_Left_Knob_strength");
//                                mDB.deleteTable(name+"_Left_Key_strength");
//                                mDB.deleteTable(name+"_Left_Flexion_strength");
//                                mDB.deleteTable(name+"_Left_Pronation_strength");
                                mDatabaseHelper.deletePatient(iID, name);
                                toastMessage("Removed from Database");
                            }
                            catch (Exception e){}

                            try {
//                                mDB.deleteTable(name+"_Right_Knob_range");
//                                mDB.deleteTable(name+"_Right_Key_range");
//                                mDB.deleteTable(name+"_Right_Flexion_range");
//                                mDB.deleteTable(name+"_Right_Pronation_range");
//                                mDB.deleteTable(name+"_Right_Knob_strength");
//                                mDB.deleteTable(name+"_Right_Key_strength");
//                                mDB.deleteTable(name+"_Right_Flexion_strength");
//                                mDB.deleteTable(name+"_Right_Pronation_strength");
                                mDatabaseHelper.deletePatient(iID, name);
                                toastMessage("Removed from Database");
                            }
                            catch (Exception e){}

                            finish();
                            startActivity(getIntent());
                        }
                    });

                    alert.show();
                }

            }
        });
    }
    private void toastMessage(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(1,0,0);
        toast.show();







    }
}
