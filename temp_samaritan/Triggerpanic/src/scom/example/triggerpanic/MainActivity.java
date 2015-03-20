package scom.example.triggerpanic;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private TextView txt, txt1;
	float x = 0, y = 0, z = 0;
	double sqrt = 0;
	private static final float alpha = 0.65f;
	private static final float SENSITIVITY = 1f / 128;
	private boolean firstTime = true;
	private float[] vals = { 0, 0, 0 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		txt = (TextView) findViewById(R.id.txt);
		txt1 = (TextView) findViewById(R.id.txt1);
		SensorActivity();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		x = 0;
		y = 0;
		z = 0;
		sqrt = 0;
	}

	public void SensorActivity() {
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}

	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}

	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	public void onSensorChanged(SensorEvent event) {
		float x1 = event.values[0];
		float y1 = event.values[1];
		float z1 = event.values[2];
		double squareroot = 0;
		if (firstTime) {
			vals[0] = event.values[0];
			vals[1] = event.values[1];
			vals[2] = event.values[2];
			firstTime = false;
		}
		vals[0] = vals[0] + alpha * (event.values[0] - vals[0]);
		vals[1] = vals[1] + alpha * (event.values[1] - vals[1]);
		vals[2] = vals[2] + alpha * (event.values[2] - vals[2]);
		squareroot = Math.sqrt(Double.parseDouble("" + (vals[0] * vals[0] + vals[1] * vals[1] + vals[2] * vals[2])));
		if (squareroot > sqrt) {
			sqrt = squareroot;
			txt1.setText("Acc :" + sqrt + "\nX max: " + x + "\nY max: " + y + "\nZ max: " + z);
		}

		Log.d("squareroot", "" + squareroot);
		txt.setText(mAccelerometer.getMaximumRange() + "\nX: " + event.values[0] + "\nY: " + event.values[1] + "\nZ: "
				+ event.values[2]);
		if (event.values[0] > x) {
			x = event.values[0];
			txt1.setText("Acc :" + sqrt + "\nX max: " + x + "\nY max: " + y + "\nZ max: " + z);
		}
		if (event.values[1] > y) {
			y = event.values[1];
			txt1.setText("Acc :" + sqrt + "\nX max: " + x + "\nY max: " + y + "\nZ max: " + z);
		}
		if (event.values[2] > z) {
			z = event.values[2];
			txt1.setText("Acc :" + sqrt + "\nX max: " + x + "\nY max: " + y + "\nZ max: " + z);
		}

	}

}
