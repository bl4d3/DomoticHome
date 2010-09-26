package nerdydog.domoHome;

import java.util.ArrayList;
import java.util.ListIterator;

import nerdydog.domoHome.db.ConfDatabase;
import nerdydog.domoHome.db.ToDoDBAdapter;
import nerdydog.domoHome.json.ParseJSON;
import nerdydog.domoHome.object.Actuator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class Utility extends Activity{
	
    public static final int  ABOUT = 1;
    public static final int  BACK = 2;
    public static final int  SYNC = 3;  
    public static final int  SETTINGS = 4;
    public static final int  ACTIONS = 5;
    public static boolean continueSync;
    
    public static final String TAG = "Utility";
    
    public static boolean showMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu, menu);
    	return true;
    }
    
    public static boolean choiceMenu(MenuItem item, Activity l){
    	switch (item.getItemId()){

    	case BACK:
    		l.finish();
    		return true;
    		
    	case R.id.menusync:
    		syncArduino(l);
    		return true;
		case R.id.menusettings:
			Intent i = new Intent(l, Settings.class);
			l.startActivity(i);			
			return true;
		case R.id.menuactions:
			Intent iaction = new Intent(l, SelectDomoItem.class);
			l.startActivity(iaction);	
			return true;
		case R.id.menuabout:
			Intent iabout = new Intent(l, About.class);
			l.startActivity(iabout);	
			return true;			
		}    	
    	return false;
    }      
    
    // sync arduino - db
    public static void syncArduino(final Activity activity){
    	 continueSync = false;
    	 AlertDialog.Builder alertbox = new AlertDialog.Builder(activity);
    	 alertbox.setTitle("Arduino Sync");
    	 alertbox.setMessage("All the data will be deleted. Are you sure?");
         // set a positive/yes button and create a listener
         alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

             // do something when the button is clicked
             public void onClick(DialogInterface arg0, int arg1) {
            	 Log.i(TAG, "yes " + continueSync);
            	 doSync(activity);
             }
         });

         // set a negative/no button and create a listener
         alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {

             // do something when the button is clicked
             public void onClick(DialogInterface arg0, int arg1) {
            	 Log.i(TAG, "no " + continueSync);
             }
         });

         // display box
         alertbox.show();
    }
    
    public static void doSync(Activity activity){
    	 ProgressDialog dialog = new ProgressDialog(activity);
    	 dialog.setMessage("Loading...");
    	 dialog.show();

    	try {
			ToDoDBAdapter toDoDBAdapter;
			// DATABASE
			toDoDBAdapter = new ToDoDBAdapter(activity);
			toDoDBAdapter.open();
			// insert actuators into the db
			ArrayList<Actuator> aryActuators = ParseJSON.getAllActuator();
			if (aryActuators.size() > 0) {
				toDoDBAdapter.clearActuator();
				toDoDBAdapter.clearAction();
				toDoDBAdapter.clearCounter();
				ListIterator<Actuator> iter = aryActuators.listIterator();
				while (iter.hasNext()) {
					Actuator actuator = iter.next();
					toDoDBAdapter.intertActuator(actuator);
				}
			}
			
			toDoDBAdapter.close();
			
			// start the DomeHome activity, in this way we refresh tha listview with all the actuators
			Intent i = new Intent(activity, DomoHome.class);
			activity.startActivity(i);
			Log.i(TAG, "do sync ");
			
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}finally{
			dialog.dismiss();
		}     
		
    }
    
    // initialize the list view with all the actuators
    public static void initializeActuators(ToDoDBAdapter toDoDBAdapter, ListView listViewType, Context c){
        ArrayList<Actuator> aryActuators = toDoDBAdapter.getAllActuators(null, null, ConfDatabase.ACTUATOR_TYPE);
        
        ArrayList<String> aryForList = new ArrayList<String>();
        if(aryActuators.size() > 0){
			ListIterator<Actuator> iter = aryActuators.listIterator();
			while (iter.hasNext()) {
				Actuator actuator = iter.next();
				aryForList.add(actuator.getType() + " (" + toDoDBAdapter.getAllActuators(ConfDatabase.ACTUATOR_TYPE + "=" + "?", new String[]{actuator.getType()}, null).size() + ")");
			}   
        }
        
        final ArrayAdapter<String> aa;
        aa = new ArrayAdapter<String>(c,
        							android.R.layout.simple_list_item_1,
        							aryForList);
        
        listViewType.setAdapter(aa); 
    }
    
    public static void showDialog(Context c, String msg){
	    AlertDialog.Builder alertbox = new AlertDialog.Builder(c);
		alertbox.setMessage(msg);
        // add a neutral button to the alert box and assign a click listener
        alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            // click listener on the alert box
            public void onClick(DialogInterface arg0, int arg1) {
                // the button was clicked
            }
        });

        // show it
        alertbox.show();
    }

	public static void runActuator(ArrayList<Actuator> aryActuator) {
		try {
			for(int i = 0; i < aryActuator.size(); i++){
				Actuator a = aryActuator.get(i);
		        String url = "http://" + ConfDatabase.CURRENT_IP_KEY + "/?out=" + a.getOut() + "&status=1";
		        Log.i(TAG, url);
		        ParseJSON.doRequestToArduino(url);
				Thread.sleep(1000);	
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    public static void getIpFromPreferencesData(Context c){
    	
        SharedPreferences prefs = c.getSharedPreferences(ConfDatabase.MY_PREFERENCES, Context.MODE_PRIVATE);
        String p_ip = prefs.getString(ConfDatabase.IP_KEY, "");
        if(p_ip != null)
        	ConfDatabase.CURRENT_IP_KEY = p_ip;
    }    	
}
