package nerdydog.domoHomeProd;

import java.util.ArrayList;

import nerdydog.domoHomeProd.db.ConfDatabase;
import nerdydog.domoHomeProd.db.ToDoDBAdapter;
import nerdydog.domoHomeProd.object.Actuator;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TypeAdapter extends ArrayAdapter<Actuator> {
	 private ArrayList<Actuator> items;
	 String TAG = "TypeAdapter";
	 String actuatorName = "";
	 ProgressDialog dialogLoading;
	 Context c;

     public TypeAdapter(Context context, int textViewResourceId, ArrayList<Actuator> items) {
             super(context, textViewResourceId, items);
             c = context;
             this.items = items;
     }

     public View getView(int position, View convertView, ViewGroup parent) {
         	 View v = convertView;
    	 	 if( c != null){
	             if (v == null) {
	                 LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                 v = vi.inflate(R.layout.rowtype, null);
	             }
	
	             Actuator a = items.get(position);
	             if (a != null) {
	                     TextView typeinfo1 = (TextView) v.findViewById(R.id.typeinfo1);
	                     ImageView imageType = (ImageView) v.findViewById(R.id.icontype);
	                     
	                     if(a.getType().equals(new String(ConfDatabase.TYPE_GATE)))
	                    	 imageType.setImageResource(R.drawable.gate);
	                     if(a.getType().equals(new String(ConfDatabase.TYPE_DOOR)))
	                    	 imageType.setImageResource(R.drawable.door);
	                     if(a.getType().equals(new String(ConfDatabase.TYPE_WATTMETER)))
	                    	 imageType.setImageResource(R.drawable.lightning);
	                     if(a.getType().equals(new String(ConfDatabase.TYPE_LIGHT)))
	                    	 imageType.setImageResource(R.drawable.light);
	                     if(a.getType().equals(new String(ConfDatabase.TYPE_PLUG)))
	                    	 imageType.setImageResource(R.drawable.eletric);
	                     if(a.getType().equals(new String(ConfDatabase.TYPE_TEMPERATURE)))
	                    	 imageType.setImageResource(R.drawable.temperature);
	                     if(a.getType().equals(new String(ConfDatabase.TYPE_ACTION)))
	                      	 imageType.setImageResource(R.drawable.actions);	    
	                     if(a.getType().equals(new String(ConfDatabase.TYPE_SOFA)))
	                      	 imageType.setImageResource(R.drawable.sofa);
	                     if(a.getType().equals(new String(ConfDatabase.TYPE_SOFA_2M_FEET)))
	                      	 imageType.setImageResource(R.drawable.sofa);
	                     if(a.getType().equals(new String(ConfDatabase.TYPE_SOFA_1M_FEET)))
	                      	 imageType.setImageResource(R.drawable.sofa);
	                     if(a.getType().equals(new String(ConfDatabase.TYPE_IRRIGATION)))
	                      	 imageType.setImageResource(R.drawable.irrigation);
	                     if(a.getType().equals(new String(ConfDatabase.TYPE_WINDOW)))
	                      	 imageType.setImageResource(R.drawable.window);	
	                     
	                     if (typeinfo1 != null) {
	                    	 ToDoDBAdapter toDoDBAdapter = new ToDoDBAdapter(c);
	                    	 toDoDBAdapter.open();
	                    	 if(a.getType() == ConfDatabase.TYPE_ACTION)
	                    		 typeinfo1.setText(a.getType());
	                         else
	                        	 typeinfo1.setText(a.getType() + " (" + toDoDBAdapter.getAllActuators(ConfDatabase.ACTUATOR_TYPE + "=" + "?", new String[]{a.getType()}, null).size() + ")");
	                     
	                    	 toDoDBAdapter.close();
	                     }
	                     v.setTag(a);
	             }
    	 	 }
             return v;
     }
}
