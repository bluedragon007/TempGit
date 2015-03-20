package com.example.systemdialogs;

import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private AlertDialog.Builder alertDialogBuilder;
	private WindowManager manager;
	private TimerTask task;
	private CountDownTimer countDownTimer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		alertDialogBuilder = new AlertDialog.Builder(getApplicationContext());
		alertDialogBuilder.setTitle(this.getTitle() + " decision");

		alertDialogBuilder.setMessage("Are you sure?");

		// set positive button: Yes message

		alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {

				// go to a new activity of the app

			}

		});

		// set negative button: No message

		alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {

				// cancel the alert box and put a Toast to the user

				dialog.cancel();

				Toast.makeText(getApplicationContext(), "You chose a negative answer",

				Toast.LENGTH_LONG).show();

			}

		});

		// set neutral button: Exit the app message

		alertDialogBuilder.setNeutralButton("Exit the app", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {

				// exit the app and go to the HOME

				MainActivity.this.finish();

			}

		});
		countDownTimer = new CountDownTimer(1000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				loaddialog();
				Log.d("", "Lock"+millisUntilFinished);
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub

			}
		};
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// for (int i = 0; i < 10; i++) {
		// // AlertDialog dialog = alertDialogBuilder.create();
		// //
		// dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		// // dialog.show();
		//
		// loaddialog();
		// }
		countDownTimer.start();

	}

	private void loaddialog() {
		// TODO Auto-generated method stub
		manager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
		layoutParams.gravity = Gravity.CENTER;
		layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		layoutParams.alpha = 1.0f;
		layoutParams.packageName = getPackageName();
		layoutParams.buttonBrightness = 1f;
		layoutParams.windowAnimations = android.R.style.Animation_Dialog;

		final View view = View.inflate(getApplicationContext(), R.layout.activity_main, null);
		Button yesButton = (Button) view.findViewById(R.id.yesButton);
		Button noButton = (Button) view.findViewById(R.id.noButton);
		yesButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				manager.removeView(view);
			}
		});
		noButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				manager.removeView(view);
			}
		});
		manager.addView(view, layoutParams);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

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
}
