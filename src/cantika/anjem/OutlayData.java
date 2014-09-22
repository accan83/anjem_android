package cantika.anjem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class OutlayData extends Activity {
	File myExternalFile;
	File myExternalDir;
	File outlayFiles[];
	SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
	SimpleDateFormat sdfMonth = new SimpleDateFormat("MMMM yyyy", Locale.US);
	String newdate = sdfDate.format(new Date(System.currentTimeMillis()));
	String newmonth;
	int yr, month, day;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.outlay_data);

		Calendar hsl = Calendar.getInstance();
		Intent i = this.getIntent();
		yr = i.getIntExtra("yr", 0);
		month = i.getIntExtra("month", 0);
		day = i.getIntExtra("day", 0);
		hsl.set(yr, month, day);
		java.util.Date d = new java.util.Date(hsl.getTimeInMillis());
		newmonth = sdfMonth.format(d);

		if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
			finish();
		} else {
			myExternalDir = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/.Application of Anjem/Outlay/" + newmonth);
			if (!myExternalDir.exists()) {
				myExternalDir.mkdirs();
			}
		}

		Button goSave = (Button) findViewById(R.id.btn_Odata_add);
		goSave.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String CustCompl = "False";
				// validate data entries
				String CustName = ((EditText) findViewById(R.id.edit_Odata_name))
						.getText().toString().trim();
				String CustQty = ((EditText) findViewById(R.id.edit_Odata_qty))
						.getText().toString().trim();
				String CustFee = ((EditText) findViewById(R.id.edit_Odata_fee))
						.getText().toString().trim();
				CheckBox completed = ((CheckBox) findViewById(R.id.chk_Odata_completed));
				if (completed.isChecked()) {
					CustCompl = "True";
				}

				Context context = OutlayData.this;
				if (CustName.equals("") || CustQty.equals("")
						|| CustFee.equals("")) {
					String e = "Please complete the data";
					new AlertDialog.Builder(context)
							.setTitle("Invalid Data")
							.setMessage(e)
							.setNeutralButton("Close",
									new DialogInterface.OnClickListener() {

										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
										}
									}).show();
				} else {
					// If OK, then send the data to save
					saveData(CustName, CustQty, CustFee, CustCompl);
				}
			}
		});
		TextView DB = (TextView) findViewById(R.id.txt_DB_O);
		DB.setText("Database: " + newmonth);

		Button goDat = (Button) findViewById(R.id.btn_Odata_data);
		goDat.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(OutlayData.this, DataOutlay.class);
				i.putExtra("yr", yr);
				i.putExtra("month", month);
				i.putExtra("day", day);
				startActivity(i);
			}
		});
	}

	public void saveData(String custName, String custQty, String custFee,
			String custCmpl) {
		EditText nama = (EditText) findViewById(R.id.edit_Odata_name);
		EditText jumlah = (EditText) findViewById(R.id.edit_Odata_qty);
		EditText bayar = (EditText) findViewById(R.id.edit_Odata_fee);
		CheckBox selesai = (CheckBox) findViewById(R.id.chk_Odata_completed);

		String eol = System.getProperty("line.separator");
		myExternalFile = new File(myExternalDir + "/" + custName + ".aoao");
		try {
			myExternalFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(myExternalFile);
			String outlayString = null;
			outlayString = "#" + newdate + "#" + eol + "Name : " + custName
					+ eol + "Quantity : " + custQty + eol + "Fee : " + custFee
					+ eol + "Status : " + custCmpl;
			fos.write(outlayString.toString().getBytes());
			fos.close();
			nama.setText("");
			jumlah.setText("");
			bayar.setText("");
			selesai.setChecked(false);
			Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
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