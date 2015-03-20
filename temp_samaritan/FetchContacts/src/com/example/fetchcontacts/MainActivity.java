package com.example.fetchcontacts;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ListView;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class MainActivity extends FragmentActivity implements LoaderCallbacks<Cursor> {

	TelephonyManager telephony;
	private static final String[] PROJECTION = { Data._ID, Data.MIMETYPE, Data.DATA1, Data.DATA2, Data.DATA3,
			Data.DATA4, Data.DATA5, Data.DATA6, Data.DATA7, Data.DATA8, Data.DATA9, Data.DATA10, Data.DATA11,
			Data.DATA12, Data.DATA13, Data.DATA14, Data.DATA15 };
	private static final String[] PROJECTION2 = { Email._ID, Email.ADDRESS, Email.TYPE, Email.LABEL };
	private static final String[] PROJECTION3 = { Phone.PHOTO_URI, Phone.DATA1, Phone.DISPLAY_NAME

	};
	private ListView listView;
	ArrayList<Contact> arrayList2 = new ArrayList<Contact>();
	HashMap<String, String> hashmap_isocodes = new HashMap<String, String>();
	private SuperAdapter adapter2;
	public String latlong;
	private PhoneNumberUtil numberUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getSupportLoaderManager().initLoader(1, null, this);
		telephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		numberUtil = PhoneNumberUtil.getInstance();
		Log.d("getSimCountryIso", "" + telephony.getSimCountryIso());
		Log.d("getSimOperator", "" + telephony.getSimOperator());
		Log.d("getSimSerialNumber", "" + telephony.getSimSerialNumber());
		Log.d("getSimState", "" + telephony.getSimState());
		Log.d("getSimOperatorName", "" + telephony.getSimOperatorName());
		Log.d("getLine1Number", "" + telephony.getLine1Number());
		Log.d("getNetworkCountryIso", "" + telephony.getNetworkCountryIso());
		Log.d("getNetworkOperator", "" + telephony.getNetworkOperator());
		Log.d("getNetworkOperatorName", "" + telephony.getNetworkOperatorName());
		listView = (ListView) findViewById(R.id.listView1);

		try {
			new asyncs().execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setupIsoData();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		CursorLoader cursorLoader = new CursorLoader(MainActivity.this, // Parent
				// activity
				// context
				Phone.CONTENT_URI, // Table to query
				PROJECTION3, // Projection to return
				null, // No selection clause
				null, // No selection arguments
				"UPPER(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " )ASC");
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		// TODO Auto-generated method stub
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					String[] s = cursor.getColumnNames();
					String ss = "";
					Contact contact = new Contact();
					for (int i = 0; i < s.length; i++) {

						Log.d("" + s[i], "" + cursor.getString(cursor.getColumnIndex(s[i])));
						;
						ss = cursor.getString(cursor.getColumnIndex(s[i])) + "\n" + ss;

						switch (i) {
						case 0:
							contact.setContact_pic_uri(cursor.getString(cursor.getColumnIndex(s[i])));
							break;
						case 1:
							contact.setContact_number(formatPhoneNumbers(cursor.getString(cursor.getColumnIndex(s[i]))));

							contact.setLocation(latlong);
							break;
						case 2:
							contact.setContact_name(cursor.getString(cursor.getColumnIndex(s[i])));
							break;

						default:
							break;
						}

					}
					if (!contact.getContact_number().equals("")) {
						arrayList2.add(contact);
					}

				} while (cursor.moveToNext());

			}
		}
		adapter2.notifyDataSetChanged();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub

	}

	class asyncs extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Boolean result = false;

			String urlmmap = "http://www.google.com/glm/mmap";

			try {
				URL url = new URL(urlmmap);
				URLConnection conn = url.openConnection();
				HttpURLConnection httpConn = (HttpURLConnection) conn;
				httpConn.setRequestMethod("POST");
				httpConn.setDoOutput(true);
				httpConn.setDoInput(true);
				httpConn.connect();

				OutputStream outputStream = httpConn.getOutputStream();
				String s = "" + telephony.getCellLocation();

				s = s.replace("[", "");
				s = s.replace("]", "");

				String temp[] = s.split(",");
				if (temp.length > 0 && !s.equalsIgnoreCase("null")) {
					WriteData(outputStream, Integer.parseInt(temp[1]), Integer.parseInt(temp[0]));

					InputStream inputStream = httpConn.getInputStream();
					DataInputStream dataInputStream = new DataInputStream(inputStream);

					dataInputStream.readShort();
					dataInputStream.readByte();
					int code = dataInputStream.readInt();
					if (code == 0) {

						float myLatitude = dataInputStream.readInt();

						float myLongitude = dataInputStream.readInt();

						int lat = (int) (myLatitude * 1E6 / 1E6);
						int lng = (int) (myLongitude * 1E6 / 1E6);
						double lat1d = (double) lat / 1e6;
						double long1d = (double) lng / 1e6;
						latlong = "lat-long" + lat1d + "-" + long1d;
						result = true;

					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

	}

	private void setupIsoData() {
		// TODO Auto-generated method stub
		String[] arr = getResources().getStringArray(R.array.callingcodes);
		String isocodes = "";
		String[] splitediso = null;
		for (int i = 0; i < arr.length; i++) {
			isocodes = arr[i].replace(" ", "");
			splitediso = isocodes.split(",");
			if (splitediso.length == 3) {
				hashmap_isocodes.put(splitediso[2], splitediso[1]);
			}

		}
		adapter2 = new SuperAdapter(MainActivity.this, arrayList2);
		listView.setAdapter(adapter2);

	}

	private String formatPhoneNumbers(String contactnumber) {
		try {

			contactnumber = PhoneNumberUtils.formatNumber(contactnumber);
			PhoneNumber numbPhone = numberUtil.parse(contactnumber, telephony.getNetworkCountryIso());
			if (numberUtil.isValidNumber(numbPhone)
					&& numberUtil.getNumberType(numbPhone).equals(PhoneNumberUtil.PhoneNumberType.MOBILE)) {
				contactnumber = numberUtil.format(numbPhone, PhoneNumberFormat.INTERNATIONAL);
				return contactnumber;
			}

			else {
				return "";
			}

		} catch (NumberParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			try {

				contactnumber = "+" + hashmap_isocodes.get(telephony.getNetworkCountryIso().toUpperCase()) + ""
						+ contactnumber;
				PhoneNumber numbPhone;
				numbPhone = numberUtil.parse(contactnumber, telephony.getNetworkCountryIso());
				if (numberUtil.isValidNumber(numbPhone)
						&& numberUtil.getNumberType(numbPhone).equals(PhoneNumberUtil.PhoneNumberType.MOBILE)) {
					contactnumber = numberUtil.format(numbPhone, PhoneNumberFormat.INTERNATIONAL);
					return contactnumber;
				} else {
					return "";
				}

			} catch (NumberParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		// TODO Auto-generated method stub
		return "";
	}

	private void WriteData(OutputStream out, int cid, int lac) throws IOException {
		DataOutputStream dataOutputStream = new DataOutputStream(out);
		dataOutputStream.writeShort(21);
		dataOutputStream.writeLong(0);
		dataOutputStream.writeUTF("en");
		dataOutputStream.writeUTF("Android");
		dataOutputStream.writeUTF("1.0");
		dataOutputStream.writeUTF("Web");
		dataOutputStream.writeByte(27);
		dataOutputStream.writeInt(0);
		dataOutputStream.writeInt(0);
		dataOutputStream.writeInt(3);
		dataOutputStream.writeUTF("");

		dataOutputStream.writeInt(cid);
		dataOutputStream.writeInt(lac);

		dataOutputStream.writeInt(0);
		dataOutputStream.writeInt(0);
		dataOutputStream.writeInt(0);
		dataOutputStream.writeInt(0);
		dataOutputStream.flush();
	}
}
