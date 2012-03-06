package nerdydog.domoHomeProd;

import java.net.URL;
import java.util.ArrayList;


import nerdydog.domoHomeProd.db.ConfDatabase;
import nerdydog.domoHomeProd.db.ToDoDBAdapter;
import nerdydog.domoHomeProd.object.Actuator;
import nerdydog.domoHomeProd.object.Counter;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MeterAdapter extends ArrayAdapter<Actuator> {
	 private ArrayList<Actuator> items;
	 String TAG = "ActuatorAdapter";
	 String actuatorName = "";
	 ProgressDialog dialogLoading;
	 Context c;
	 boolean goon = true;

     public MeterAdapter(Context context, int textViewResourceId, ArrayList<Actuator> items) {
             super(context, textViewResourceId, items);
             c = context;
             this.items = items;
     }

     public View getView(int position, View convertView, ViewGroup parent) {
             View v = convertView;
             if (v == null) {
                 LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                 v = vi.inflate(R.layout.rowmeter, null);
             }

             final Actuator a = items.get(position);
             if (a != null) {
            	 	 ToDoDBAdapter toDoDBAdapter = new ToDoDBAdapter(c);
	    	         toDoDBAdapter.open();
            	     
	    	         Counter counter = toDoDBAdapter.getLast(a.getId());
            	     
                     TextView tinfo1 = (TextView) v.findViewById(R.id.meterinfo1);
                     
                     TextView tmeter = (TextView) v.findViewById(R.id.meter);
                     ImageView icon = (ImageView)v.findViewById(R.id.icon);
                     
                     if (tinfo1 != null) {
                    	 tinfo1.setText(a.getName());                            
                     }

                	 if(a.getType().equals(new String(ConfDatabase.TYPE_TEMPERATURE)))
                		 tmeter.setText(counter.getValue() + "¡C");
                	 else if(a.getType().equals(new String(ConfDatabase.TYPE_WATTMETER))){
                		 Log.i(TAG, "counter.getValue() " + counter.getValue());
                		 Log.i(TAG, "ConfDatabase.CURRENT_POWER_ADJ_KEY " + ConfDatabase.CURRENT_POWER_ADJ_KEY);
                		 float v_w = 0;
                		 if(ConfDatabase.CURRENT_POWER_ADJ_KEY!=null && ConfDatabase.CURRENT_POWER_ADJ_KEY!="0"){
                			 v_w = Float.parseFloat(counter.getValue())/Float.parseFloat(ConfDatabase.CURRENT_POWER_ADJ_KEY);
                		 }
                		 tmeter.setText(String.valueOf(v_w) + " W");
                	 }else
                		 tmeter.setText(counter.getValue());
                     
                     
                     toDoDBAdapter.close();
                     
                     // TODO other meter...
                     if(a.getType().equals(new String(ConfDatabase.TYPE_TEMPERATURE)))
                    	 icon.setImageResource(R.drawable.temperature);
                     else if(a.getType().equals(new String(ConfDatabase.TYPE_WATTMETER)))
                    	 icon.setImageResource(R.drawable.lightning);                 
                     
                     // hide close button for the actuator that does not support this functions
                     Log.i(TAG, "bool "+a.isSingleButton() + " " + a.getType());
                     
             }
             return v;
     }
     

	
	
	
}
