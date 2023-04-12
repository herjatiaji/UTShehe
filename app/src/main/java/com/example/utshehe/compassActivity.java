package com.example.utshehe;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class compassActivity extends AppCompatActivity implements SensorEventListener {
    private TextView textView;
    private ImageView imageView;
    private SensorManager sensorManager;

    private Sensor acceleromaterSensor, magnetometerSensor;

    private float[] lastAccelerometer = new float[3];
    private float[] lastMagnetometer = new float[3];
    private float[] rotaionMatrix = new float[9];
    private float[] orientation = new float[3];

    boolean isLastAccelerometerArrayCopied = false;
    boolean isLastMagnetometerArrayCopied = false;
    long lastUpdateTime =0;
    float currentDegree = 0f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_compass);
        textView = findViewById(R.id.degree);
        imageView = findViewById(R.id.compass_image);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        acceleromaterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor == acceleromaterSensor){
            System.arraycopy(sensorEvent.values,0,lastAccelerometer,0,sensorEvent.values.length);
            isLastAccelerometerArrayCopied = true;
        } else if (sensorEvent.sensor == magnetometerSensor) {
            System.arraycopy(sensorEvent.values,0,lastMagnetometer,0,sensorEvent.values.length);
            isLastMagnetometerArrayCopied = true;

        }

        if (isLastAccelerometerArrayCopied && isLastMagnetometerArrayCopied && System.currentTimeMillis() - lastUpdateTime> 250){
            SensorManager.getRotationMatrix(rotaionMatrix, null, lastAccelerometer, lastMagnetometer);
            SensorManager.getOrientation(rotaionMatrix, orientation);

            float azimuthInRadians= orientation[0];
            float azimuthInDegree = (float) Math.toDegrees(azimuthInRadians);

            RotateAnimation rotateAnimation =
                    new RotateAnimation(currentDegree, -azimuthInDegree, RotateAnimation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(250);
            rotateAnimation.setFillAfter(true);
            imageView.startAnimation(rotateAnimation);

            currentDegree = -azimuthInDegree;
            lastUpdateTime = System.currentTimeMillis();

            int x = (int) azimuthInDegree;
            textView.setText(x + "°");
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(this,acceleromaterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetometerSensor,SensorManager.SENSOR_DELAY_NORMAL);


    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, acceleromaterSensor);
        sensorManager.unregisterListener(this,magnetometerSensor);
    }
}