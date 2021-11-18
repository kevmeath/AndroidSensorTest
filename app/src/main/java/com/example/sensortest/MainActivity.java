package com.example.sensortest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

public class MainActivity extends AppCompatActivity implements SensorEventListener, OnChartValueSelectedListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Sensor Chart");

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        }

        chart = findViewById(R.id.chart);
        chart.setOnChartValueSelectedListener(this);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        chart.setData(data);

        Legend legend = chart.getLegend();

        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextColor(Color.WHITE);

        XAxis x = chart.getXAxis();
        x.setTextColor(Color.WHITE);
        x.setDrawGridLines(false);
        x.setAvoidFirstLastClipping(true);
        x.setEnabled(true);

        YAxis y = chart.getAxisLeft();
        y.setTextColor(Color.WHITE);
        y.setAxisMaximum(10f);
        y.setAxisMinimum(-10f);
        y.setDrawGridLines(true);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void addEntry(float value) {
        LineData data = chart.getData();

        if (data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            data.addEntry(new Entry(set.getEntryCount(), value), 0);
            data.notifyDataChanged();

            chart.notifyDataSetChanged();
            chart.setVisibleXRangeMaximum(120);
            chart.moveViewToX(data.getEntryCount());
        }
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "Vertical Acceleration");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        set.setDrawCircles(false);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        addEntry(event.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}