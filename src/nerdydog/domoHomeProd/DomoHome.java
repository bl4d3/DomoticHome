package nerdydog.domoHomeProd;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

import nerdydog.domoHomeProd.db.ConfDatabase;
import nerdydog.domoHomeProd.db.ToDoDBAdapter;
import nerdydog.domoHomeProd.json.ParseJSON;
import nerdydog.domoHomeProd.object.Action;
import nerdydog.domoHomeProd.object.Actuator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class DomoHome extends Activity{

	ToDoDBAdapter toDoDBAdapter;
	ListView listViewType;
	TextView infoTextView;
	private TypeAdapter m_adapterType;
	
	private final String TAG = "DomoHome"; 
	
    // menu
    public boolean onCreateOptionsMenu(Menu menu){return Utility.showMenu(menu, getMenuInflater());}
    public boolean onOptionsItemSelected (MenuItem item){return Utility.choiceMenu( item, DomoHome.this);}
    // end menu			
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Utility.getIpFromPreferencesData(this);
        
        listViewType = (ListView)findViewById(R.id.listViewType);
        infoTextView = (TextView)findViewById(R.id.domohometextview);
        
        toDoDBAdapter = new ToDoDBAdapter(this);
        toDoDBAdapter.open();
        
        /*toDoDBAdapter.dropTableAction();
        toDoDBAdapter.dropTableActuator();
        toDoDBAdapter.dropTableCounter();*/
        
        
        // get all the actions
        ArrayList<Action> actions = toDoDBAdapter.getAllAction(
        		ConfDatabase.ACTION_PARENT_ID + "=" + "?", new String[]{"-1"}, null);
        
        ArrayList<Actuator> actuatorspark = toDoDBAdapter.getAllActuators(null, null, ConfDatabase.ACTUATOR_TYPE);
        
        if(actions.size()>0){
        	Date d = new Date();
        	Actuator afake = new Actuator(-1, "", "", ConfDatabase.TYPE_ACTION, "Actions", -1, d);
        	actuatorspark.add(afake);
        }
        
        if(actuatorspark.size()>0)
        {
	        infoTextView.setText("Pick up an item");
	        
	    	this.m_adapterType = new TypeAdapter(this, R.layout.rowtype, actuatorspark);
	    	listViewType.setAdapter(m_adapterType);
	    	
	       
	        listViewType.setOnItemClickListener(listClicked);
	        
	        
	        // check data into the db
	        ArrayList actuators = toDoDBAdapter.getAllActuators(null,null,null);
	        Log.i(TAG, Integer.toString(actuators.size()));
	        
	        // get all the meter's actuator  
	        /*ConfDatabase.aryActuatorsForMeter = toDoDBAdapter.getAllActuators(
	        		ConfDatabase.ACTUATOR_TYPE + "=" + "?" + " AND " + ConfDatabase.ACTUATOR_TYPE + "=" + , new String[]{ConfDatabase.TYPE_TEMPERATURE}, null);*/
	       
	        ConfDatabase.aryActuatorsForMeter = toDoDBAdapter.getAllActuators(
	        		ConfDatabase.ACTUATOR_TYPE + " IN ('" + new String(ConfDatabase.TYPE_TEMPERATURE) + "','" + new String(ConfDatabase.TYPE_WATTMETER) + "')" ,null, null);
	        
	        toDoDBAdapter.close();       
	        
	        // start data service
	        Intent service=new Intent(getApplicationContext(), DataService.class);
	        startService(service);        
        }else{
        	infoTextView.setText("Attention! You should sync DomoticHome with your Arduino.");
        }
        
        
    }

	
	private View.OnLongClickListener listLongClicked = new View.OnLongClickListener() {
		
		public boolean onLongClick(View arg0) {
			Log.i(TAG, "long click!");
			return false;
		}
	};
	
	
    private OnItemClickListener listClicked = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
			
			Log.i(TAG, " clicked");
			Actuator actuator = (Actuator)v.getTag();
			if(actuator != null && actuator.getType() != ConfDatabase.TYPE_ACTION){
				Intent i = new Intent(DomoHome.this, ActuatorFunction.class);
				i.putExtra("idList", position);
				startActivity(i);				
			}else if (actuator.getType() == ConfDatabase.TYPE_ACTION){
				Intent i = new Intent(DomoHome.this, ActionShow.class);
				startActivity(i);				
				Log.i(TAG, "We get an action");
			}else{
				Log.i(TAG, "Something wrong");
			}
		}
    };   

}