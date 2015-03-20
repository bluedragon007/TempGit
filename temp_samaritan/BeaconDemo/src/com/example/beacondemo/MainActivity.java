package com.example.beacondemo;

import java.util.ArrayList;
import java.util.Collection;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.radiusnetworks.ibeacon.IBeacon;
import com.radiusnetworks.ibeacon.IBeaconConsumer;
import com.radiusnetworks.ibeacon.IBeaconManager;
import com.radiusnetworks.ibeacon.MonitorNotifier;
import com.radiusnetworks.ibeacon.RangeNotifier;
import com.radiusnetworks.ibeacon.Region;

public class MainActivity extends ActionBarActivity implements IBeaconConsumer {
	private IBeaconManager iBeaconManager = IBeaconManager.getInstanceForApplication(this);

	private BeaconServiceUtility beaconUtill = null;

	protected boolean deviceDetected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		beaconUtill = new BeaconServiceUtility(this);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		verifyBluetooth();

	}

	public void startRanging(View view) {
		beaconUtill.onStart(iBeaconManager, this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		beaconUtill.onStop(iBeaconManager, this);
		super.onStop();

	}

	private boolean verifyBluetooth() {

		try {
			// returns false if it is suppo
			if (!IBeaconManager.getInstanceForApplication(this).checkAvailability()) {

				final AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Bluetooth not enabled");
				builder.setMessage("Please enable bluetooth in settings and restart this application.");
				builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						startActivityForResult(new Intent("android.settings.BLUETOOTH_SETTINGS"), 11);
						dialog.cancel();
					}
				});
				builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
				builder.show();
				return false;
			} else {

			}
		} catch (RuntimeException e) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Bluetooth LE not available");
			builder.setMessage("Sorry, this device does not support Bluetooth LE.");
			builder.setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			});
			builder.show();
			return false;

		}
		return false;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	double getDistance(int rssi, int txPower) {
		/*
		 * RSSI = TxPower - 10 * n * lg(d) n = 2 (in free space)
		 * 
		 * d = 10 ^ ((TxPower - RSSI) / (10 * n))
		 */

		return Math.pow(10d, ((double) txPower - rssi) / (10 * 2));
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		if (arg0 == 11 && arg1 == RESULT_OK) {

		} else if (arg0 == 11 && arg1 == RESULT_CANCELED) {
			verifyBluetooth();
		}
		super.onActivityResult(arg0, arg1, arg2);
	}

	@Override
	public void onIBeaconServiceConnect() {
		// TODO Auto-generated method stub
		iBeaconManager.setRangeNotifier(new RangeNotifier() {
			@Override
			public void didRangeBeaconsInRegion(Collection<IBeacon> iBeacons, Region region) {

				ArrayList<IBeacon> iBeacons2 = new ArrayList<IBeacon>();
				iBeacons2.clear();
				iBeacons2.addAll(iBeacons);
				for (int i = 0; i < iBeacons2.size(); i++) {

					if (iBeacons2.get(i).getProximity() == IBeacon.PROXIMITY_IMMEDIATE) {
						generateNotification(
								MainActivity.this,
								"Device Found at Distance"
										+ getDistance(iBeacons2.get(i).getRssi(), iBeacons2.get(i).getTxPower()));
					}
				}
			}

		});
		iBeaconManager.setMonitorNotifier(new MonitorNotifier() {
			@Override
			public void didEnterRegion(Region region) {
				Log.e("BeaconDetactorService", "didEnterRegion");

				generateNotification(MainActivity.this, "didEnterRegion");

				// logStatus("I just saw an iBeacon for the first time!");
			}

			@Override
			public void didExitRegion(Region region) {
				Log.e("BeaconDetactorService", "didExitRegion");
				// logStatus("I no longer see an iBeacon");
				generateNotification(MainActivity.this, "didExitRegion");
			}

			@Override
			public void didDetermineStateForRegion(int state, Region region) {
				Log.e("BeaconDetactorService", "didDetermineStateForRegion");
				// logStatus("I have just switched from seeing/not seeing iBeacons: "
				// + state);
				generateNotification(MainActivity.this, "didDetermineStateForRegion");
			}

		});

		try {
			iBeaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId",
					"E2C56DB5-DFFB-48D2-B060-D0F5A71096E2", null, null));
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		try {
			iBeaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId",
					"E2C56DB5-DFFB-48D2-B060-D0F5A71096E2", null, null));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void generateNotification(Context context, String message) {

		Intent launchIntent = new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);

		((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(
				0,
				new NotificationCompat.Builder(context).setWhen(System.currentTimeMillis())
						.setSmallIcon(R.drawable.ic_launcher).setTicker(message)
						.setContentTitle(context.getString(R.string.app_name)).setContentText(message)
						.setContentIntent(PendingIntent.getActivity(context, 0, launchIntent, 0)).setAutoCancel(true)
						.build());

	}

}
