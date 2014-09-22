package cantika.anjem;

import android.os.Bundle;
import android.os.Environment;

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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.ListActivity;
import android.widget.ArrayAdapter;
import android.view.View;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Toast;

public class DataOutlay extends ListActivity {
	File myExternalFile;
	File myExternalDir;
	File outlayFiles[];
	SimpleDateFormat sdfMonth = new SimpleDateFormat("MMMM yyyy", Locale.US);
	String newmonth;
	int yr, month, day;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Calendar hsl = Calendar.getInstance();
		Intent i = this.getIntent();
		yr = i.getIntExtra("yr", 0);
		month = i.getIntExtra("month", 0);
		day = i.getIntExtra("day", 0);
		hsl.set(yr, month, day);
		java.util.Date d = new java.util.Date(hsl.getTimeInMillis());
		newmonth = sdfMonth.format(d);

		refreshData();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(cantika.anjem.R.menu.action, menu);
	}

	public void refreshData() {
		if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
			finish();
		} else {
			myExternalDir = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/.Application of Anjem/Outlay/" + newmonth);
			if (!myExternalDir.exists()) {
				myExternalDir.mkdirs();
			}
			outlayFiles = myExternalDir.listFiles();
			ArrayList<String> results = new ArrayList<String>();
			registerForContextMenu(getListView());

			for (int i = 0; i < outlayFiles.length; i++) {
				File file = outlayFiles[i];
				results.add(file.getName().substring(0,
						file.getName().length() - 5));
			}
			this.setListAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, results));
		}
	}

	public void searchData(String data) {
		if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
			finish();
		} else {
			myExternalDir = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/.Application of Anjem/Outlay/" + newmonth);
			if (!myExternalDir.exists()) {
				myExternalDir.mkdirs();
			}
			String isiFile = "";
			String eol = System.getProperty("line.separator");
			myExternalFile = new File(myExternalDir + "/" + data + ".aoao");
			try {
				FileInputStream fis = new FileInputStream(myExternalFile);
				DataInputStream in = new DataInputStream(fis);
				BufferedReader br = new BufferedReader(
						new InputStreamReader(in));
				String strLine;
				while ((strLine = br.readLine()) != null) {
					isiFile = isiFile + strLine + eol;
				}
				in.close();
			} catch (IOException e) {
				Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
			}
			new AlertDialog.Builder(DataOutlay.this)
					.setTitle("Detail")
					.setMessage(isiFile)
					.setNeutralButton("Close",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
								}
							}).show();
		}
	}

	public void deleteData(String data) {
		if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
			finish();
		} else {
			myExternalDir = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/.Application of Anjem/Outlay/" + newmonth);
			if (!myExternalDir.exists()) {
				myExternalDir.mkdirs();
			}
			myExternalFile = new File(myExternalDir + "/" + data + ".aoao");
			try {
				myExternalFile.delete();
			} catch (Exception e) {
				Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
			}
			refreshData();
			Toast.makeText(this, "'" + data + "' deleted", Toast.LENGTH_SHORT)
					.show();
		}
	}

	/** This will be invoked when a menu item is selected */
	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		String isi = (String) getListAdapter().getItem(info.position);

		if (item.getItemId()==R.id.menu_member_detail){
			searchData(isi);
		}else{
			deleteData(isi);
		}
		return true;
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