package pl.wlabuda.backhomeradar;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RightWay extends Activity implements SensorEventListener {

	private static SensorManager mySensorManager;
	private boolean sersorrunning;
	private MyWayView myCompassView;
	private TextView textviewPitch, textviewRoll;
	private TextView dl1;
	private TextView dl2;
	private TextView sz1;
	private TextView sz2;
	private TextView dystans;
	private TextView cel;
	private TextView pamiec1;
	private TextView pamiec2;
	private LocationManager lm;
	private LocationListener locationListener;
	private Location locB;
	private Location locA;
	private float distance = 0;
	private double c = -1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainkompass);
		dl1 = (TextView) findViewById(R.id.dl1);
		sz1 = (TextView) findViewById(R.id.sz1);
		dl2 = (TextView) findViewById(R.id.dl2);
		sz2 = (TextView) findViewById(R.id.sz2);
		pamiec1 = (TextView) findViewById(R.id.pamiec1);
		pamiec2 = (TextView) findViewById(R.id.pamiec2);
		dystans = (TextView) findViewById(R.id.dystans);
		cel = (TextView) findViewById(R.id.cel);
		textviewPitch = (TextView) findViewById(R.id.textView5);
		textviewRoll = (TextView) findViewById(R.id.textView6);
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MyLocationListener();

		lm.requestLocationUpdates(

		LocationManager.GPS_PROVIDER,

		0,

		0,
																							//BackHomeRadar
		locationListener);
		myCompassView = (MyWayView) findViewById(R.id.mycompassview);

		mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> mySensors = mySensorManager
				.getSensorList(Sensor.TYPE_ORIENTATION);

		if (mySensors.size() > 0) {
			mySensorManager.registerListener(mySensorEventListener,
					mySensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);
			sersorrunning = true;
			Toast.makeText(this, "Uruchomienie czujnika orientacji",
					Toast.LENGTH_LONG).show();

		} else {
			Toast.makeText(this, "Brak czujnika orientacji", Toast.LENGTH_LONG)
					.show();
			sersorrunning = false;
			finish();
		}
//		Toast.makeText(
//				this,
//				"Naciśnij START aby zapamiętać współrzędne miejsca, z którego wyruszasz.",
//				Toast.LENGTH_LONG).show();
//		Toast.makeText(this, "Naciśnij STOP aby odnaleźć drogę powrotną.",
//				Toast.LENGTH_LONG).show();
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
		case 1:
			Intent intent1 = new Intent(getApplicationContext(),
					Compass.class);
			startActivity(intent1);
			break;
		case 2:
			Intent intent2 = new Intent(getApplicationContext(),
				Gps2sms.class);
			startActivity(intent2);
			break;
		case 3:
			System.exit(0);
			break;
		default:
			break;
		}
		return false;
	}
	
	public class MyLocationListener implements LocationListener {
		public float locALa = 0;
		public float locALo = 0;
		private float actualLa;
		private float actualLo;
		private double locBLa;
		private double locBLo;
		double x;
		double y;

		public void onLocationChanged(final Location loc) {
			if (loc != null) {

				actualLa = (float) loc.getLatitude();
				actualLo = ((float) loc.getLongitude());

				final Button Start = (Button) findViewById(R.id.start);
				Start.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						double a = loc.getLatitude();
						double b = loc.getLongitude();
						int zaokr = (int) Math.pow(10, 5);
						a *= zaokr;
						a = Math.round(a);
						a /= zaokr;

						b *= zaokr;
						b = Math.round(b);
						b /= zaokr;
						sz1.setText("" + a);
						dl1.setText("  " + b);
						pamiec1.setText("  Zapamiętano");
						locALa = (float) loc.getLatitude();
						locALo = ((float) loc.getLongitude());

					}

				});
				final Button Stop = (Button) findViewById(R.id.stop);
				Stop.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						double a = loc.getLatitude();
						double b = loc.getLongitude();
						int zaokr = (int) Math.pow(10, 5);
						a *= zaokr;
						a = Math.round(a);
						a /= zaokr;

						b *= zaokr;
						b = Math.round(b);
						b /= zaokr;

						sz2.setText("" + a);
						dl2.setText("  " + b);

						pamiec2.setText("  Zapamiętano");
						locBLa = ((float) loc.getLatitude());
						locBLo = ((float) loc.getLongitude());

						locA = new Location(ACCESSIBILITY_SERVICE);
						locB = new Location(ACCESSIBILITY_SERVICE);
						locA.setLatitude(locALa);
						locA.setLongitude(locALo);
						locB.setLatitude(locBLa);
						locB.setLongitude(locBLo);

						Global.bearing = locA.bearingTo(locB);

						distance = locA.distanceTo(locB);
						c = distance;
						// c *= zaokr;
						// c = Math.round(c);
						// c /= zaokr;
						x = locALa - actualLa;
						y = locALo - actualLo;
						// zielone
						if (c <= 15) {
							dystans.setText("" + c + " m");
							cel.setVisibility(View.GONE);
							// 1
							// if(x == 0 && y > 0){
							Global.wybor = 3;
							// }
						}
						// zolte
						if (c > 15 && c < 99) {
							dystans.setText("" + c + " m");
							cel.setVisibility(View.GONE);
							// 1
							// if(x == 0 && y > 0){
							Global.wybor = 2;
							// }
						}
						// czerwone
						if (c >= 999) {
							dystans.setText("" + c / 1000 + " km");
							cel.setVisibility(View.GONE);
							// 1
							// if(x == 0 && y > 0){
							Global.wybor = 1;
							// }
						}
						if (c <= 10) {
							cel.setText("  Jesteś u celu!:)");
							cel.setVisibility(View.VISIBLE);
							Global.wybor = 4;
						}
					}

				});
				if (this.locALa != 0.0 && this.locALo != 0.0
						&& this.locBLa != 0.0 && this.locBLo != 0.0) {
					locA = new Location(ACCESSIBILITY_SERVICE);
					locB = new Location(ACCESSIBILITY_SERVICE);
					locA.setLatitude(this.locALa);
					locA.setLongitude(this.locALo);
					locB.setLatitude(loc.getLatitude());
					locB.setLongitude(loc.getLongitude());
					distance = locA.distanceTo(locB);
					// int zaokr1 = (int) Math.pow(10, 2);
					c = distance;
					// c *= zaokr1;
					// c = Math.round(c);
					// c /= zaokr1;
					if (c <= 15) {
						dystans.setText("" + c + " m");
						cel.setVisibility(View.GONE);
						// 1
						// if(x == 0 && y > 0){
						Global.wybor = 3;
						// }
					}
					if (c > 15 && c < 99) {
						dystans.setText("" + c + " m");
						cel.setVisibility(View.GONE);
						// 1
						// if(x == 0 && y > 0){
						Global.wybor = 2;
						// }
					}
					if (c >= 999) {
						dystans.setText("" + c / 1000 + " km");
						cel.setVisibility(View.GONE);
						// 1
						// if(x == 0 && y > 0){
						Global.wybor = 1;
						// }
					}
					if (c <= 10) {
						cel.setText("  Jesteś u celu!:)");
						cel.setVisibility(View.VISIBLE);
						Global.wybor = 4;
					}
				}

			}

		}

		public void onProviderDisabled(String provider) {

			// TODO Auto-generated method stub
		}

		public void onProviderEnabled(String provider) {

			// TODO Auto-generated method stub
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {

			// TODO Auto-generated method stub
		}

	}

	private SensorEventListener mySensorEventListener = new SensorEventListener() {

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			myCompassView.updateDirection((float) event.values[0]);
			textviewPitch.setText("X: " + String.valueOf(event.values[1])
					+ "  ");
			textviewRoll.setText("Y: " + String.valueOf(event.values[2]));
			Global.angel = (float) event.values[0];
		}
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (sersorrunning) {
			mySensorManager.unregisterListener(mySensorEventListener);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub

	}

}