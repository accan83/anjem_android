package cantika.anjem;

import android.app.Activity;
import android.content.Intent;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

import android.widget.ArrayAdapter;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.CheckBox;
import android.widget.Toast;

public class IncomeData extends Activity {
	File myExternalFileI;
	File myExternalFileM;
	File myExternalDirI;
	File myExternalDirM;
	File incomeFiles[];
	SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
	SimpleDateFormat sdfMonth = new SimpleDateFormat("MMMM yyyy", Locale.US);
	String newdate = sdfDate.format(new Date(System.currentTimeMillis()));
	String newmonth;
	int yr, month, day;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(cantika.anjem.R.layout.income_data);

		Calendar hsl = Calendar.getInstance();
		Intent i = this.getIntent();
		yr = i.getIntExtra("yr", 0);
		month = i.getIntExtra("month", 0);
		day = i.getIntExtra("day", 0);
		hsl.set(yr, month, day);
		java.util.Date d = new java.util.Date(hsl.getTimeInMillis());
		newmonth = sdfMonth.format(d);

		addItemsOnSpinner();

		Button goOpen = (Button) findViewById(cantika.anjem.R.id.btn_Idata_open);
		goOpen.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Spinner spin = (Spinner) findViewById(cantika.anjem.R.id.spin_Idata_nama);
				String txtSpin = spin.getSelectedItem().toString();
				if (txtSpin.equals("-Choose a Name-")) {
					TextView alamat = (TextView) findViewById(R.id.txt_Idata_addr);
					alamat.setText("--");
					TextView kelas = (TextView) findViewById(R.id.txt_Idata_Class);
					kelas.setText("--");
					TextView bayar = (TextView) findViewById(R.id.txt_Idata_fee);
					bayar.setText("--");
					TextView program = (TextView) findViewById(R.id.txt_Idata_prog);
					program.setText("--");
					CheckBox status = (CheckBox) findViewById(R.id.chk_Idata_status);
					status.setChecked(false);
					status.setClickable(false);
				} else {
					openData(txtSpin);
				}
			}
		});

		Button goSave = (Button) findViewById(cantika.anjem.R.id.btn_Idata_save);
		goSave.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Spinner SPINNER = ((Spinner) findViewById(cantika.anjem.R.id.spin_Idata_nama));
				String CustName = SPINNER.getSelectedItem().toString().trim();
				if (CustName.equals("-Choose a Name-")) {
					TextView alamat = (TextView) findViewById(R.id.txt_Idata_addr);
					alamat.setText("--");
					TextView kelas = (TextView) findViewById(R.id.txt_Idata_Class);
					kelas.setText("--");
					TextView bayar = (TextView) findViewById(R.id.txt_Idata_fee);
					bayar.setText("--");
					TextView program = (TextView) findViewById(R.id.txt_Idata_prog);
					program.setText("--");
					CheckBox status = (CheckBox) findViewById(R.id.chk_Idata_status);
					status.setChecked(false);
					status.setClickable(false);
				} else {
					String CustStat = "False";
					// validate data entries
					final CheckBox Status = ((CheckBox) findViewById(cantika.anjem.R.id.chk_Idata_status));
					if (Status.isChecked()) {
						CustStat = "True";
					}
					updateData(CustName, CustStat);
				}
			}
		});
		TextView DB = (TextView) findViewById(R.id.txt_DB_I);
		DB.setText("Database: " + newmonth);
	}

	public void addItemsOnSpinner() {
		if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
			finish();
		} else {
			myExternalDirM = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/.Application of Anjem/Member");
			if (!myExternalDirM.exists()) {
				myExternalDirM.mkdirs();
			}
			incomeFiles = myExternalDirM.listFiles();
			List<String> list = new ArrayList<String>();
			list.add("-Choose a Name-");
			Spinner nama = (Spinner) findViewById(cantika.anjem.R.id.spin_Idata_nama);

			for (int i = 0; i < incomeFiles.length; i++) {
				File file = incomeFiles[i];
				list.add(file.getName().substring(0,
						file.getName().length() - 5));
			}
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, list);
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			nama.setAdapter(dataAdapter);
		}
	}

	public void openData(String dd) {
		TextView alamat = (TextView) findViewById(R.id.txt_Idata_addr);
		TextView kelas = (TextView) findViewById(R.id.txt_Idata_Class);
		TextView bayar = (TextView) findViewById(R.id.txt_Idata_fee);
		TextView jasa = (TextView) findViewById(R.id.txt_Idata_prog);
		CheckBox status = (CheckBox) findViewById(R.id.chk_Idata_status);
		String Status = null;

		if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
			finish();
		} else {
			myExternalDirI = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/.Application of Anjem/Income/" + newmonth);
			myExternalDirM = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/.Application of Anjem/Member");
			if (!myExternalDirI.exists()) {
				myExternalDirI.mkdirs();
			}
			if (!myExternalDirM.exists()) {
				myExternalDirM.mkdirs();
			}
			List<String> isiFile = new ArrayList<String>();
			myExternalFileI = new File(myExternalDirI + "/" + dd + ".aoai");
			myExternalFileM = new File(myExternalDirM + "/"+ dd + ".aoam");
			if (!myExternalFileI.exists()) {
				copyFile(myExternalFileM, myExternalFileI);
			}
			try {
				FileInputStream fis = new FileInputStream(myExternalFileI);
				DataInputStream in = new DataInputStream(fis);
				BufferedReader br = new BufferedReader(
						new InputStreamReader(in));
				String strLine;
				while ((strLine = br.readLine()) != null) {
					isiFile.add(strLine);
				}
				in.close();
			} catch (IOException e) {
				Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
			}
			alamat.setText(isiFile.get(1)
					.substring(10, isiFile.get(1).length()));
			kelas.setText(isiFile.get(2).substring(8, isiFile.get(2).length()));
			bayar.setText(isiFile.get(3).substring(6, isiFile.get(3).length()));
			jasa.setText(isiFile.get(4).substring(10, isiFile.get(4).length()));
			Status = isiFile.get(5).substring(9, isiFile.get(5).length());
			if (Status.equals("True")) {
				status.setChecked(true);
			} else {
				status.setChecked(false);
			}
			status.setClickable(true);
		}
	}

	public void updateData(String custName, String custStat) {
		Spinner nama = (Spinner) findViewById(R.id.spin_Idata_nama);
		TextView alamat = (TextView) findViewById(R.id.txt_Idata_addr);
		TextView kelas = (TextView) findViewById(R.id.txt_Idata_Class);
		TextView bayar = (TextView) findViewById(R.id.txt_Idata_fee);
		TextView program = (TextView) findViewById(R.id.txt_Idata_prog);
		CheckBox status = (CheckBox) findViewById(R.id.chk_Idata_status);

		String eol = System.getProperty("line.separator");
		if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
			finish();
		} else {
				myExternalDirI = new File(
						Environment.getExternalStorageDirectory().getPath() +"/.Application of Anjem/Income/" + newmonth);
			if (!myExternalDirI.exists()) {
				myExternalDirI.mkdirs();
			}
			myExternalFileI = new File(myExternalDirI + "/" + custName
					+ ".aoai");
			try {
				FileOutputStream fos = new FileOutputStream(myExternalFileI);
				String incomeString = null;
				incomeString = "Name : " + nama.getSelectedItem().toString()
						+ eol + "Address : " + alamat.getText().toString()
						+ eol + "Class : " + kelas.getText().toString() + eol
						+ "Fee : " + bayar.getText().toString() + eol
						+ "Program : " + program.getText().toString() + eol
						+ "Status : " + custStat + eol + "#" + newdate + "#";
				fos.write(incomeString.toString().getBytes());
				fos.close();
				nama.setSelection(0);
				alamat.setText("--");
				kelas.setText("--");
				bayar.setText("--");
				program.setText("--");
				status.setChecked(false);
				status.setClickable(false);
				Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
			}
		}
	}

	public static boolean copyFile(File source, File dest) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {
			bis = new BufferedInputStream(new FileInputStream(source));
			bos = new BufferedOutputStream(new FileOutputStream(dest, false));

			byte[] buf = new byte[1024];
			bis.read(buf);

			String tgl = "#00.00.0000#";
			bos.write(tgl.getBytes());
			do {
				bos.write(buf);
			} while (bis.read(buf) != -1);
		} catch (IOException e) {
			return false;
		} finally {
			try {
				if (bis != null)
					bis.close();
				if (bos != null)
					bos.close();
			} catch (IOException e) {
				return false;
			}
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