package pl.wlabuda.backhomeradar;

import android.app.Activity;
import android.content.Intent;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

// implement SensorListener
public class Compass extends Activity implements SensorListener {
	SensorManager sensorManager;
	static final int sensor = SensorManager.SENSOR_ORIENTATION;
	Rose rose;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set full screen view
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				//WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);

		rose = new Rose(this);

		setContentView(rose);

		// get sensor manager
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "Znajdz droge powrotna");
		menu.add(0, 1, 0, "Kompas");
		menu.add(0, 2, 0, "Kontrola obiektu");
		menu.add(0, 3, 0, "Zamknij aplikację");
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case 0:
			Intent intent0 = new Intent(getApplicationContext(),
					RightWay.class);
			startActivity(intent0);
			break;
		case 2:
			Intent intent2 = new Intent(getApplicationContext(),
				Gps2sms.class);
			startActivity(intent2);
			break;
		case 3:
			moveTaskToBack(true);
			break;
		default:
			break;
		}
		return false;
	}
	

	// register to listen to sensors
	@Override
	public void onResume() {
		super.onResume();
		sensorManager.registerListener(this, sensor);
	}

	// unregister
	@Override
	public void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this);
	}

	// Ignore for now
	public void onAccuracyChanged(int sensor, int accuracy) {
	}

	// Listen to sensor and provide output
	public void onSensorChanged(int sensor, float[] values) {
		if (sensor != Compass.sensor)
			return;
		int pitch = (int) values[2];
		int roll = (int) values[1];
		int orientation = (int) values[0];
		rose.setDirection(orientation);
		rose.setPitch(pitch);
		rose.setRoll(roll);
	}
}