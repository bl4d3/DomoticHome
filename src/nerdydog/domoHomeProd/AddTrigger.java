package nerdydog.domoHomeProd;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import nerdydog.domoHomeProd.db.ConfDatabase;
import nerdydog.domoHomeProd.db.ToDoDBAdapter;
import nerdydog.domoHomeProd.object.Action;
import nerdydog.domoHomeProd.object.Actuator;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class AddTrigger extends Activity{
	
	private final Context context= this;
	ToDoDBAdapter toDoDBAdapter;
	private final String LOG = "AddTrigger";
	int actuator_id = -1;
	ArrayList<Actuator> actuators;
	CheckBox mon, tue, wed, thu, fri, sat, sun;
	
	public void onCreate(Bundle savedInstanceState)  {
	 super.onCreate(savedInstanceState);
     setContentView(R.layout.addtrigger);
     
	 Bundle extras = getIntent().getExtras();
	 if(extras != null){
		 actuator_id = extras.getInt("actuator_id");
         Log.i(LOG, "actuator_id " + actuator_id );
	 }
     
	 toDoDBAdapter = new ToDoDBAdapter(this);
	 if (actuator_id != -1){
	     toDoDBAdapter.open();
	     actuators = toDoDBAdapter.getAllActuators(
	     		ConfDatabase.ACTUATOR_ID + "=" + "?", new String[]{String.valueOf(actuator_id)}, null);
	     toDoDBAdapter.close();
	 }
	 
	 /* initialize checkbox days */
	 mon = (CheckBox)findViewById(R.id.mon);
	 tue = (CheckBox)findViewById(R.id.tue);
	 wed = (CheckBox)findViewById(R.id.wed);
	 thu = (CheckBox)findViewById(R.id.thu);
	 fri = (CheckBox)findViewById(R.id.fri);
	 sat = (CheckBox)findViewById(R.id.sat);
	 sun = (CheckBox)findViewById(R.id.sun);
	      
     Button btnDate=(Button)findViewById(R.id.btndate);
     Button btnTime=(Button)findViewById(R.id.btntime);
     final TextView txtDate=(TextView)findViewById(R.id.txtdate);
     final TextView txtTime=(TextView)findViewById(R.id.txttime);
     
     Button btnDateEnd=(Button)findViewById(R.id.btndateEnd);
     Button btnTimeEnd=(Button)findViewById(R.id.btntimeEnd);
     final TextView txtDateEnd=(TextView)findViewById(R.id.txtdateEnd);
     final TextView txtTimeEnd=(TextView)findViewById(R.id.txttimeEnd);        
     
     Button btnAddTrigger = (Button)findViewById(R.id.btnInsertTrigger);
     
     /* INITIALIZE */
     Calendar cal=Calendar.getInstance();
     txtDate.setText(cal.get(Calendar.DAY_OF_MONTH)-1+"/"+cal.get(Calendar.MONTH)+"/"+cal.get(Calendar.YEAR));
     txtTime.setText(cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE));

     txtDateEnd.setText(cal.get(Calendar.DAY_OF_MONTH)+"/"+cal.get(Calendar.MONTH)+"/"+cal.get(Calendar.YEAR));
     txtTimeEnd.setText(cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE));
     
     
     
     /* QUERY ALARMS */
     btnAddTrigger.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Log.i("-->", "End " + txtDate.getText() + " " + txtTime.getText());
				Log.i("-->", "Start " + txtDateEnd.getText() + " " + txtTimeEnd.getText());
				
				GregorianCalendar dtStart = new GregorianCalendar(Integer.valueOf(txtDate.getText().toString().split("/")[2]),
						Integer.valueOf(txtDate.getText().toString().split("/")[1]),
						Integer.valueOf(txtDate.getText().toString().split("/")[0]),
						Integer.valueOf(txtTime.getText().toString().split(":")[0]),
						Integer.valueOf(txtTime.getText().toString().split(":")[1]));

				GregorianCalendar dtEnd= new GregorianCalendar(Integer.valueOf(txtDateEnd.getText().toString().split("/")[2]),
						Integer.valueOf(txtDateEnd.getText().toString().split("/")[1]),
						Integer.valueOf(txtDateEnd.getText().toString().split("/")[0]),
						Integer.valueOf(txtTimeEnd.getText().toString().split(":")[0]),
						Integer.valueOf(txtTimeEnd.getText().toString().split(":")[1]));
				
				EditText etTriggerName = (EditText)findViewById(R.id.edittexttriggername);
				
				String triggerName = etTriggerName.getText().toString();
				
				if(triggerName != null){
					/*get days*/
					String s_mon = "0", s_tue = "0", s_wed = "0", s_thu = "0", s_fri = "0", s_sat = "0", s_sun = "0", s_checkbox = "";
					if(mon.isChecked())
						s_mon = "1";
					if(tue.isChecked())
						s_tue = "1";
					if(wed.isChecked())
						s_wed = "1";
					if(thu.isChecked())
						s_thu = "1";
					if(fri.isChecked())
						s_fri = "1";
					if(sat.isChecked())
						s_sat = "1";
					if(sun.isChecked())
						s_sun = "1";
					s_checkbox = s_mon + s_tue + s_wed + s_thu + s_fri + s_sat + s_sun;
					Log.i(LOG, "...inserting action " + triggerName);
					Actuator a;
					a = actuators.get(0);
					Date dateNow = new Date();
					Action action = new Action(-1, a.getId(), -1, dtStart.getTime(), dtEnd.getTime(), dateNow, triggerName, 0, -1, 1, 0, s_checkbox);
					toDoDBAdapter.open();
					toDoDBAdapter.insertAction(action);
					toDoDBAdapter.close();
					// back at home
					Intent iHome = new Intent(context, DomoHome.class);
					context.startActivity(iHome);
				}
			}
		});

     /* DATE START */
		final OnDateSetListener odsl=new OnDateSetListener()
     {
		
		   public void onDateSet(DatePicker arg0, int year, int month, int dayOfMonth) {
		    // TODO Auto-generated method stub
		    
		    txtDate.setText(dayOfMonth+"/"+month+"/"+year);
		   }
		         
     };
		
     btnDate.setOnClickListener(new View.OnClickListener() 
     {
		
		   public void onClick(View arg0) {
		    // TODO Auto-generated method stub
		   
		    
		    Calendar cal=Calendar.getInstance();
		    DatePickerDialog datePickDiag=new DatePickerDialog(context,odsl,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
		    datePickDiag.show();
		   }
		         
     });
     
     /* DATE END */
		final OnDateSetListener odslEnd=new OnDateSetListener()
     {
		
		   public void onDateSet(DatePicker arg0, int year, int month, int dayOfMonth) {
		    // TODO Auto-generated method stub
		    
		    txtDateEnd.setText(dayOfMonth+"/"+month+"/"+year);
		   }
		         
     };
		
     btnDateEnd.setOnClickListener(new View.OnClickListener() 
     {
		
		   public void onClick(View arg0) {
		    Calendar cal=Calendar.getInstance();
		    DatePickerDialog datePickDiag=new DatePickerDialog(context,odslEnd,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
		    datePickDiag.show();
		   }
		         
     });        
		
     
     /* START TIME */
     final OnTimeSetListener otsl=new OnTimeSetListener()
     {

		   public void onTimeSet(TimePicker arg0, int hourOfDay, int minute) {
		    txtTime.setText(hourOfDay+":"+minute);
		   }
		 };
		 
		 btnTime.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) { 
				Calendar cal=Calendar.getInstance();
				TimePickerDialog timePickDiag=new TimePickerDialog(context,otsl,cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),true);
				timePickDiag.show();
			}
		         
		  });	
		 
		/* END TIME */
		final OnTimeSetListener otslEnd=new OnTimeSetListener()
		{
		
		   public void onTimeSet(TimePicker arg0, int hourOfDay, int minute) {
		    txtTimeEnd.setText(hourOfDay+":"+minute);
		   }
		 };
		 
		 btnTimeEnd.setOnClickListener(new View.OnClickListener() {
		
			public void onClick(View arg0) { 
				Calendar cal=Calendar.getInstance();
				TimePickerDialog timePickDiag=new TimePickerDialog(context,otslEnd,cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),true);
				timePickDiag.show();
			}
		         
		  });		
	}
}
