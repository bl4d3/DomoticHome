package nerdydog.domoHome;

import java.util.ArrayList;


import nerdydog.domoHome.json.ParseJSON;
import nerdydog.domoHome.object.Actuator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
                    // TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                     if (tt != null) {
                           tt.setText(a.getName());                            
                     }
                     /*if(bt != null){
                           bt.setText(a.getStatus());
                     }*/
                     
                     Button buttonAction = (Button) v.findViewById(R.id.buttonaction);
                     buttonAction.setTag(a);
                     
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
     
    // button action listener
	View.OnClickListener actionClicked = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Actuator a = (Actuator)v.getTag();
			
			actuatorName = a.getName();
			new DownloadFilesTask().execute(a.getIp(), a.getOut(), "1");
			dialogLoading = ProgressDialog.show(c, "","Loading. Please wait...", true);
			

		}
	};	
	
    // button action close listener
	View.OnClickListener actionClickedClose = new View.OnClickListener() {
		@Override
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
