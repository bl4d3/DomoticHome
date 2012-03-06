package nerdydog.domoHomeProd;

import java.util.ArrayList;
import java.util.ListIterator;

import nerdydog.domoHomeProd.db.ConfDatabase;
import nerdydog.domoHomeProd.db.ToDoDBAdapter;
import nerdydog.domoHomeProd.object.Actuator;
import nerdydog.domoHomeProd.object.Counter;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ActuatorFunction extends Activity{
	
	int id;
	String TAG = "ActuatorFunction";
	ToDoDBAdapter toDoDBAdapter;
	ListView listViewActuator;
	private ActuatorAdapter m_adapterAcutator;
	private MeterAdapter m_adapterMeter;
	
    // menu
    public boolean onCreateOptionsMenu(Menu menu){return Utility.showMenu(menu, getMenuInflater());}
    public boolean onOptionsItemSelected (MenuItem item){return Utility.choiceMenu( item, ActuatorFunction.this);}
    // end menu		
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actuator);
        
        listViewActuator = (ListView)findViewById(R.id.listViewActuator);
        
		Bundle extras = getIntent().getExtras();
		if(extras != null){
	        id = extras.getInt("idList");
	        Log.i(TAG, Integer.toString(id));
	        
	        toDoDBAdapter = new ToDoDBAdapter(this);
	        toDoDBAdapter.open();
	        // get the type
	        ArrayList<Actuator> aryActuators = toDoDBAdapter.getAllActuators(null, null, ConfDatabase.ACTUATOR_TYPE);
	        Actuator currentActuator = aryActuators.get(id);
	        // get all the actuator for this type	        
	        ArrayList<Actuator> aryActuatorsForType = toDoDBAdapter.getAllActuators(ConfDatabase.ACTUATOR_TYPE + "=" + "?", new String[]{currentActuator.getType()}, null);
	        
	        if(aryActuatorsForType.size()>0){
		        if(aryActuatorsForType.get(0).isMeter()){
		        	this.m_adapterMeter = new MeterAdapter(this, R.layout.rowmeter, aryActuatorsForType);
		        	listViewActuator.setAdapter(m_adapterMeter);
		        	Log.i(TAG, "meter");
		        	
	            	for( int i = 0; i < aryActuatorsForType.size(); i++){
	            		Counter counter = toDoDBAdapter.getLast(aryActuatorsForType.get(i).getId());
	            		String value = "";
	            		if(counter != null)
	            			value = counter.getValue();
	            		else
	            			value = "27,82";
	            	}
	            	    
		        	
		        }else{
		        	this.m_adapterAcutator = new ActuatorAdapter(this, R.layout.rowactuator, aryActuatorsForType);
		        	listViewActuator.setAdapter(m_adapterAcutator);
		        	Log.i(TAG, "actuator");
		        }
	        }
	        
	        toDoDBAdapter.close();
		}
	}
	


}
