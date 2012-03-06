package nerdydog.domoHomeProd;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ListIterator;

import nerdydog.domoHomeProd.db.ConfDatabase;
import nerdydog.domoHomeProd.db.ToDoDBAdapter;
import nerdydog.domoHomeProd.json.ParseJSON;
import nerdydog.domoHomeProd.object.Action;
import nerdydog.domoHomeProd.object.Actuator;
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
		case R.id.menuhome:
			Intent ihome = new Intent(l, DomoHome.class);
			l.startActivity(ihome);			
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
			
			//fill up all the ips
			ArrayList<String> ips = new ArrayList<String>();
			//ips.add(ConfDatabase.CURRENT_IP_KEY);
			
			String ip_start = new String(ConfDatabase.CURRENT_IP_KEY);
			String ip_end = new String(ConfDatabase.CURRENT_IP_END_KEY);
			
			Log.i(TAG, "s ip "+ ip_start + " e ip " + ip_end);
			
			String[] ary_start = ip_start.split("\\.");
			String[] ary_end = ip_end.split("\\.");
			
			int start=0, end=0;
			String base_ip = "";
			
			
			base_ip = ary_start[0] + "." + ary_start[1] + "." + ary_start[2] + ".";
			
			start = Integer.parseInt(ary_start[3])-1;
			end = Integer.parseInt(ary_end[3]);
			
			Log.i(TAG, "base " + base_ip + " start " + start + " end " + end);
			
			for(int i=start; i<=end; i++){
				Log.i(TAG, "ip " + base_ip + i);
				ips.add(base_ip + i);
			}
			
			
			
			//ips.add("192.168.10.16");
			//ips.add("192.168.10.19");
			//ips.add("192.168.10.22");
			
			// insert actuators into the db
			ArrayList<Actuator> aryActuators = ParseJSON.getAllActuator(ips);
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
				Thread.sleep(100);	
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    public static void getIpFromPreferencesData(Context c){
    	
        SharedPreferences prefs = c.getSharedPreferences(ConfDatabase.MY_PREFERENCES, Context.MODE_PRIVATE);
        String p_ip = prefs.getString(ConfDatabase.IP_KEY, "");
        String p_ip_end = prefs.getString(ConfDatabase.IP_END_KEY, "");
        String p_power_adj = prefs.getString(ConfDatabase.IP_POWER_ADJ_KEY, "");
        if(p_ip != null){
        	ConfDatabase.CURRENT_IP_KEY = p_ip;
        	ConfDatabase.CURRENT_IP_END_KEY = p_ip_end;
        	ConfDatabase.CURRENT_POWER_ADJ_KEY = p_power_adj;
        }
    }    	
    
    public static String getScheduledDays(String schedul){
    	String days = "";
		if(String.valueOf(schedul.charAt(0)).equals(new String("1"))){
			days = days + "M";
		}else{ days = days + "-";}
		if(String.valueOf(schedul.charAt(1)).equals(new String("1"))){
			days = days + "T";
		}else{ days = days + "-";}
		if(String.valueOf(schedul.charAt(2)).equals(new String("1"))){
			days = days + "W";
		}else{ days = days + "-";}
		if(String.valueOf(schedul.charAt(3)).equals(new String("1"))){
			days = days + "T";
		}else{ days = days + "-";}
		if(String.valueOf(schedul.charAt(4)).equals(new String("1"))){
			days = days + "F";
		}else{ days = days + "-";}
		if(String.valueOf(schedul.charAt(5)).equals(new String("1"))){
			days = days + "S";
		}else{ days = days + "-";}
		if(String.valueOf(schedul.charAt(6)).equals(new String("1"))){
			days = days + "S";
		}else{ days = days + "-";}
    	return days;
    }
    
    public static boolean is_day_to_trigger(String s_compare){
    	Date dateNow = new Date();
    	Log.i(TAG, "-- " + dateNow.getDay() + " " + Calendar.SUNDAY);
    	if(dateNow.getDay()+1 == Calendar.MONDAY){
    		if(String.valueOf(s_compare.charAt(0)).equals(new String("1"))){
    			return true;
    		} 
    	}
    	if(dateNow.getDay()+1 == Calendar.TUESDAY){
    		if(String.valueOf(s_compare.charAt(1)).equals(new String("1"))){
    			return true;
    		} 
    	}
    	if(dateNow.getDay()+1 == Calendar.WEDNESDAY){
    		if(String.valueOf(s_compare.charAt(2)).equals(new String("1"))){
    			return true;
    		} 
    	}
    	if(dateNow.getDay()+1 == Calendar.THURSDAY){
    		if(String.valueOf(s_compare.charAt(3)).equals(new String("1"))){
    			return true;
    		} 
    	}
    	if(dateNow.getDay()+1 == Calendar.FRIDAY){
    		if(String.valueOf(s_compare.charAt(4)).equals(new String("1"))){
    			Log.i(TAG, "c " + s_compare.charAt(4) + " " + String.valueOf(s_compare.charAt(4)).equals(new String("1")));
    			return true;
    		} 
    	}
    	if(dateNow.getDay()+1 == Calendar.SATURDAY){
    		if(String.valueOf(s_compare.charAt(5)).equals(new String("1"))){
    			return true;
    		} 
    	}
    	if(dateNow.getDay()+1 == Calendar.SUNDAY){
    		if(String.valueOf(s_compare.charAt(6)).equals(new String("1"))){
    			return true;
    		} 
    	}
    	return false;
    }
    
    public static void updateActionTriggered(Context c){
    	ToDoDBAdapter toDoDBAdapter = new ToDoDBAdapter(c);
    	toDoDBAdapter.open();
    	ArrayList<Action> actions = toDoDBAdapter.getAllAction(null, null, null);

    	Calendar calNow = Calendar.getInstance();
    	//calNow.add(Calendar.DATE, -1);
    	//Log.i(TAG, " NOW " + calNow.get(Calendar.YEAR) +" "+ calNow.get(Calendar.MONTH) +" "+ calNow.get(Calendar.DATE));
    	
    	
    	for(int i = 0; i < actions.size(); i++){
    		Date created_at = actions.get(i).getCreated_at();
    		Calendar calAction = Calendar.getInstance();
    		Log.i(TAG, "action date " + created_at.toLocaleString() + " " + created_at.getYear() +" "+ created_at.getMonth() +" "+ created_at.getDay());
    		calAction.setTime(created_at);
    		
    		//Log.i(TAG, "calAction " + calAction.getTime().toLocaleString());
    		//Log.i(TAG, " calAction " + calAction.get(Calendar.YEAR) +" "+ calAction.get(Calendar.MONTH) +" "+ calAction.get(Calendar.DATE));
    		
    		if(calAction.get(Calendar.YEAR) == calNow.get(Calendar.YEAR)
    			&& calAction.get(Calendar.MONTH) == calNow.get(Calendar.MONTH) 
    			&& calAction.get(Calendar.DATE) == calNow.get(Calendar.DATE) ){
    			// TODO update status and date creation
    			toDoDBAdapter.updateAction(actions.get(i), ConfDatabase.ACTION_POS, "0");
    			toDoDBAdapter.updateAction(actions.get(i), ConfDatabase.ACTION_CREATED_AT, new Date());
    			Log.i(TAG,"the same");
    		}
    	}
    	toDoDBAdapter.close();
    }
    

}
