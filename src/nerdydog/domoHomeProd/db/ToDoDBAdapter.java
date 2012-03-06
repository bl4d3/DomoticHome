package nerdydog.domoHomeProd.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import nerdydog.domoHomeProd.object.Action;
import nerdydog.domoHomeProd.object.Actuator;
import nerdydog.domoHomeProd.object.Counter;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.ParseException;
import android.util.Log;



public class ToDoDBAdapter {
	
	final static String TAG = "ToDoDBAdapter";
	
	private final Context context;
	private toDoDBOpenHelper dbHelper;
	private SQLiteDatabase db;
	
	public ToDoDBAdapter(Context _context) {
		this.context = _context;
		dbHelper = new toDoDBOpenHelper(context, ConfDatabase.DATABASE_NAME, null, ConfDatabase.DATABASE_VERSION);
	}
	
	public void close() {
		db.close();
	}	
	
	public void open() throws SQLiteException {
		
		try {
			db = dbHelper.getWritableDatabase();
		} catch (SQLiteException ex) {
			db = dbHelper.getReadableDatabase();
		}
		/*db.execSQL(ConfDatabase.DATABASE_CREATE_TABLE_ACTUATOR);
		db.execSQL(ConfDatabase.DATABASE_CREATE_TABLE_COUNTER);
		db.execSQL(ConfDatabase.DATABASE_CREATE_TABLE_ACTION);*/
	}
	
	// ------------------------------------------------------------------
	// insert a new action
	public long insertAction(Action action){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ContentValues newTaskValues = new ContentValues();
		newTaskValues.put(ConfDatabase.ACTION_DOMO_ID, action.getDomo_id());
		newTaskValues.put(ConfDatabase.ACTION_PARENT_ID, action.getParent_id());
		newTaskValues.put(ConfDatabase.ACTION_STARTTIME, dateFormat.format(action.getStarttime()));
		newTaskValues.put(ConfDatabase.ACTION_ENDTIME, dateFormat.format(action.getEndtime()));
		newTaskValues.put(ConfDatabase.ACTION_CREATED_AT, dateFormat.format(action.getCreated_at()));
		newTaskValues.put(ConfDatabase.ACTION_NAME, action.getName());
		newTaskValues.put(ConfDatabase.ACTION_DELAY, action.getDelay());
		newTaskValues.put(ConfDatabase.ACTION_ROOT_ID, action.getRoot_id());
		newTaskValues.put(ConfDatabase.ACTION_IS_TRIGGER, action.getIs_trigger());
		newTaskValues.put(ConfDatabase.ACTION_POS, action.getPos());
		newTaskValues.put(ConfDatabase.ACTION_SCHEDULED, action.getScheduled());
		return db.insert(ConfDatabase.DATABASE_TABLE_ACTION, null, newTaskValues);
	}
	// clear counter table
	public boolean clearAction() {
		return db.delete(ConfDatabase.DATABASE_TABLE_ACTION, null, null) > 0;
	}
	
	public Action getLastAction() {		
		Cursor cursor = db.query(false, ConfDatabase.DATABASE_TABLE_ACTION,
				new String[] { ConfDatabase.ACTION_ID, ConfDatabase.ACTION_DOMO_ID, ConfDatabase.ACTION_PARENT_ID, ConfDatabase.ACTION_STARTTIME, ConfDatabase.ACTION_ENDTIME, ConfDatabase.ACTION_CREATED_AT, ConfDatabase.ACTION_NAME, ConfDatabase.ACTION_DELAY, ConfDatabase.ACTION_ROOT_ID, ConfDatabase.ACTION_IS_TRIGGER, ConfDatabase.ACTION_POS, ConfDatabase.ACTION_SCHEDULED},
				null, null, null, null, ConfDatabase.COUNTER_CREATED_AT + " DESC", "1");
		ArrayList<Action> aryAction = new ArrayList<Action>();
		cursor.requery();
        if (cursor.moveToFirst())
        do {
        	Action a =  getAction(cursor);
        	aryAction.add(a);
        } while(cursor.moveToNext());
        
        if(aryAction.size()>0)
        	return aryAction.get(0);
        else
        	return null;
		
	}	
	
	public void dropTableAction(){
		db.execSQL("DROP TABLE IF EXISTS " + ConfDatabase.DATABASE_TABLE_ACTION);
	}
	
	// Remove a task based on its index
	public boolean removeAction(long _rowIndex) {
		return db.delete(ConfDatabase.DATABASE_TABLE_ACTION, ConfDatabase.ACTION_ID + "=" + _rowIndex, null) > 0;
	}	
	
	// get action
	public Action getAction(long _rowIndex) throws SQLException {
		
		Cursor cursor = getAllActionCursor(null,null,null);
		
		if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
			throw new SQLException("No to do item found for row: " + _rowIndex);
		}
		
		int id = cursor.getInt(ConfDatabase.ACTION_ID_COLUMN);
		int domo_id = cursor.getInt(ConfDatabase.ACTION_DOMO_ID_COLUMN);
		int parent_id = cursor.getInt(ConfDatabase.ACTION_PARENT_ID_COLUMN);
		String starttime = cursor.getString(ConfDatabase.ACTION_STARTTIME_COLUMN);
		String endtime = cursor.getString(ConfDatabase.ACTION_ENDTIME_COLUMN);
		String created_at = cursor.getString(ConfDatabase.ACTION_CREATED_AT_COLUMN);
		String name = cursor.getString(ConfDatabase.ACTION_NAME_COLUMN);
		int delay = cursor.getInt(ConfDatabase.ACTION_DELAY_COLUMN);
		int root_id = cursor.getInt(ConfDatabase.ACTION_ROOT_ID_COLUMN);		
		int is_trigger = cursor.getInt(ConfDatabase.ACTION_IS_TRIGGER_COLUMN);		
		int pos = cursor.getInt(ConfDatabase.ACTION_POS_COLUMN);		
		String scheduled = cursor.getString(ConfDatabase.ACTION_SCHEDULED_COLUMN);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date dateCreated_at = dateFormat.parse(created_at);
			Date dateStarttime = dateFormat.parse(starttime);
			Date dateEndtitme = dateFormat.parse(endtime);
			Action result = new Action(id, domo_id, parent_id, dateStarttime, dateEndtitme, dateCreated_at, name, delay, root_id, is_trigger, pos, scheduled);
			return result;
		}catch(Exception e){
			Log.e(TAG, "Error during parse date " + e);
		}
		return null;	
	}		
	
	// get array list of counter
	public ArrayList<Action> getAllAction(String where, String[] wherep, String groupby){
		Cursor c = getAllActionCursor(where, wherep, groupby);
		ArrayList<Action> aryAction = new ArrayList<Action>();
        
        c.requery();
        if (c.moveToFirst())
        do {
        	Action action =  getAction(c);
        	aryAction.add(action);
        } while(c.moveToNext());
        
        return aryAction;
	}		
	
	public Cursor getAllActionCursor(String where, String[] wherep, String groupby) {
		return db.query(false, ConfDatabase.DATABASE_TABLE_ACTION,
				new String[] { ConfDatabase.ACTION_ID, ConfDatabase.ACTION_DOMO_ID, ConfDatabase.ACTION_PARENT_ID, ConfDatabase.ACTION_STARTTIME, ConfDatabase.ACTION_ENDTIME, ConfDatabase.ACTION_CREATED_AT, ConfDatabase.ACTION_NAME, ConfDatabase.ACTION_DELAY, ConfDatabase.ACTION_ROOT_ID, ConfDatabase.ACTION_IS_TRIGGER, ConfDatabase.ACTION_POS, ConfDatabase.ACTION_SCHEDULED},
				where, wherep, groupby, null, null, null);		
	}	
	
	public Action getAction(Cursor cursor){
		
		int id = cursor.getInt(ConfDatabase.ACTION_ID_COLUMN);
		int domo_id = cursor.getInt(ConfDatabase.ACTION_DOMO_ID_COLUMN);
		int parent_id = cursor.getInt(ConfDatabase.ACTION_PARENT_ID_COLUMN);
		String starttime = cursor.getString(ConfDatabase.ACTION_STARTTIME_COLUMN);
		String endtime = cursor.getString(ConfDatabase.ACTION_ENDTIME_COLUMN);
		String created_at = cursor.getString(ConfDatabase.ACTION_CREATED_AT_COLUMN);
		String name = cursor.getString(ConfDatabase.ACTION_NAME_COLUMN);
		int delay = cursor.getInt(ConfDatabase.ACTION_DELAY_COLUMN);
		int root_id = cursor.getInt(ConfDatabase.ACTION_ROOT_ID_COLUMN);	
		int is_trigger = cursor.getInt(ConfDatabase.ACTION_IS_TRIGGER_COLUMN);		
		int pos = cursor.getInt(ConfDatabase.ACTION_POS_COLUMN);	
		String scheduled = cursor.getString(ConfDatabase.ACTION_SCHEDULED_COLUMN);
        
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date dateCreated_at = dateFormat.parse(created_at);
			Date dateStarttime = dateFormat.parse(starttime);
			Date dateEndtitme = dateFormat.parse(endtime);
			Action result = new Action(id, domo_id, parent_id, dateStarttime, dateEndtitme, dateCreated_at, name, delay, root_id, is_trigger, pos, scheduled);
			return result;
		}catch(Exception e){
			Log.e(TAG, "Error during parse date " + e);
		}
		return null;	
	}	
	
	public void updateAction(Action action, String raw_name, String value){
		ContentValues updateAction = new ContentValues();
		// check the type! integer right now!
			updateAction.put(raw_name, Integer.parseInt(value));
		Log.i(TAG, "---> v "+value);
	    db.update(ConfDatabase.DATABASE_TABLE_ACTION, updateAction, ConfDatabase.ACTION_ID + "=" + action.getId(), null);
	}
	
	public void updateAction(Action action, String raw_name, Date value){
		ContentValues updateAction = new ContentValues();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		updateAction.put(raw_name, dateFormat.format(value));
		Log.i(TAG, "---> v "+value);
	    db.update(ConfDatabase.DATABASE_TABLE_ACTION, updateAction, ConfDatabase.ACTION_ID + "=" + action.getId(), null);
	}
	// ------------------------------------------------------------------
	
	// ------------------------------------------------------------------
	// insert a new counter
	public long insertCounter(Counter counter){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ContentValues newTaskValues = new ContentValues();
		newTaskValues.put(ConfDatabase.COUNTER_DOMO_ID, counter.getDomo_id());
		newTaskValues.put(ConfDatabase.COUNTER_VALUE, counter.getValue());
		newTaskValues.put(ConfDatabase.ACTUATOR_CREATED_AT, dateFormat.format(counter.getCreated_at()));
		return db.insert(ConfDatabase.DATABASE_TABLE_COUNTER, null, newTaskValues);
	}
	
	
	// clear counter table
	public boolean clearCounter() {
		return db.delete(ConfDatabase.DATABASE_TABLE_COUNTER, null, null) > 0;
	}
	
	public void dropTableCounter(){
		db.execSQL("DROP TABLE IF EXISTS " + ConfDatabase.DATABASE_TABLE_COUNTER);
	}
	
	// Remove a task based on its index
	public boolean removeCounter(long _rowIndex) {
		return db.delete(ConfDatabase.DATABASE_TABLE_COUNTER, ConfDatabase.COUNTER_ID + "=" + _rowIndex, null) > 0;
	}
	
	// Remove a task based on its index
	public boolean removeOldCounter(int days) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.add(Calendar.DATE, -days);
		return db.delete(ConfDatabase.DATABASE_TABLE_COUNTER, ConfDatabase.COUNTER_CREATED_AT + "<" + rightNow.getTime(), null) > 0;
	}	
	
	// get actuator
	public Counter getCounter(long _rowIndex) throws SQLException {
		
		Cursor cursor = getAllActuatorCursor(null,null,null);
		
		if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
			throw new SQLException("No to do item found for row: " + _rowIndex);
		}
		
		int id = cursor.getInt(ConfDatabase.COUNTER_ID_COLUMN);
		int domo_id = cursor.getInt(ConfDatabase.COUNTER_DOMO_ID_COLUMN);
		String value = cursor.getString(ConfDatabase.COUNTER_VALUE_COLUMN);
		String created_at = cursor.getString(ConfDatabase.COUNTER_CREATED_AT_COLUMN);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date dateCreated_at = dateFormat.parse(created_at);
			Counter result = new Counter(id, domo_id, value, dateCreated_at);
			return result;
		}catch(Exception e){
			Log.e(TAG, "Error during parse date " + e);
		}
		return null;	
	}		
	
	// get array list of counter
	public ArrayList<Counter> getAllCounter(String where, String[] wherep, String groupby){
		Cursor c = getAllCounterCursor(where, wherep, groupby);
		ArrayList<Counter> aryCounter = new ArrayList<Counter>();
        
        c.requery();
        if (c.moveToFirst())
        do {
        	Counter counter =  getCounter(c);
        	aryCounter.add(counter);
        } while(c.moveToNext());
        
        return aryCounter;
	}	
	
	public Counter getLast(int domo_id) {		
		Cursor cursor = db.query(false, ConfDatabase.DATABASE_TABLE_COUNTER,
				new String[] { ConfDatabase.COUNTER_ID, ConfDatabase.COUNTER_DOMO_ID, ConfDatabase.COUNTER_VALUE, ConfDatabase.COUNTER_CREATED_AT},
				ConfDatabase.COUNTER_DOMO_ID + "=" + "?", new String[]{Integer.toString(domo_id)}, null, null, ConfDatabase.COUNTER_CREATED_AT + " DESC", "1");
		ArrayList<Counter> aryCounter = new ArrayList<Counter>();
		cursor.requery();
        if (cursor.moveToFirst())
        do {
        	Counter counter =  getCounter(cursor);
        	aryCounter.add(counter);
        } while(cursor.moveToNext());
        
        if(aryCounter.size()>0)
        	return aryCounter.get(0);
        else
        	return null;
		
	}		
	
	public Cursor getAllCounterCursor(String where, String[] wherep, String groupby) {		
		/*return db.query(ConfDatabase.DATABASE_TABLE_ACTUATOR,
		new String[] { ConfDatabase.ACTUATOR_ID, ConfDatabase.ACTUATOR_IP, ConfDatabase.ACTUATOR_OUT, ConfDatabase.ACTUATOR_TYPE, ConfDatabase.ACTUATOR_NAME, ConfDatabase.ACTUATOR_STATUS, ConfDatabase.ACTUATOR_CREATED_AT},
		null, null, null, null, null);*/
		return db.query(false, ConfDatabase.DATABASE_TABLE_COUNTER,
				new String[] { ConfDatabase.COUNTER_ID, ConfDatabase.COUNTER_DOMO_ID, ConfDatabase.COUNTER_VALUE, ConfDatabase.COUNTER_CREATED_AT},
				where, wherep, groupby, null, null, null);		
	}		
	
	public Counter getCounter(Cursor cursor){
		
		int id = cursor.getInt(ConfDatabase.COUNTER_ID_COLUMN);
		int domo_id = cursor.getInt(ConfDatabase.COUNTER_DOMO_ID_COLUMN);
		String value = cursor.getString(ConfDatabase.COUNTER_VALUE_COLUMN);
		String created_at = cursor.getString(ConfDatabase.COUNTER_CREATED_AT_COLUMN);
		
        
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date dateCreated_at = dateFormat.parse(created_at);
			Counter result = new Counter(id, domo_id, value, dateCreated_at);
			return result;
		}catch(Exception e){
			Log.e(TAG, "Error during parse date " + e);
		}
		return null;	
	}	
	// ------------------------------------------------------------------	
	
	// ------------------------------------------------------------------
	// insert a new actuators
	public long intertActuator(Actuator a){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ContentValues newTaskValues = new ContentValues();
		newTaskValues.put(ConfDatabase.ACTUATOR_IP, a.getIp());
		newTaskValues.put(ConfDatabase.ACTUATOR_OUT, a.getOut());
		newTaskValues.put(ConfDatabase.ACTUATOR_TYPE, a.getType());
		newTaskValues.put(ConfDatabase.ACTUATOR_NAME, a.getName());
		newTaskValues.put(ConfDatabase.ACTUATOR_STATUS, a.getStatus());
		newTaskValues.put(ConfDatabase.ACTUATOR_CREATED_AT, dateFormat.format(a.getCreated_at()));
		
		return db.insert(ConfDatabase.DATABASE_TABLE_ACTUATOR, null, newTaskValues);
	}
	
	// Remove a task based on its index
	public boolean clearActuator() {
		return db.delete(ConfDatabase.DATABASE_TABLE_ACTUATOR, null, null) > 0;
	}
	
	public void dropTableActuator(){
		db.execSQL("DROP TABLE IF EXISTS " + ConfDatabase.DATABASE_TABLE_ACTUATOR);
	}
	
	// Remove a task based on its index
	public boolean removeActuator(long _rowIndex) {
		return db.delete(ConfDatabase.DATABASE_TABLE_ACTUATOR, ConfDatabase.ACTUATOR_ID + "=" + _rowIndex, null) > 0;
	}	
	
	// get actuator
	public Actuator getActuator(long _rowIndex) throws SQLException {
		
		Cursor cursor = getAllActuatorCursor(null,null,null);
		
		if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
			throw new SQLException("No to do item found for row: " + _rowIndex);
		}
	
		int id = cursor.getInt(ConfDatabase.ACTUATOR_ID_COLUMN);
		String ip = cursor.getString(ConfDatabase.ACTUATOR_IP_COLUMN);
		String out = cursor.getString(ConfDatabase.ACTUATOR_OUT_COLUMN);
		String type = cursor.getString(ConfDatabase.ACTUATOR_TYPE_COLUMN);
		String name = cursor.getString(ConfDatabase.ACTUATOR_NAME_COLUMN);
		int status = cursor.getInt(ConfDatabase.ACTUATOR_STATUS_COLUMN);
		String created_at = cursor.getString(ConfDatabase.ACTUATOR_CREATED_AT_COLUMN);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date dateCreated_at = dateFormat.parse(created_at);
			Actuator result = new Actuator(id, ip, out, type, name, status, dateCreated_at);
			return result;
		}catch(Exception e){
			Log.e(TAG, "Error during parse date " + e);
		}
		return null;	
	}	
	
	// get array list of actuator
	public ArrayList<Actuator> getAllActuators(String where, String[] wherep, String groupby){
		Cursor c = getAllActuatorCursor(where, wherep, groupby);
		ArrayList<Actuator> aryActuator = new ArrayList<Actuator>();
        
        c.requery();
        if (c.moveToFirst())
        do {
        	Actuator geoPoint =  getActuator(c);
        	aryActuator.add(geoPoint);
        } while(c.moveToNext());
        
        return aryActuator;
	}	
	
	// help
	public Cursor getAllActuatorCursor(String where, String[] wherep, String groupby) {		
		/*return db.query(ConfDatabase.DATABASE_TABLE_ACTUATOR,
		new String[] { ConfDatabase.ACTUATOR_ID, ConfDatabase.ACTUATOR_IP, ConfDatabase.ACTUATOR_OUT, ConfDatabase.ACTUATOR_TYPE, ConfDatabase.ACTUATOR_NAME, ConfDatabase.ACTUATOR_STATUS, ConfDatabase.ACTUATOR_CREATED_AT},
		null, null, null, null, null);*/
		return db.query(false, ConfDatabase.DATABASE_TABLE_ACTUATOR,
				new String[] { ConfDatabase.ACTUATOR_ID, ConfDatabase.ACTUATOR_IP, ConfDatabase.ACTUATOR_OUT, ConfDatabase.ACTUATOR_TYPE, ConfDatabase.ACTUATOR_NAME, ConfDatabase.ACTUATOR_STATUS, ConfDatabase.ACTUATOR_CREATED_AT},
				where, wherep, groupby, null, null, null);		
	}	
	
	public Cursor custom(){
		//query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
		return db.query(false, ConfDatabase.DATABASE_TABLE_ACTUATOR,
				new String[] { ConfDatabase.ACTUATOR_ID, ConfDatabase.ACTUATOR_IP, ConfDatabase.ACTUATOR_OUT, ConfDatabase.ACTUATOR_TYPE, ConfDatabase.ACTUATOR_NAME, ConfDatabase.ACTUATOR_STATUS, ConfDatabase.ACTUATOR_CREATED_AT},
				null, null, ConfDatabase.ACTUATOR_TYPE, null, null, null);		
	}
	
	public Actuator getActuator(Cursor cursor){
		int id = cursor.getInt(ConfDatabase.ACTUATOR_ID_COLUMN);
		String ip = cursor.getString(ConfDatabase.ACTUATOR_IP_COLUMN);
		String out = cursor.getString(ConfDatabase.ACTUATOR_OUT_COLUMN);
		String type = cursor.getString(ConfDatabase.ACTUATOR_TYPE_COLUMN);
		String name = cursor.getString(ConfDatabase.ACTUATOR_NAME_COLUMN);
		int status = cursor.getInt(ConfDatabase.ACTUATOR_STATUS_COLUMN);
		String created_at = cursor.getString(ConfDatabase.ACTUATOR_CREATED_AT_COLUMN);
		
        
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date dateCreated_at = dateFormat.parse(created_at);
			Actuator result = new Actuator(id, ip, out, type, name, status, dateCreated_at);
			return result;
		}catch(Exception e){
			Log.e(TAG, "Error during parse date " + e);
		}
		return null;	
	}	
	
	
	// ------------------------------------------------------------------
}


