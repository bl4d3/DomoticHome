package nerdydog.domoHomeProd;

import java.util.ArrayList;
import java.util.List;

import nerdydog.domoHomeProd.db.ConfDatabase;
import nerdydog.domoHomeProd.object.Actuator;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DnDAdapter extends BaseAdapter {
	
	private static List<Actuator> items = null;
	private Context context = null;
	
	public static View emptyView = null;
	
	private static Actuator emptyEntry = ConfDatabase.aryActuatorsSelectedForActions.get(0);
	
	public DnDAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
//		buildList();
	}
	
	
	
	public List<Actuator> getItems() {
		return items;
	}
	
	public void setList(List<Actuator> items) {
		this.items = items;
	}

	
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Actuator entry = items.get(position);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view = inflater.inflate(R.layout.orderdomoitemadapter, parent, false);
		
		view.setTag(entry);
		
		TextView txt = (TextView)view.findViewById(R.id.typeorderdomoinfo1);
		
		txt.setText(entry.getName());
		
		ImageView imageType = (ImageView)view.findViewById(R.id.iconorderdomoitem);
        if(entry.getType().equals(new String(ConfDatabase.TYPE_GATE)))
       	 imageType.setImageResource(R.drawable.gate);
        if(entry.getType().equals(new String(ConfDatabase.TYPE_DOOR)))
       	 imageType.setImageResource(R.drawable.door);
        if(entry.getType().equals(new String(ConfDatabase.TYPE_WATTMETER)))
       	 imageType.setImageResource(R.drawable.lightning);
        if(entry.getType().equals(new String(ConfDatabase.TYPE_LIGHT)))
       	 imageType.setImageResource(R.drawable.light);
        if(entry.getType().equals(new String(ConfDatabase.TYPE_PLUG)))
       	 imageType.setImageResource(R.drawable.eletric);
        if(entry.getType().equals(new String(ConfDatabase.TYPE_TEMPERATURE)))
       	 imageType.setImageResource(R.drawable.temperature);
        if(entry.getType().equals(new String(ConfDatabase.TYPE_ACTION)))
          	 imageType.setImageResource(R.drawable.actions);	        
		
		final ImageView img = (ImageView)view.findViewById(R.id.iconorderdomoitem);
		
		img.setOnTouchListener(new OnTouchListener() {
			
			
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				Log.v(getClass().toString(),"Icon onTouch. Selected position: " + position);
				
				DragAndDrop.listView.moveEntry = items.get(position);
				emptyEntry = items.get(position);
				buildEmptyView(DragAndDrop.listView.moveEntry);
				items.remove(position);
				items.add(position, emptyEntry);
				DragAndDrop.listView.invalidateViews();
				DragAndDrop.listView.setEnabled(false);
				DragAndDrop.listView.isMoveFlag = true;
				DragAndDrop.listView.moveView = view;
				DragAndDrop.listView.movedPosition = position;
				return false;
			}
		});
		if (entry.getName().equals("Empty entry")) {
			img.setVisibility(View.INVISIBLE);
			txt.setVisibility(View.INVISIBLE);
		}
		return view;
	}

	private void buildEmptyView(Actuator moveentry) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//emptyView = inflater.inflate(R.layout.emptyview, null, false);
		emptyView = inflater.inflate(R.layout.orderdomoitemadapter, null, false);
		TextView txt = (TextView)emptyView.findViewById(R.id.typeorderdomoinfo1);
		txt.setText(moveentry.getName());
		txt.setTextColor(Color.WHITE);
	}
	
	public static void addEntry(int pos) {
		items.add(pos, DragAndDrop.listView.moveEntry);
		DragAndDrop.listView.invalidateViews();
	}
	
	public static void addEmptyEntry(int pos) {
		items.add(pos, emptyEntry);
		DragAndDrop.listView.invalidateViews();
	}
	
	public static void removeEmptyEntry() {
		items.remove(emptyEntry);
		DragAndDrop.listView.invalidateViews();
	}
}
