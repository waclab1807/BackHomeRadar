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

public class RightWay extends Activity {

	private MyWayView myCompassView;
	private TextView dystans;
	private TextView pamiec1;
	private TextView pamiec2;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainkompass);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActionBar actionBar = getActionBar();
        //actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);

		pamiec1 = (TextView) findViewById(R.id.pamiec1);
		pamiec2 = (TextView) findViewById(R.id.pamiec2);
		dystans = (TextView) findViewById(R.id.dystans);
        Start = (Button) findViewById(R.id.start);
        Stop = (Button) findViewById(R.id.stop);
        Reset = (Button) findViewById(R.id.reset);

        Stop.setEnabled(false);
        Start.setEnabled(false);
        Reset.setEnabled(false);

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

		Toast.makeText(
				this,
				"Naciśnij START aby zapamiętać współrzędne miejsca, z którego wyruszasz.",
				Toast.LENGTH_LONG).show();
		Toast.makeText(this, "Naciśnij STOP aby odnaleźć drogę powrotną.",
				Toast.LENGTH_LONG).show();
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
		private float actualLa;
		private float actualLo;
		private double locBLa;
		private double locBLo;
		double x;
		double y;

		public void onLocationChanged(final Location loc) {
			if (loc != null) {

                Start.setEnabled(true);
                Start.setText("Start");
                Start.setTextColor(Color.WHITE);
				actualLa = (float) loc.getLatitude();
				actualLo = ((float) loc.getLongitude());

				Start.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
                        Stop.setEnabled(true);
                        Start.setEnabled(false);
						double a = loc.getLatitude();
						double b = loc.getLongitude();
						int zaokr = (int) Math.pow(10, 5);
						a *= zaokr;
						a = Math.round(a);
						a /= zaokr;

						b *= zaokr;
						b = Math.round(b);
						b /= zaokr;
						pamiec1.setText(" Zapamiętano");
						locALa = (float) loc.getLatitude();
						locALo = ((float) loc.getLongitude());

					}

				});
				Stop.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
                        Stop.setEnabled(false);
                        Start.setEnabled(false);
						double a = loc.getLatitude();
						double b = loc.getLongitude();
						int zaokr = (int) Math.pow(10, 5);
						a *= zaokr;
						a = Math.round(a);
						a /= zaokr;

						b *= zaokr;
						b = Math.round(b);
						b /= zaokr;

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
							// 1
							// if(x == 0 && y > 0){
							Global.wybor = 3;
							// }
						}
						// zolte
						if (c > 15 && c < 99) {
							dystans.setText("" + c + " m");
							// 1
							// if(x == 0 && y > 0){
							Global.wybor = 2;
							// }
						}
						// czerwone
						if (c >= 999) {
							dystans.setText("" + c / 1000 + " km");
							// 1
							// if(x == 0 && y > 0){
							Global.wybor = 1;
							// }
						}
						if (c <= 5) {
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
						// 1
						// if(x == 0 && y > 0){
						Global.wybor = 3;
						// }
					}
					if (c > 15 && c < 99) {
						dystans.setText("" + c + " m");
						// 1
						// if(x == 0 && y > 0){
						Global.wybor = 2;
						// }
					}
					if (c >= 999) {
						dystans.setText("" + c / 1000 + " km");
						// 1
						// if(x == 0 && y > 0){
						Global.wybor = 1;
						// }
					}
					if (c <= 5) {
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


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
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