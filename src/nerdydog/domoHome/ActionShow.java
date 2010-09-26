package nerdydog.domoHome;

import java.util.ArrayList;

import nerdydog.domoHome.db.ConfDatabase;
import nerdydog.domoHome.db.ToDoDBAdapter;
import nerdydog.domoHome.object.Action;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class ActionShow extends Activity{
	
	ToDoDBAdapter toDoDBAdapter;
	ListView listViewAction;
	TextView textViewAction;
	private ActionShowAdapter m_adapterType;
	
	private final String TAG = "DomoHome"; 
	
    // menu
    public boolean onCreateOptionsMenu(Menu menu){return Utility.showMenu(menu, getMenuInflater());}
    public boolean onOptionsItemSelected (MenuItem item){return Utility.choiceMenu( item, ActionShow.this);}
    // end menu			
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actionshow);
        listViewAction = (ListView)findViewById(R.id.listviewactionshow);
        textViewAction = (TextView)findViewById(R.id.actiontextview);
        
        toDoDBAdapter = new ToDoDBAdapter(this);
        toDoDBAdapter.open();
        // get all the actions
        ArrayList<Action> actions = toDoDBAdapter.getAllAction(
        		ConfDatabase.ACTION_PARENT_ID + "=" + "?", new String[]{"-1"}, null);
        
        if(actions.size()>0){
	    	this.m_adapterType = new ActionShowAdapter(this, R.layout.actionshowadapter, actions);
	    	listViewAction.setAdapter(m_adapterType);
        }else{
        	textViewAction.setText("Attention! No actions available.");
        }

        toDoDBAdapter.close();
        
    }
 
}
