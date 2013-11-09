package de.hs_osnabrueck.asta.sensorlog;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.CompoundButton;
import android.text.Html;
import android.os.Environment;
import android.util.Log;
import android.media.MediaScannerConnection;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.Locale;

public class SensorLog extends Activity implements SensorEventListener, CompoundButton.OnCheckedChangeListener {
	private SensorManager sm;
	private Sensor ss[] = new Sensor[12];
	private boolean on = false;
	File esDir, slDir;
	File[] file = new File[12];
	BufferedWriter[] out = new BufferedWriter[12];

	private void write(int s, long t, float[] v) {
		try {
			switch (s) {
				case Sensor.TYPE_ACCELEROMETER:
					out[0].write(String.format(Locale.US, "%d,%f,%f,%f\n", t, v[0], v[1], v[2]));
					break;
				case Sensor.TYPE_GRAVITY:
					out[1].write(String.format(Locale.US, "%d,%f,%f,%f\n", t, v[0], v[1], v[2]));
					break;
				case Sensor.TYPE_GYROSCOPE:
					out[2].write(String.format(Locale.US, "%d,%f,%f,%f\n", t, v[0], v[1], v[2]));
					break;
				case Sensor.TYPE_LINEAR_ACCELERATION:
					out[3].write(String.format(Locale.US, "%d,%f,%f,%f\n", t, v[0], v[1], v[2]));
					break;
				case Sensor.TYPE_ROTATION_VECTOR:
					out[4].write(String.format(Locale.US, "%d,%f,%f,%f\n", t, v[0], v[1], v[2]));
					break;
				case Sensor.TYPE_MAGNETIC_FIELD:
					out[5].write(String.format(Locale.US, "%d,%f,%f,%f\n", t, v[0], v[1], v[2]));
					break;
				case Sensor.TYPE_ORIENTATION:
					out[6].write(String.format(Locale.US, "%d,%f,%f,%f\n", t, v[0], v[1], v[2]));
					break;
				case Sensor.TYPE_PROXIMITY:
					out[7].write(String.format(Locale.US, "%d,%f\n", t, v[0]));
					break;
				case Sensor.TYPE_AMBIENT_TEMPERATURE:
					out[8].write(String.format(Locale.US, "%d,%f\n", t, v[0]));
					break;
				case Sensor.TYPE_LIGHT:
					out[9].write(String.format(Locale.US, "%d,%f\n", t, v[0]));
					break;
				case Sensor.TYPE_PRESSURE:
					out[10].write(String.format(Locale.US, "%d,%f\n", t, v[0]));
					break;
				case Sensor.TYPE_RELATIVE_HUMIDITY:
					out[11].write(String.format(Locale.US, "%d,%f\n", t, v[0]));
					break;
			}
		} catch (Exception e) {
			Log.e("SensorLog", e.toString());
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor_log);
		((CompoundButton)findViewById(R.id.logSwitch)).setOnCheckedChangeListener(this);

		esDir = Environment.getExternalStorageDirectory();
		slDir = new File(esDir, "SensorLog");
		slDir.mkdir();
		file[0] = new File(slDir, "accelerometer.csv");
		file[1] = new File(slDir, "gravity.csv");
		file[2] = new File(slDir, "gyroscope.csv");
		file[3] = new File(slDir, "linear_acceleration.csv");
		file[4] = new File(slDir, "rotation_vector.csv");
		file[5] = new File(slDir, "magnetic_field.csv");
		file[6] = new File(slDir, "orientation.csv");
		file[7] = new File(slDir, "proximity.csv");
		file[8] = new File(slDir, "ambient_temparature.csv");
		file[9] = new File(slDir, "illuminance.csv");
		file[10] = new File(slDir, "pressure.csv");
		file[11] = new File(slDir, "relative_humidity.csv");

		try {
			out[0] = new BufferedWriter(new FileWriter(file[0], true));
			out[1] = new BufferedWriter(new FileWriter(file[1], true));
			out[2] = new BufferedWriter(new FileWriter(file[2], true));
			out[3] = new BufferedWriter(new FileWriter(file[3], true));
			out[4] = new BufferedWriter(new FileWriter(file[4], true));
			out[5] = new BufferedWriter(new FileWriter(file[5], true));
			out[6] = new BufferedWriter(new FileWriter(file[6], true));
			out[7] = new BufferedWriter(new FileWriter(file[7], true));
			out[8] = new BufferedWriter(new FileWriter(file[8], true));
			out[9] = new BufferedWriter(new FileWriter(file[9], true));
			out[10] = new BufferedWriter(new FileWriter(file[10], true));
			out[11] = new BufferedWriter(new FileWriter(file[11], true));
		} catch (Exception e) {
			Log.e("SensorLog", e.toString());
		}

		sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		ss[0] = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		ss[1] = sm.getDefaultSensor(Sensor.TYPE_GRAVITY);
		ss[2] = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		ss[3] = sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		ss[4] = sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		ss[5] = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		ss[6] = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		ss[7] = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		ss[8] = sm.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
		ss[9] = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
		ss[10] = sm.getDefaultSensor(Sensor.TYPE_PRESSURE);
		ss[11] = sm.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.activity_sensor_log, menu);
		return true;
	}

	@Override
	public void onResume() {
		super.onResume();

		sm.registerListener(this, ss[0], SensorManager.SENSOR_DELAY_NORMAL);
		sm.registerListener(this, ss[1], SensorManager.SENSOR_DELAY_NORMAL);
		sm.registerListener(this, ss[2], SensorManager.SENSOR_DELAY_NORMAL);
		sm.registerListener(this, ss[3], SensorManager.SENSOR_DELAY_NORMAL);
		sm.registerListener(this, ss[4], SensorManager.SENSOR_DELAY_NORMAL);
		sm.registerListener(this, ss[5], SensorManager.SENSOR_DELAY_NORMAL);
		sm.registerListener(this, ss[6], SensorManager.SENSOR_DELAY_NORMAL);
		sm.registerListener(this, ss[7], SensorManager.SENSOR_DELAY_NORMAL);
		sm.registerListener(this, ss[8], SensorManager.SENSOR_DELAY_NORMAL);
		sm.registerListener(this, ss[9], SensorManager.SENSOR_DELAY_NORMAL);
		sm.registerListener(this, ss[10], SensorManager.SENSOR_DELAY_NORMAL);
		sm.registerListener(this, ss[11], SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public void onPause() {
		super.onPause();

		sm.unregisterListener(this);

		try {
			out[0].flush();
			out[1].flush();
			out[2].flush();
			out[3].flush();
			out[4].flush();
			out[5].flush();
			out[6].flush();
			out[7].flush();
			out[8].flush();
			out[9].flush();
			out[10].flush();
			out[11].flush();
			MediaScannerConnection.scanFile(getApplicationContext(), new String[] {
				slDir.getAbsolutePath(),
				file[0].getAbsolutePath(),
				file[1].getAbsolutePath(),
				file[2].getAbsolutePath(),
				file[3].getAbsolutePath(),
				file[4].getAbsolutePath(),
				file[5].getAbsolutePath(),
				file[6].getAbsolutePath(),
				file[7].getAbsolutePath(),
				file[8].getAbsolutePath(),
				file[9].getAbsolutePath(),
				file[10].getAbsolutePath(),
				file[11].getAbsolutePath()
			}, null, null);
		} catch (Exception e) {
			Log.e("SensorLog", e.toString());
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		try {
			out[0].close();
			out[1].close();
			out[2].close();
			out[3].close();
			out[4].close();
			out[5].close();
			out[6].close();
			out[7].close();
			out[8].close();
			out[9].close();
			out[10].close();
			out[11].close();
			MediaScannerConnection.scanFile(getApplicationContext(), new String[] {
				slDir.getAbsolutePath(),
				file[0].getAbsolutePath(),
				file[1].getAbsolutePath(),
				file[2].getAbsolutePath(),
				file[3].getAbsolutePath(),
				file[4].getAbsolutePath(),
				file[5].getAbsolutePath(),
				file[6].getAbsolutePath(),
				file[7].getAbsolutePath(),
				file[8].getAbsolutePath(),
				file[9].getAbsolutePath(),
				file[10].getAbsolutePath(),
				file[11].getAbsolutePath()
			}, null, null);
		} catch (Exception e) {
			Log.e("SensorLog", e.toString());
		}
	}

	public void onAccuracyChanged(Sensor s, int a) {
	}

	public void onSensorChanged(SensorEvent se) {
		SensorTask as = new SensorTask();
		as.execute(se);
	}

	public void onCheckedChanged(CompoundButton b, boolean c) {
		on = c;
	}

	private class SensorTask extends AsyncTask<SensorEvent, Void, SensorEvent> {
		protected SensorEvent doInBackground(SensorEvent... se) {
			if (on && Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				write(se[0].sensor.getType(), se[0].timestamp, se[0].values);
			}
			return se[0];
		}

		protected void onPostExecute(SensorEvent se) {
			TextView tv;

			switch (se.sensor.getType()) {
				case Sensor.TYPE_ACCELEROMETER:
					tv = (TextView)findViewById(R.id.accel_data);
					tv.setText(Html.fromHtml(String.format(getString(R.string.accel_format), se.values[0], se.values[1], se.values[2], Math.sqrt(Math.pow(se.values[0], 2) + Math.pow(se.values[1], 2) + Math.pow(se.values[2], 2)))));
					break;
				case Sensor.TYPE_GRAVITY:
					tv = (TextView)findViewById(R.id.gravity_data);
					tv.setText(Html.fromHtml(String.format(getString(R.string.gravity_format), se.values[0], se.values[1], se.values[2], Math.sqrt(Math.pow(se.values[0], 2) + Math.pow(se.values[1], 2) + Math.pow(se.values[2], 2)))));
					break;
				case Sensor.TYPE_GYROSCOPE:
					tv = (TextView)findViewById(R.id.gyro_data);
					tv.setText(Html.fromHtml(String.format(getString(R.string.gyro_format), se.values[0], se.values[1], se.values[2])));
					break;
				case Sensor.TYPE_LINEAR_ACCELERATION:
					tv = (TextView)findViewById(R.id.linaccel_data);
					tv.setText(Html.fromHtml(String.format(getString(R.string.linaccel_format), se.values[0], se.values[1], se.values[2], Math.sqrt(Math.pow(se.values[0], 2) + Math.pow(se.values[1], 2) + Math.pow(se.values[2], 2)))));
					break;
				case Sensor.TYPE_ROTATION_VECTOR:
					tv = (TextView)findViewById(R.id.rotvect_data);
					tv.setText(Html.fromHtml(String.format(getString(R.string.rotvect_format), se.values[0], se.values[1], se.values[2], Math.sqrt(Math.pow(se.values[0], 2) + Math.pow(se.values[1], 2) + Math.pow(se.values[2], 2)))));
					break;
				case Sensor.TYPE_MAGNETIC_FIELD:
					tv = (TextView)findViewById(R.id.magfield_data);
					tv.setText(Html.fromHtml(String.format(getString(R.string.magfield_format), se.values[0], se.values[1], se.values[2], Math.sqrt(Math.pow(se.values[0], 2) + Math.pow(se.values[1], 2) + Math.pow(se.values[2], 2)))));
					break;
				case Sensor.TYPE_ORIENTATION:
					tv = (TextView)findViewById(R.id.ori_data);
					tv.setText(Html.fromHtml(String.format(getString(R.string.ori_format), se.values[1], se.values[2], se.values[0])));
					break;
				case Sensor.TYPE_PROXIMITY:
					tv = (TextView)findViewById(R.id.proxy_data);
					tv.setText(Html.fromHtml(String.format(getString(R.string.proxy_format), se.values[0] * 10f)));
					break;
				case Sensor.TYPE_AMBIENT_TEMPERATURE:
					tv = (TextView)findViewById(R.id.ambitemp_data);
					tv.setText(Html.fromHtml(String.format(getString(R.string.ambitemp_format), se.values[0] + 273.15f)));
					break;
				case Sensor.TYPE_LIGHT:
					tv = (TextView)findViewById(R.id.light_data);
					tv.setText(Html.fromHtml(String.format(getString(R.string.light_format), se.values[0])));
					break;
				case Sensor.TYPE_PRESSURE:
					tv = (TextView)findViewById(R.id.press_data);
					tv.setText(Html.fromHtml(String.format(getString(R.string.press_format), se.values[0])));
					break;
				case Sensor.TYPE_RELATIVE_HUMIDITY:
					tv = (TextView)findViewById(R.id.humi_data);
					tv.setText(Html.fromHtml(String.format(getString(R.string.humi_format), se.values[0])));
					break;
			}
		}
	}
}
