package com.example.shrey.theflyingfishgameapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.github.mikephil.charting.utils.ColorTemplate.COLORFUL_COLORS;

public class Graph_Activity extends AppCompatActivity {

   // public static databasehelper mDatabaseHelper = DatabaseActivity.mDatabaseHelper;


   public static databasehelper mDatabaseHelper;
   Button xaxis,yaxis;
   int select_axis=0;

    private dartView dart1;
    private int x1;
    private int array[];
    String patient_name;


    ArrayList<BarEntry> barentries=new ArrayList<>();
    ArrayList<BarEntry> barentries1=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_);

        final BarChart strength_chart,strength_chart1;
        strength_chart = findViewById(R.id.strength_chart);
        strength_chart1= findViewById(R.id.strength_chart1);

        mDatabaseHelper = new databasehelper(this);
        patient_name=getIntent().getStringExtra("patientName");


        xaxis=(Button) findViewById(R.id.xaxis);
        yaxis=(Button) findViewById(R.id.yaxis);



        strength_chart.setDrawBarShadow(false );
        strength_chart.setDrawValueAboveBar(true);
        strength_chart.setMaxVisibleValueCount(50);
        strength_chart.setPinchZoom(false);
        strength_chart.setDrawGridBackground(true);

        strength_chart1.setDrawBarShadow(false );
        strength_chart1.setDrawValueAboveBar(true);
        strength_chart1.setMaxVisibleValueCount(50);
        strength_chart1.setPinchZoom(false);
        strength_chart1.setDrawGridBackground(true);


       /* barentries.add(new BarEntry(1,10f));
        barentries.add(new BarEntry(2,20f));
        barentries.add(new BarEntry(3,30f));
        barentries.add(new BarEntry(4,40f));
        */

           try
        {
            data123();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        BarDataSet barDataSet=new BarDataSet(barentries,"dataset_X_AXIS");
        barDataSet.setColor(ColorTemplate.rgb("FFF033"));
        BarData data=new BarData(barDataSet);
        data.setBarWidth(0.5f);
        strength_chart.setData(data);

        BarDataSet barDataSet1=new BarDataSet(barentries1,"dataset_Y_AXIS");
        barDataSet1.setColor(ColorTemplate.rgb("0080FF"));
        BarData data1=new BarData(barDataSet1);
        data1.setBarWidth(0.5f);
        strength_chart1.setData(data1);

        xaxis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strength_chart.setVisibility(View.VISIBLE);
                strength_chart1.setVisibility(View.INVISIBLE);
            }
        });

        yaxis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strength_chart1.setVisibility(View.VISIBLE);
                strength_chart.setVisibility(View.INVISIBLE);
            }
        });






//        dart1= new dartView(this);
//        BarChart strength_chart, range_chart;
//        String strength_table_name, range_table_name;
//
//        x1=dart1.databasedata1();
//
//       // strength_table_name = getIntent().getStringExtra("patient_name")+"_strength";
//        range_table_name = getIntent().getStringExtra("patient_name");
//        strength_chart = findViewById(R.id.strength_chart);
//        range_chart = findViewById(R.id.range_chart);
//
//        strength_chart.setVisibility(View.INVISIBLE);
//
//       // chart_view(strength_chart, strength_table_name, Color.CYAN);
//        chart_view(range_chart,range_table_name, Color.YELLOW);
    }

    public void data123()
    {
        Cursor res=mDatabaseHelper.getData(patient_name);
         int counter=0;
         int x_data=0,y_data=0;
        if(res.getCount()==0)
        {
            toastMessage("No data available");
        }
        else
        {
            StringBuffer buffer= new StringBuffer();
            while (res.moveToNext())
            {
                buffer.append("x"+res.getString(1));
                buffer.append("y"+res.getString(2));
                buffer.append("z"+res.getString(3));

                x_data=Integer.parseInt(res.getString(1));
                y_data=Integer.parseInt(res.getString(2));
                //res.getString(3);

                barentries.add(new BarEntry(counter, x_data));
                barentries1.add(new BarEntry(counter,y_data));

                counter++;

            }

            showMessage("data ",buffer.toString());

        }
    }


    public void showMessage(String title, String message)
    {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


    private void chart_view(BarChart chart, String table_name, int bar_color) {

        //get the data and append to a list
//        Cursor data = mDatabaseHelper.getData(table_name);
        List<String> xAXES = new ArrayList<>();
        List<BarEntry> yAXES = new ArrayList<>();
        int x = 0, x1, y;
//        while (data.moveToNext()) {
//            x1 = Integer.parseInt(data.getString(1));
//            y = Integer.parseInt(data.getString(2));
//
//            xAXES.add(x, String.valueOf(x1));
//            yAXES.add(new BarEntry(y,x));
//            x++;
//        }
        while (x < 10) {
            x1 = x;
            y = 10-x;

            xAXES.add(x, String.valueOf(x1));
            yAXES.add(new BarEntry(y,x));
            x++;
        }

        String[] xaxes = new String[xAXES.size()];
        for(int i=0; i<xAXES.size();i++){
            xaxes[i] = xAXES.get(i).toString();
        }

        ArrayList<IBarDataSet> lineDataSets = new ArrayList<>();

        BarDataSet barDataSet = new BarDataSet(yAXES,"score");
        barDataSet.setColor(bar_color);

        lineDataSets.add(barDataSet);
        //chart.setData(new BarData(xaxes,lineDataSets));

//        gaugeChart.setVisibleXRangeMaximum(65f);

    }

    private void toastMessage(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(1,0,0);
        toast.show();
    }
}
