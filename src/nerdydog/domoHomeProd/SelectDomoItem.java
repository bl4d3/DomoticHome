package nerdydog.domoHomeProd;

import java.util.ArrayList;

import nerdydog.domoHomeProd.db.ConfDatabase;
import nerdydog.domoHomeProd.db.ToDoDBAdapter;
import nerdydog.domoHomeProd.object.Actuator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class SelectDomoItem extends Activity{
	ToDoDBAdapter toDoDBAdapter;
	ListView listViewSelectDomoItem;
	Button buttonNext;
	private SelectDomoItemAdapter m_adapterType;
	ArrayList<Actuator> aryActuators;
	private final String TAG = "SelectDomoItem"; 
	Context c = null;
	
    // menu
    public boolean onCreateOptionsMenu(Menu menu){return Utility.showMenu(menu, getMenuInflater());}
    public boolean onOptionsItemSelected (MenuItem item){return Utility.choiceMenu( item, SelectDomoItem.this);}
    // end menu		
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectdomoitem);
        c = this;
        listViewSelectDomoItem = (ListView)findViewById(R.id.listViewselectdomoitem);
        buttonNext = (Button)findViewById(R.id.buttonnext);
        
        ConfDatabase.aryActuatorsSelectedForActions.clear();
        
        toDoDBAdapter = new ToDoDBAdapter(this);
        toDoDBAdapter.open();
        
        aryActuators = toDoDBAdapter.getAllActuators(ConfDatabase.ACTUATOR_TYPE + "!=" + "? AND " + ConfDatabase.ACTUATOR_TYPE + "!=" + "? AND " + ConfDatabase.ACTUATOR_TYPE + "!=" + "? AND " + ConfDatabase.ACTUATOR_TYPE + "!=" + "? AND " + ConfDatabase.ACTUATOR_TYPE + "!=" + "?"+ " AND " + ConfDatabase.ACTUATOR_TYPE + "!=" + "?"+ " AND " + ConfDatabase.ACTUATOR_TYPE + "!=" + "?" + " AND " + ConfDatabase.ACTUATOR_TYPE + "!=" + "?", 
        		new String[]{ConfDatabase.TYPE_TEMPERATURE, ConfDatabase.TYPE_WATTMETER, ConfDatabase.TYPE_ACTION, ConfDatabase.TYPE_HUMIDITY, ConfDatabase.TYPE_LIGHT, ConfDatabase.TYPE_PLUG, ConfDatabase.TYPE_SOFA, ConfDatabase.TYPE_IRRIGATION}, 
        		null);        
        
    	this.m_adapterType = new SelectDomoItemAdapter(this, R.layout.selectdomoitemadpter, aryActuators);
    	listViewSelectDomoItem.setAdapter(m_adapterType);
    	listViewSelectDomoItem.addFooterView(buttonNext);
    	buttonNext.setOnClickListener(clickedNext);
    	
    	toDoDBAdapter.close();
    }
    
    
    @Override
    public void onRestart(){
    	super.onRestart();
    	ConfDatabase.aryActuatorsSelectedForActions.clear();
    	
    	Intent intent = new android.content.Intent();
        intent.setClass(this, this.getClass());  //We will start the activuty itself
        startActivity(intent);
    	Log.i(TAG, "clear actions ary");
    }        
        
    View.OnClickListener clickedNext = new View.OnClickListener() {
		
		public void onClick(View arg0) {
			if(ConfDatabase.aryActuatorsSelectedForActions != null && ConfDatabase.aryActuatorsSelectedForActions.size()>0)
			{
				Intent i = new Intent(SelectDomoItem.this, DragAndDrop.class);
				startActivity(i);	
			}else{
				Log.i(TAG, "no item selected");
				Utility.showDialog(c, "Please, select at least one item.");
			}
		}
	};
}
