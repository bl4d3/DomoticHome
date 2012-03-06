package nerdydog.domoHomeProd;

import java.util.ArrayList;


import nerdydog.domoHomeProd.db.ConfDatabase;
import nerdydog.domoHomeProd.json.ParseJSON;
import nerdydog.domoHomeProd.object.Action;
import nerdydog.domoHomeProd.object.Actuator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ActuatorAdapter extends ArrayAdapter<Actuator> {
	 private ArrayList<Actuator> items;
	 String TAG = "ActuatorAdapter";
	 String actuatorName = "";
	 ProgressDialog dialogLoading;
	 Context c;

     public ActuatorAdapter(Context context, int textViewResourceId, ArrayList<Actuator> items) {
             super(context, textViewResourceId, items);
             c = context;
             this.items = items;
     }

     public View getView(int position, View convertView, ViewGroup parent) {
             View v = convertView;
             if (v == null) {
                 LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                 v = vi.inflate(R.layout.rowactuator, null);
             }

             Actuator a = items.get(position);
             if (a != null) {
                     TextView tt = (TextView) v.findViewById(R.id.toptext);
                     tt.setTag(a);
                     
                     if(a.isTrigger())
                    	 tt.setOnLongClickListener(onlong);
                    // TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                     if (tt != null) {
                           tt.setText(a.getName());                            
                     }
                     /*if(bt != null){
                           bt.setText(a.getStatus());
                     }*/
                     
                     // TODO 
                     // change button icon when type="SOFA" and raise a new avtivity
                     Button buttonAction = (Button) v.findViewById(R.id.buttonaction);
                     buttonAction.setTag(a);
                     //buttonAction.setBackgroundDrawable(R.id.buttonaction);
                     
                     buttonAction.setOnClickListener(actionClicked);

                     Button buttonActionClose = (Button) v.findViewById(R.id.buttonactionclose);
                     buttonActionClose.setTag(a);
                     buttonActionClose.setOnClickListener(actionClickedClose);   
                     
                     // hide close button for the actuator that does not support this functions
                     Log.i(TAG, "bool "+a.isSingleButton() + " " + a.getType());
                     if(a.isSingleButton()){
                    	 buttonActionClose.setVisibility(View.INVISIBLE);
                     }
             }
             return v;
     }
     
     // used to show options available
     View.OnLongClickListener onlong = new View.OnLongClickListener() {
 		
 		public boolean onLongClick(final View v) {
 		      //set up dialog
 			//set up dialog
 	          final Dialog dialog = new Dialog(c);
 	          
 	          dialog.setContentView(R.layout.popupaction);
 	          dialog.setTitle("Options available");
 	          dialog.setCancelable(true);
 	          
 	          TextView tv = (TextView)dialog.findViewById(R.id.textviewpopup);
 	          tv.setText("Add trigger");
 	          
 	          tv.setOnClickListener(new View.OnClickListener() {
 				
 				public void onClick(View arg0) {
 					Actuator a = (Actuator) v.getTag();
 					Log.i(TAG, "click " + a.getId() + " name " + a.getName());
 					Intent iTrigger = new Intent(c, AddTrigger.class);
 					iTrigger.putExtra("actuator_id", a.getId());
 					c.startActivity(iTrigger);
 				}
 			  });
 	          //now that the dialog is set up, it's time to show it    
 	          dialog.show();    
 			
 			return false;
 		}
 	};
     
    // button action listener
	View.OnClickListener actionClicked = new View.OnClickListener() {
		
		public void onClick(View v) {
			Actuator a = (Actuator)v.getTag();
			Log.i(TAG, "----> " + a.getType() + " - " + ConfDatabase.TYPE_SOFA);
			if(new String(a.getType()).equals(new String(ConfDatabase.TYPE_SOFA))){
				if(a.getOut().equals(new String("0")))
				{
					Log.i(TAG, "Armchair");
					Intent i = new Intent(c, Sofa.class);
					i.putExtra("ip", a.getIp());
					c.startActivity(i);
				}
				
				if(a.getOut().equals(new String("1")))
				{
					Log.i(TAG, "Armchair 1m");
					Intent i = new Intent(c, Sofa1mFeet.class);
					i.putExtra("ip", a.getIp());
					c.startActivity(i);
				}
				
				if(a.getOut().equals(new String("2")))
				{
					Log.i(TAG, "Sofa 2 m feet");
					Intent i = new Intent(c, Sofa2mFeet.class);
					i.putExtra("ip", a.getIp());
					c.startActivity(i);
				}
			}else{
				Log.i(TAG, "No Sofa");
				actuatorName = a.getName();
				new DownloadFilesTask().execute(a.getIp(), a.getOut(), "1");
				dialogLoading = ProgressDialog.show(c, "","Loading. Please wait...", true);
			}

		}
	};	
	
    // button action close listener
	View.OnClickListener actionClickedClose = new View.OnClickListener() {
		
		public void onClick(View v) {
			Actuator a = (Actuator)v.getTag();
			
			actuatorName = a.getName();
			new DownloadFilesTask().execute(a.getIp(), a.getOut(), "0");
			dialogLoading = ProgressDialog.show(c, "","Loading. Please wait...", true);
			

		}
	};		
	
	 private class DownloadFilesTask extends AsyncTask<String, String, Boolean> {
		 
	     protected Boolean doInBackground(String... strings) {
	    	 
	         String url = "http://" + strings[0] + "/?out=" + strings[1] + "&status=" + strings[2];
	         return ParseJSON.doRequestToArduino(url);
	     }

	     protected void onPostExecute(Boolean result) {
	    	 dialogLoading.dismiss();
	    	 String msg = "An error has accoured, check your internet connection. Is Arduino alive?";
	    	 if(result){
	    		 if(actuatorName != "")
	    			 msg = actuatorName + " has been opened!";
	    		 else
	    			 msg = "Well done!";
	    	 }
	    	 if(!result)
	    		 Utility.showDialog(c, msg);
	     }
	 }	
}
