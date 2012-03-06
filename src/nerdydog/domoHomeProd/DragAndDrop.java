package nerdydog.domoHomeProd;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nerdydog.domoHomeProd.db.ConfDatabase;
import nerdydog.domoHomeProd.db.ToDoDBAdapter;
import nerdydog.domoHomeProd.object.Action;
import nerdydog.domoHomeProd.object.Actuator;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


public class DragAndDrop extends ListActivity {
    /** Called when the activity is first created. */
	
	
	public static LinearLayout layout = null;
	public static ScrollView scrollView = null;
	public static DnDListView listView = null;
	String TAG = "DragAndDrop";
	Button buttonaction;
	ToDoDBAdapter toDoDBAdapter;
	Context c = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maindnd);
        c = this;
        toDoDBAdapter = new ToDoDBAdapter(this);
        
        DnDAdapter adapter = new DnDAdapter(this);
        adapter.setList(buildList());
        listView = (DnDListView)getListView();
        listView.setAdapter(adapter);
        
        buttonaction = (Button)findViewById(R.id.buttosetaction);
        buttonaction.setOnClickListener(clickedaction);
        listView.addFooterView(buttonaction);
        
    }
    
    private List<Actuator> buildList() {
		List<Actuator> items = ConfDatabase.aryActuatorsSelectedForActions;
		
		return items;
	}
    
    View.OnClickListener clickedaction = new View.OnClickListener() {
		
		public void onClick(View arg0) {
			final int start = listView.getFirstVisiblePosition();
			final int end = listView.getLastVisiblePosition();
			
			
			
			Log.i(TAG, "start " + start + " end " + end);
			
			final Dialog dialog = new Dialog(DragAndDrop.this);
            dialog.setContentView(R.layout.popupactionname);
            dialog.setTitle("Insert the action name");
            dialog.setCancelable(true);	
            
            dialog.show();
            
            final EditText actionName = (EditText)dialog.findViewById(R.id.edittextactionname);
            
            //set up button
            Button button = (Button) dialog.findViewById(R.id.buttonsaveactionname);
            button.setOnClickListener(new View.OnClickListener() {
            
                public void onClick(View v) {
            		Actuator a_parent = null;
            		Log.i(TAG, "name: " + actionName.getText());
            		String actionNameString = actionName.getText().toString();
                    dialog.dismiss();
                    toDoDBAdapter.open();
                    
                    if(actionNameString!=null && actionNameString.length()>0){
                    	int root_id_master = -1;
                    	for(int i = start; i <= end; i++){
                    		View v_root = listView.getChildAt(i);
            				Actuator a_root = (Actuator) v_root.getTag();
            				Log.i(TAG, "start " + start);
            				Log.i(TAG, "a_root id " + a_root.getId());
            				
            				if( i > start){
            					View v_parent = listView.getChildAt(i-1);
            					if( v_parent != null){
            						a_parent = (Actuator) v_parent.getTag();
            					}
            				}
            				
            				Date dateNow = new Date();
            				Action action;
            				// TODO fix commented rows
            				if(a_parent != null){
            					action = new Action(-1, a_root.getId(), a_parent.getId(), dateNow, dateNow, dateNow, actionNameString, 1, root_id_master, 0, -1, "0000000");
            					toDoDBAdapter.insertAction(action);
            				}else{
            					action = new Action(-1, a_root.getId(), -1, dateNow, dateNow, dateNow, actionNameString, 1, -1, 0, -1, "0000000");
            					toDoDBAdapter.insertAction(action);
            					Action lastAction = toDoDBAdapter.getLastAction();
            					
            					root_id_master = lastAction.getId();
            					Log.i(TAG, "a_root id create " + root_id_master);
            				}
                    	}
                    	toDoDBAdapter.close();
        				Intent iHome = new Intent(DragAndDrop.this, DomoHome.class);
        				startActivity(iHome);
        				
                    }else{
                    	Utility.showDialog(c, "Please, choose a name for this action.");
                    }
                }
            });            
			
		}
	};
    
    
}