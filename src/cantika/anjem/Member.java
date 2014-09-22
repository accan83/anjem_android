package cantika.anjem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Member extends Activity {
	File myExternalFile;
	File myExternalDir;
	File myExternalDir2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member);

		if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
			finish();
		} else {
			myExternalDir = new File(Environment.getExternalStorageDirectory().getPath() + "/.Application of Anjem/Member/");
			if (!myExternalDir.exists()) {
				myExternalDir.mkdirs();
			}
		}

		TextView txtMember = (TextView) findViewById(R.id.txt_member);
		Intent i = this.getIntent();
		txtMember.setText(i.getStringExtra("pesan"));

		Button btnEducation = (Button) findViewById(R.id.btn_add_member_save);
		btnEducation.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// validate data entries
				String CustName = ((EditText) findViewById(R.id.edit_member_name))
						.getText().toString().trim();
				String CustAddr = ((EditText) findViewById(R.id.edit_member_address))
						.getText().toString().trim();
				String CustClass = ((EditText) findViewById(R.id.edit_member_class))
						.getText().toString().trim();
				String CustFee = ((EditText) findViewById(R.id.edit_member_fee))
						.getText().toString().trim();
				String CustProgram = ((Spinner) findViewById(R.id.spin_member_program))
						.getSelectedItem().toString().trim();

				Context context = Member.this;
				if (CustName.equals("") || CustAddr.equals("")
						|| CustClass.equals("") || CustFee.equals("")
						|| CustProgram.equals("")) {
					String e = "Please complete the data.";
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
					saveData(CustName, CustAddr, CustClass, CustFee,
							CustProgram);
				}
			}
		});
		Button goDat = (Button) findViewById(R.id.btn_member_data);
		goDat.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Member.this, DataMember.class);
				startActivity(i);
			}
		});
	}

	public void saveData(String custName, String custAddr, String custClass,
			String custFee, String custProgram) {
		EditText nama = (EditText) findViewById(R.id.edit_member_name);
		EditText alamat = (EditText) findViewById(R.id.edit_member_address);
		EditText kelas = (EditText) findViewById(R.id.edit_member_class);
		EditText bayar = (EditText) findViewById(R.id.edit_member_fee);
		Spinner program = (Spinner) findViewById(R.id.spin_member_program);

		String eol = System.getProperty("line.separator");
		myExternalFile = new File(myExternalDir +"/"+custName+".aoam");
		try {
			myExternalFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(myExternalFile);
			String memberString = null;
			memberString = "Name : " + custName + eol + "Address : " + custAddr
					+ eol + "Class : " + custClass + eol + "Fee : " + custFee
					+ eol + "Program : " + custProgram + eol + "Status : False";
			fos.write(memberString.toString().getBytes());
			fos.close();
			nama.setText("");
			alamat.setText("");
			kelas.setText("");
			bayar.setText("");
			program.setSelection(0);
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