package nerdydog.domoHomeProd;

import java.util.ArrayList;

import nerdydog.domoHomeProd.db.ConfDatabase;
import nerdydog.domoHomeProd.object.Actuator;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class SelectDomoItemAdapter extends ArrayAdapter<Actuator> {
	 private ArrayList<Actuator> items;
	 String TAG = "SelectDomoItemAdapter";
	 String actuatorName = "";
	 ProgressDialog dialogLoading;
	 Context c;
	 CheckBox checkbox;
	 
     public SelectDomoItemAdapter(Context context, int textViewResourceId, ArrayList<Actuator> items) {
         super(context, textViewResourceId, items);
         c = context;
         this.items = items;
     }
     
     public View getView(int position, View convertView, ViewGroup parent) {
    	 View v = convertView;
         if (v == null) {
             LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
             v = vi.inflate(R.layout.selectdomoitemadpter, null);
         }
         
         Actuator a = items.get(position);
         if(a != null){
             TextView typeinfo1 = (TextView) v.findViewById(R.id.typeselectdomoinfo1);
             ImageView imageType = (ImageView) v.findViewById(R.id.iconselectdomoitem);
             checkbox = (CheckBox) v.findViewById(R.id.checkboxselectdomoitem);
             checkbox.setTag(a);
             checkbox.setChecked(false);
             checkbox.setOnCheckedChangeListener(clickedCheckbox);
             
             
             typeinfo1.setText(a.getName());
             
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
         }
    	 return v;
     }
     
     // checked box change status
     CompoundButton.OnCheckedChangeListener clickedCheckbox = new CompoundButton.OnCheckedChangeListener() {
		
		public void onCheckedChanged(CompoundButton mycheckbox, boolean isChecked) {
			Actuator a = (Actuator)mycheckbox.getTag();
			if( a != null){
				if(isChecked){
					if(ConfDatabase.aryActuatorsSelectedForActions.indexOf(a)<0)
					{
						ConfDatabase.aryActuatorsSelectedForActions.add(a);
					}
				}else{
					ConfDatabase.aryActuatorsSelectedForActions.remove(a);
				}
				Log.i(TAG, "size " + ConfDatabase.aryActuatorsSelectedForActions.size());
			}
				
			}
	};

}
