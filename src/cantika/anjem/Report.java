package cantika.anjem;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import cantika.anjem.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

public class Report extends Activity {
	File myExternalFileI;
	File myExternalFileO;
	File myExternalDirI;
	File myExternalDirO;
	File outlayFiles[];
	File incomeFiles[];
	SimpleDateFormat sdfMonth = new SimpleDateFormat("MMMM yyyy", Locale.US);
	String newmonth;
	int yr, month, day;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report);
		Calendar hsl = Calendar.getInstance();
		Intent i = this.getIntent();
		yr = i.getIntExtra("yr", 0);
		month = i.getIntExtra("month", 0);
		day = i.getIntExtra("day", 0);
		hsl.set(yr, month, day);
		java.util.Date d = new java.util.Date(hsl.getTimeInMillis());
		newmonth = sdfMonth.format(d);
		TextView txtReport = (TextView) findViewById(R.id.txt_report);
		txtReport.setText(i.getStringExtra("pesan"));
		TextView txtDB = (TextView) findViewById(R.id.txt_DB_R);
		txtDB.setText("Database: " + newmonth);

		setTab();
		dataI();
		dataO();
	}

	public void setTab() {
		TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
		tabHost.setup();

		TabSpec spec1 = tabHost.newTabSpec("ITab");
		spec1.setContent(R.id.tab1);
		// spec1.setIndicator("Tab Satu");
		spec1.setIndicator("Income");

		TabSpec spec2 = tabHost.newTabSpec("OTab");
		spec2.setIndicator("Outlay");
		spec2.setContent(R.id.tab2);
		// spec2.setIndicator("Tab Kedua",getResources().getDrawable(R.drawable.icon));

		tabHost.addTab(spec1);
		tabHost.addTab(spec2);
	}

	public void dataI() {
		ListView listView = (ListView) findViewById(R.id.list1);

		if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
			finish();
		} else {
			myExternalDirI = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/.Application of Anjem/Income/" + newmonth);
			if (!myExternalDirI.exists()) {
				myExternalDirI.mkdirs();
			}
			incomeFiles = myExternalDirI.listFiles();
			ArrayList<String> results = new ArrayList<String>();

			for (int i = 0; i < incomeFiles.length; i++) {
				File file = incomeFiles[i];

				String isiFile = "";
				String eol = System.getProperty("line.separator");
				myExternalFileI = new File(myExternalDirI + "/"
						+ file.getName());
				try {
					FileInputStream fis = new FileInputStream(myExternalFileI);
					DataInputStream in = new DataInputStream(fis);
					BufferedReader br = new BufferedReader(
							new InputStreamReader(in));
					String strLine;
					while ((strLine = br.readLine()) != null) {
						isiFile = isiFile + strLine + eol;
					}
					in.close();
				} catch (IOException e) {
					Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT)
							.show();
				}
				results.add(isiFile);
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, android.R.id.text1,
					results);
			listView.setAdapter(adapter);
		}
	}

	public void dataO() {
		ListView listView = (ListView) findViewById(R.id.list2);

		if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
			finish();
		} else {
			myExternalDirO = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/.Application of Anjem/Outlay/" + newmonth);
			if (!myExternalDirO.exists()) {
				myExternalDirO.mkdirs();
			}
			outlayFiles = myExternalDirO.listFiles();
			ArrayList<String> results = new ArrayList<String>();

			for (int i = 0; i < outlayFiles.length; i++) {
				File file = outlayFiles[i];

				String isiFile = "";
				String eol = System.getProperty("line.separator");
				myExternalFileO = new File(myExternalDirO + "/"
						+ file.getName());
				try {
					FileInputStream fis = new FileInputStream(myExternalFileO);
					DataInputStream in = new DataInputStream(fis);
					BufferedReader br = new BufferedReader(
							new InputStreamReader(in));
					String strLine;
					while ((strLine = br.readLine()) != null) {
						isiFile = isiFile + strLine + eol;
					}
					in.close();
				} catch (IOException e) {
					Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT)
							.show();
				}
				results.add(isiFile);
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, android.R.id.text1,
					results);
			listView.setAdapter(adapter);
		}
	}

	private static boolean isExternalStorageReadOnly() {
		String extStorageState = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
			return true;
		}
		return false;
	}

	private static boolean isExternalStorageAvailable() {
		String extStorageState = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
			return true;
		}
		return false;
	}

}
