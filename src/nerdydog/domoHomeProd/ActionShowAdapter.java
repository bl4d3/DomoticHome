package nerdydog.domoHomeProd;


import java.text.SimpleDateFormat;
import java.util.ArrayList;

import nerdydog.domoHomeProd.db.ConfDatabase;
import nerdydog.domoHomeProd.db.ToDoDBAdapter;
import nerdydog.domoHomeProd.json.ParseJSON;
import nerdydog.domoHomeProd.object.Action;
import nerdydog.domoHomeProd.object.Actuator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ActionShowAdapter extends ArrayAdapter<Action>{
	
	 private ArrayList<Action> items;
	 String TAG = "ActionShowAdapter";
	 String actuatorName = "";
	 ProgressDialog dialogLoading;
	 Context c;
	
    public ActionShowAdapter(Context context, int textViewResourceId, ArrayList<Action> items) {
        super(context, textViewResourceId, items);
        c = context;
        this.items = items;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.actionshowadapter, null);
        }
        
        Action a = items.get(position);
        v.setTag(a);
        if (a != null) {        
        	
        	//check if is a trigger
        	boolean is_trigger = false;
        	if(!a.getStarttime().equals(a.getEndtime())){
        		is_trigger = true;
        	}
        	
        	TextView tt = (TextView) v.findViewById(R.id.info1actionshowadapter);
        	if(!is_trigger){
        		tt.setText(a.getName());
        		TextView tt2 = (TextView) v.findViewById(R.id.info2actionshowadapter);
        		tt2.setText("Triggered manually");
        		
            	Button buttonAction = (Button) v.findViewById(R.id.buttonactionshowadapter);
            	buttonAction.setTag(a);
            	
            	buttonAction.setOnClickListener(buttonActionClicked);
        	}else{
        		SimpleDateFormat formatter = new SimpleDateFormat("hh:mm dd-MM-yy");
        		tt.setText(a.getName());
        		
        		if(a.getIs_trigger() == 1)
        		{
	        		TextView tt2 = (TextView) v.findViewById(R.id.info2actionshowadapter);
	        		tt2.setText(formatter.format(a.getEndtime()));
	        		
	        		TextView tt3 = (TextView) v.findViewById(R.id.info3actionshowadapter);
	        		tt3.setText(formatter.format(a.getStarttime()));
	        		
	        		TextView tt4 = (TextView) v.findViewById(R.id.info4actionshowadapter);
	        		tt4.setText("Days of the week: " + Utility.getScheduledDays(a.getScheduled()));
        		}
        	}
        }
        v.setOnClickListener(elementClicked);
        v.setOnLongClickListener(onlong);
        
        // long click listener
        return v;
    }   
    
    View.OnLongClickListener onlong = new View.OnLongClickListener() {
		
		public boolean onLongClick(final View v) {
		      //set up dialog
			//set up dialog
	          final Dialog dialog = new Dialog(c);
	          
	          dialog.setContentView(R.layout.popupaction);
	          dialog.setTitle("Options available");
	          dialog.setCancelable(true);
	          
	          TextView tv = (TextView)dialog.findViewById(R.id.textviewpopup);
	          
	          tv.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View arg0) {
					Action a = (Action) v.getTag();
					Log.i(TAG, "click " + a.getId() + " parent " + a.getParent_id());
					removeAction(a);
					Intent iHome = new Intent(c, DomoHome.class);
					c.startActivity(iHome);
				}
			  });
	          //now that the dialog is set up, it's time to show it    
	          dialog.show();    
			
			return false;
		}
	};
    
    View.OnClickListener elementClicked = new View.OnClickListener() {
		
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Log.i(TAG, "elementClicked");
			
		}
	};    
    
    View.OnClickListener buttonActionClicked = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.i(TAG, "buttonActionClicked");
			Action a = (Action)v.getTag();
			Log.i(TAG, "buttonActionClicked " + a.getId() + " parent " + a.getParent_id());
			ArrayList<Actuator> aryActuator = new ArrayList<Actuator>();
			
			ToDoDBAdapter toDoDBAdapter = new ToDoDBAdapter(c);
			toDoDBAdapter.open();
			Log.i(TAG, "action id root " + a.getId());
			ArrayList<Action> aryAction = new ArrayList<Action>();
			
			aryAction.add(a);
			
			ArrayList<Action> aryParkAction  = toDoDBAdapter.getAllAction(ConfDatabase.ACTION_ROOT_ID + "=" + "?", 
					new String[]{Integer.toString(a.getId())}, 
					null);
			for(int i = 0; i < aryParkAction.size(); i++){
				aryAction.add((Action)aryParkAction.get(i));
			}
			
			for(int i = 0; i < aryAction.size(); i++){
				Action parkAction = aryAction.get(i);
				int parkDomoId = parkAction.getId();
				if(i==0){
					//execute
					Log.i(TAG, "domo id root " + parkAction.getDomo_id());
					ArrayList<Actuator> aryParkActuator = toDoDBAdapter.getAllActuators(ConfDatabase.ACTUATOR_ID + "=" + "?", 
							new String[]{Integer.toString(parkAction.getDomo_id())}, 
							null);
					if(aryParkActuator.get(0)!=null)
						aryActuator.add(aryParkActuator.get(0));

				}else{
					if(aryAction.get(i-1).getDomo_id() == parkAction.getParent_id()){
						// do nect action
						Log.i(TAG, "domo id " + parkAction.getDomo_id());
						ArrayList<Actuator> aryParkActuator = toDoDBAdapter.getAllActuators(ConfDatabase.ACTUATOR_ID + "=" + "?", 
								new String[]{Integer.toString(parkAction.getDomo_id())}, 
								null);
						if(aryParkActuator.get(0)!=null)
							aryActuator.add(aryParkActuator.get(0));
					}
				}
			}
			toDoDBAdapter.close();
			Utility.runActuator(aryActuator);
		}
	};
	
	private boolean removeAction(Action a){
		ToDoDBAdapter toDoDBAdapter = new ToDoDBAdapter(c);
		toDoDBAdapter.open();
		
	
		ArrayList<Action> aryAction = toDoDBAdapter.getAllAction(ConfDatabase.ACTION_ROOT_ID + "=" + "?", 
					new String[]{Integer.toString(a.getId())}, 
					null);
		
		for( int i = 0; i < aryAction.size(); i++){
			Action parkAction = aryAction.get(i);
			if(parkAction!=null){
				toDoDBAdapter.removeAction(parkAction.getId());
			}
		}
		toDoDBAdapter.removeAction(a.getId());
		toDoDBAdapter.close();
		return false;
	}
	

}
