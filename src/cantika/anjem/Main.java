package cantika.anjem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.widget.DatePicker;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.content.Context;

public class Main extends Activity {
	DatePicker datepicker;
	int yr, day,itemChoise, month;
	String newmonth;
	SimpleDateFormat sdfMonth = new SimpleDateFormat("MMMM yyyy", Locale.US);
	CharSequence[] items={"Income","Outlay"};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Calendar today = Calendar.getInstance();
        yr=today.get(Calendar.YEAR);
        month=today.get(Calendar.MONTH);
        day=today.get(Calendar.DAY_OF_MONTH);
        datepicker=(DatePicker)findViewById(R.id.datePicker1);
		Calendar hsl=Calendar.getInstance();
		hsl.set(yr, month, day);
		java.util.Date d = new java.util.Date(hsl.getTimeInMillis());
		newmonth =  sdfMonth.format(d);

        
    /** BUTTON MEMBER PADA MAIN. */
        Button gomem = (Button)findViewById(R.id.btn_member);
        gomem.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Main.this, Member.class);
				i.putExtra("pesan", "::MEMBER::");
				startActivity(i);
			}
		});

        /** BUTTON DATA PADA MAIN. */
        Button godat = (Button)findViewById(R.id.btn_data);
        godat.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(1);
			}
		});

        /** BUTTON REPORT PADA MAIN. */
        Button gorep = (Button)findViewById(R.id.btn_report);
        gorep.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Main.this, Report.class);
				i.putExtra("pesan", "::REPORT::");
				i.putExtra("yr", yr);
				i.putExtra("month", month);
				i.putExtra("day", day);					
				startActivity(i);
			}
		});

        /** BUTTON ABOUT PADA MAIN. */
        Button goab = (Button)findViewById(R.id.btn_about);
        goab.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Context context = Main.this;
				new AlertDialog.Builder(context)
				.setTitle("ABOUT")
				.setMessage("This application was programming by Accan\nThank's to:\n-Allah\n-Accan's Software 2012\n-CANTIKA Corporation")
				.setNeutralButton("Close",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
							}
						}).show();
				}
		});

        /** BUTTON DATABASE PADA MAIN. */
        Button db = (Button)findViewById(R.id.btn_db);
        db.setText("SET DATABASE ("+newmonth+")");
        db.setOnClickListener(new View.OnClickListener() {
			
			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(0);
			}
		});
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	switch (id) {
    	case 0:
    		return new DatePickerDialog(this, mDateSetListener, yr, month, day);

    	case 1:
    		return new AlertDialog.Builder(this)
    		.setIcon(cantika.anjem.R.drawable.ic_logo)
    		.setTitle("Pilih salah satu")
    		.setNegativeButton("Batal", null)
    		.setItems(items, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					if(items[which]=="Income"){
					Intent i = new Intent(Main.this, IncomeData.class);
					i.putExtra("pesan", "::Income::");
					i.putExtra("yr", yr);
					i.putExtra("month", month);
					i.putExtra("day", day);					
					startActivity(i);}
					else{
					Intent i = new Intent(Main.this, OutlayData.class);
					i.putExtra("pesan", "::Outlay::");
					i.putExtra("yr", yr);
					i.putExtra("month", month);
					i.putExtra("day", day);					
					startActivity(i);
					}
				}
			}).create();
    	}
    	return null;
    }
    
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			Calendar hsl= Calendar.getInstance();
			yr=year;
			month=monthOfYear;
			day=dayOfMonth;
			hsl.set(yr, month, day);
			java.util.Date d = new java.util.Date(hsl.getTimeInMillis());
			newmonth =  sdfMonth.format(d);
	        Button db = (Button)findViewById(R.id.btn_db);
	        db.setText("SET DATABASE ("+newmonth+")");
			Toast.makeText(getBaseContext(), "Database terpilih '"+newmonth+"'", Toast.LENGTH_SHORT).show();
		}
	};
}