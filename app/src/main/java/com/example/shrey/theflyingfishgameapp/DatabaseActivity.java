package com.example.shrey.theflyingfishgameapp;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;

public class DatabaseActivity extends AppCompatActivity {

    public BluetoothSocket socket=BluetoothActivity.socket;


    public static databasehelper mDatabaseHelper;
    private Handler handler = new Handler();

    private TextView tvName, tvPatientID, tvAge, tvContact, tvAilment;
    private EditText etName, etPatientID, etAge, etContact, etAilment;
    private RadioGroup rgGender;
    private Button btnAdd, btnList;

    private String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        mDatabaseHelper = new databasehelper(this);

        //tvName = findViewById(R.id.tvName);
      //  tvPatientID = findViewById(R.id.tvPatientID);
      //  tvAge = findViewById(R.id.tvAge);
       // tvContact = findViewById(R.id.tvContact);
       // tvAilment = findViewById(R.id.tvDiagnosis);
        etName = findViewById(R.id.etName);
        etPatientID = findViewById(R.id.etPatientID);
        etAge = findViewById(R.id.etAge);
        etContact = findViewById(R.id.etContact);
        etAilment = findViewById(R.id.etDiagnosis);
        rgGender = findViewById(R.id.rgGender);
        btnAdd = findViewById(R.id.submit);
        btnList = findViewById(R.id.list);

        rgGender.clearCheck();
        mDatabaseHelper.createPatientTable();
//        mDatabaseHelper.deleteTable("patient_table");

        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = findViewById(i);
                gender = rb.getText().toString();
//                toastMessage(gender);
            }
        });

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(DatabaseActivity.this, PatientListActivity.class);
                startActivity(intent);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(rgGender.getCheckedRadioButtonId() == -1)
                    toastMessage("Please Select Gender");
                else {
                    String name = etName.getText().toString();
                    String patientID = etPatientID.getText().toString();
                    String age = etAge.getText().toString();
                    String contact = etContact.getText().toString();
                    String ailment = etAilment.getText().toString();
                    char[] namechars = name.toCharArray();
                    char[] patientIDchars = patientID.toCharArray();
                    char[] agechars = age.toCharArray();
                    char[] contactchars = contact.toCharArray();
                    char[] ailmentchars = ailment.toCharArray();

                    if (name.length() > 0 && patientID.length() > 0 && age.length() > 0 &&
                            contact.length() > 0 && ailment.length() > 0) {

                        boolean ok = true, isAdded=false;
                        if(ok) {
                            for (char i : namechars) {
                                if (!((i >= 48 && i <= 57) ||
                                        (i >= 65 && i <= 90) ||
                                        (i >= 97 && i <= 122) ||
                                        i == 95 || i == 32)) {
                                    toastMessage("Please enter only alphanumeric characters");
                                    ok = false;
                                    break;
                                }
                            }
                        }
                        if(ok) {
                            for (char i : patientIDchars) {
                                if (!((i >= 48 && i <= 57) ||
                                        (i >= 65 && i <= 90) ||
                                        (i >= 97 && i <= 122) ||
                                        i == 95 || i == 32)) {
                                    toastMessage("Please enter only alphanumeric characters");
                                    ok = false;
                                    break;
                                }
                            }
                        }
                        if(ok) {
                            for (char i : agechars) {
                                if (!((i >= 48 && i <= 57) ||
                                        (i >= 65 && i <= 90) ||
                                        (i >= 97 && i <= 122) ||
                                        i == 95 || i == 32)) {
                                    toastMessage("Please enter only alphanumeric characters");
                                    ok = false;
                                    break;
                                }
                            }
                        }
                        if(ok) {
                            for (char i : contactchars) {
                                if (!((i >= 48 && i <= 57) ||
                                        (i >= 65 && i <= 90) ||
                                        (i >= 97 && i <= 122) ||
                                        i == 95 || i == 32)) {
                                    toastMessage("Please enter only alphanumeric characters");
                                    ok = false;
                                    break;
                                }
                            }
                        }
                        if(ok) {
                            for (char i : ailmentchars) {
                                if (!((i >= 48 && i <= 57) ||
                                        (i >= 65 && i <= 90) ||
                                        (i >= 97 && i <= 122) ||
                                        i == 95 || i == 32)) {
                                    toastMessage("Please enter only alphanumeric characters");
                                    ok = false;
                                    break;
                                }
                            }
                        }

                        if(ok) {

                            name = name.replace(' ', '_');
                            patientID = patientID.replace(' ', '_');
                            age = age.replace(' ', '_');
                            contact = contact.replace(' ', '_');
                            ailment = ailment.replace(' ', '_');

                            try {
                                AddPatientData(name, patientID, age, contact, ailment, gender);
                                isAdded = true;
                            }
                            catch (Exception e) {
                                toastMessage("Please use only alphanumeric characters");
                            }
                        }

                        if(isAdded){
                            etName.getText().clear();
                            etPatientID.getText().clear();
                            etAge.getText().clear();
                            etContact.getText().clear();
                            etAilment.getText().clear();
                            try {
                                rgGender.clearCheck();
                            }
                            catch(Exception e){}
//                            finish();
//                            startActivity(getIntent());
                        }
                    }
                    else {
                        toastMessage("Please enter valid data");
                    }
                }
            }
        });
    }



    public void AddPatientData(String name, String patientID, String age, String contact,
                               String ailment, String gender) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String ts = timestamp.toString();
//        ts = ts.replace("-", "_");
//        ts = ts.replace(" ", "_");
//        ts = ts.replace(":", "_");
//        ts = ts.replace(".", "_");

        boolean insertData = mDatabaseHelper.addPatientData(name, patientID, age, contact,
                ailment, gender, ts);

        if (insertData) {
            toastMessage("Patient Profile Successfully Created!");
        }
        else {
            toastMessage("Something went wrong");
        }
    }


    public static void Addanalysis(String x,String y,String z,String table) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String ts = timestamp.toString();
//        ts = ts.replace("-", "_");
//        ts = ts.replace(" ", "_");
//        ts = ts.replace(":", "_");
//        ts = ts.replace(".", "_");

        boolean insertData = mDatabaseHelper.addAnalysis( x,y,z,table);

        if (insertData) {
          //  toastMessage("Patient Profile Successfully Created!");
        }
        else {
           // toastMessage("Something went wrong");
        }



    }

    public void toastMessage(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(1,0,0);
        toast.show();
    }


//    databasehelper myda;
//    EditText name,age,mobile;
//    Button add;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_database);
//        myda=new databasehelper(this);
//
//        name =findViewById(R.id.name);
//        age =findViewById(R.id.age);
//        mobile =findViewById(R.id.mobile);
//        add=findViewById(R.id.add_button);
//
//        Addata();
//
//    }
//
//    public void Addata()
//    {
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//              boolean insert=  myda.datainsert(name.getText().toString(),age.getText().toString(),mobile.getText().toString());
//                if(insert=true)
//                {
//                    Toast.makeText(DatabaseActivity.this,"data is inserted",Toast.LENGTH_LONG).show();
//                }
//                else
//                {
//                    Toast.makeText(DatabaseActivity.this,"data not inserted",Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//    }

}
