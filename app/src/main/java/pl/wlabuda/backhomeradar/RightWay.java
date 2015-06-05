package pl.wlabuda.backhomeradar;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RightWay extends Activity implements SensorEventListener{

    private static SensorManager mySensorManager;
    private boolean sersorrunning;
	private MyWayView myCompassView;
	private TextView dystans;
    private TextView waiting;
    private TextView longitude;
    private TextView latitude;
	private LocationManager lm;
	private LocationListener locationListener;
	private Location locB;
	private Location locA;
	private float distance = 0;
	private double c = -1;
	final Context context = this;
    private boolean doubleBackToExitPressedOnce = false;
    private Button Start;
    private Button Stop;
    private Button Reset;
    private Boolean zmienna = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainkompass);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActionBar actionBar = getActionBar();
        //actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);

		dystans = (TextView) findViewById(R.id.dystans);
        waiting = (TextView) findViewById(R.id.waiting);
        longitude = (TextView) findViewById(R.id.longitude);
        latitude = (TextView) findViewById(R.id.latitude);
        Start = (Button) findViewById(R.id.start);
        Stop = (Button) findViewById(R.id.stop);
        Reset = (Button) findViewById(R.id.reset);

        Stop.setEnabled(false);
        Start.setEnabled(false);
        Reset.setEnabled(false);
        Start.setTextColor(Color.GRAY);
        Stop.setTextColor(Color.GRAY);
        Reset.setTextColor(Color.GRAY);

        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ){
            buildAlertMessageNoGps();
        }

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MyLocationListener();

		lm.requestLocationUpdates(

		LocationManager.GPS_PROVIDER,

		0,

		0,

		locationListener);
		myCompassView = (MyWayView) findViewById(R.id.mycompassview);

        mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> mySensors = mySensorManager
                .getSensorList(Sensor.TYPE_ORIENTATION);

        if (mySensors.size() > 0) {
            mySensorManager.registerListener(mySensorEventListener,
                    mySensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);
            sersorrunning = true;
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

        Reset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Start.setEnabled(true);
                Reset.setEnabled(false);
                zmienna = false;
                Global.wybor = 5;
                Start.setTextColor(Color.GREEN);
                Reset.setTextColor(Color.WHITE);
            }
        });
	}

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.gpsDialog))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
	  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, getString(R.string.about));
        menu.add(0, 1, 0, getString(R.string.finish));
        return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
			case 0:
				AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialogCustom);
				builder
						.setTitle(R.string.about)
						.setMessage("Wacław Łabuda \ne-mail: waclab1807@gmail.com \nPolska/Nowy Sącz")
						.setIcon(R.drawable.author)
						.setPositiveButton("OK", null)
						.show();
				break;
			case 1:
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
		private double locBLa;
		private double locBLo;
        int zaokr = (int) Math.pow(10, 5);;

		public void onLocationChanged(final Location loc) {
			if (loc != null) {
                double a = loc.getLatitude();
                double b = loc.getLongitude();
                a *= zaokr;
                a = Math.round(a);
                a /= zaokr;

                b *= zaokr;
                b = Math.round(b);
                b /= zaokr;

                longitude.setText(b + "");
                latitude.setText(a + "");
                if (!zmienna){
                    Start.setEnabled(true);
                    Start.setTextColor(Color.GREEN);
                }
                waiting.setText("");

				Start.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
                        Stop.setEnabled(true);
                        Stop.setTextColor(Color.GREEN);
                        Start.setEnabled(false);
                        Start.setTextColor(Color.WHITE);
                        zmienna = true;
						locALa = (float) loc.getLatitude();
						locALo = ((float) loc.getLongitude());

					}

				});
				Stop.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
                        Stop.setEnabled(false);
                        Stop.setTextColor(Color.WHITE);
                        Reset.setTextColor(Color.RED);
                        Reset.setEnabled(true);
                        zmienna = true;

						locBLa = ((float) loc.getLatitude());
						locBLo = ((float) loc.getLongitude());

						locA = new Location(ACCESSIBILITY_SERVICE);
						locB = new Location(ACCESSIBILITY_SERVICE);
						locA.setLatitude(locALa);
						locA.setLongitude(locALo);
						locB.setLatitude(locBLa);
						locB.setLongitude(locBLo);

                        //Global.bearing = locA.bearingTo(locB);
                        //System.out.println("**********************1 "+Global.bearing);

						distance = locA.distanceTo(locB);
                        //System.out.println("**********************2 "+distance);
                        c = distance;
						c *= zaokr;
						c = Math.round(c);
						c /= zaokr;
						    //x = locALa - actualLa;
						    //y = locALo - actualLo;
						// zielone
						if (c <= 15) {
                            dystans.setTextColor(Color.GREEN);
							dystans.setText("" + c + " m");
							// 1
							// if(x == 0 && y > 0){
							Global.wybor = 3;
							// }
						}
						// zolte
						if (c > 15 && c < 99) {
                            dystans.setTextColor(Color.YELLOW);
							dystans.setText("" + c + " m");
							// 1
							// if(x == 0 && y > 0){
							Global.wybor = 2;
							// }
						}
						// czerwone
						if (c >= 999) {
                            dystans.setTextColor(Color.RED);
							dystans.setText("" + c / 1000 + " km");
							// 1
							// if(x == 0 && y > 0){
							Global.wybor = 1;
							// }
						}
						if (c <= 3) {
							Global.wybor = 4;
						}
					}
				});
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

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.backButton), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 1000);
    }

}