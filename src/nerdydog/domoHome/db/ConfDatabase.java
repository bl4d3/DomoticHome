package nerdydog.domoHome.db;

import java.util.ArrayList;

import nerdydog.domoHome.object.Actuator;

public final class ConfDatabase {
	
	//preferences
	String ip;
	public final static String MY_PREFERENCES = "domohome";
	public final static String IP_KEY = "ip";
	public static String DEFAULT_IP_KEY = "192.168.10.15";
	public static String CURRENT_IP_KEY = "192.168.10.15";
	
	public static final String DATABASE_NAME = "DomoHome.db";
	public static final String DATABASE_TABLE_ACTUATOR = "actuator";
	public static final String DATABASE_TABLE_ACTION = "action";
	public static final String DATABASE_TABLE_COUNTER = "counter";
	public static final int DATABASE_VERSION = 2;
	
	public static final String TYPE_DOOR = "door";
	public static final String TYPE_GATE = "gate";
	public static final String TYPE_PLUG = "plug";
	public static final String TYPE_LIGHT = "light";
	public static final String TYPE_WATTMETER = "wattmeter";
	public static final String TYPE_TEMPERATURE = "temperature";
	public static final String TYPE_HUMIDITY = "humidity";
	public static final String TYPE_ACTION = "action";
	
	public static final String TYPE_DOOR_IT = "Porte";
	public static final String TYPE_GATE_IT = "Cancelli";
	public static final String TYPE_PLUG_IT = "Spine";
	public static final String TYPE_LIGHT_IT = "Luci";
	public static final String TYPE_WATTMETER_IT = "Consumi";
	public static final String TYPE_TEMPERATURE_IT = "Temperatura";
	public static final String TYPE_HUMIDITY_IT = "Umidità";	
	public static final String TYPE_ACTION_IT = "Azioni";
	
	public static ArrayList<Actuator> aryActuatorsForMeter = new ArrayList<Actuator>();
	public static ArrayList<Actuator> aryActuatorsSelectedForActions = new ArrayList<Actuator>();
	// ---------------------------------------------------------
	// actuator 
	public static final String ACTUATOR_ID = "_ID";
	public static final String ACTUATOR_IP = "ip";
	public static final String ACTUATOR_OUT = "out";
	public static final String ACTUATOR_TYPE = "type";
	public static final String ACTUATOR_NAME = "name";
	public static final String ACTUATOR_STATUS = "status";
	public static final String ACTUATOR_CREATED_AT = "created_at";
	
	public static final int ACTUATOR_ID_COLUMN = 0;
	public static final int ACTUATOR_IP_COLUMN = 1;
	public static final int ACTUATOR_OUT_COLUMN = 2;
	public static final int ACTUATOR_TYPE_COLUMN = 3;
	public static final int ACTUATOR_NAME_COLUMN = 4;
	public static final int ACTUATOR_STATUS_COLUMN = 5;
	public static final int ACTUATOR_CREATED_AT_COLUMN = 6;	
	
	// SQL Statement to create a new database.
	public static final String DATABASE_CREATE_TABLE_ACTUATOR = "create table " +
	ConfDatabase.DATABASE_TABLE_ACTUATOR + " (" + ConfDatabase.ACTUATOR_ID + " integer primary key autoincrement, " +
	ConfDatabase.ACTUATOR_IP + " text not null, " + ConfDatabase.ACTUATOR_OUT + " text not null, " + 
	ConfDatabase.ACTUATOR_TYPE + " text not null, " + ConfDatabase.ACTUATOR_NAME + " text not null, " +
	ConfDatabase.ACTUATOR_STATUS + " integer not null, " + ConfDatabase.ACTUATOR_CREATED_AT + " date not null);";
	// ---------------------------------------------------------
	
	// ---------------------------------------------------------
	// action
	public static final String ACTION_ID = "_ID";
	public static final String ACTION_DOMO_ID = "domo_id";
	public static final String ACTION_PARENT_ID = "parent_id";
	public static final String ACTION_STARTTIME = "starttime";
	public static final String ACTION_ENDTIME = "endtime";
	public static final String ACTION_CREATED_AT = "created_at";
	public static final String ACTION_NAME = "name";
	public static final String ACTION_DELAY = "delay";
	public static final String ACTION_ROOT_ID = "root_id";
	
	public static final int ACTION_ID_COLUMN = 0;
	public static final int ACTION_DOMO_ID_COLUMN = 1;
	public static final int ACTION_PARENT_ID_COLUMN = 2;
	public static final int ACTION_STARTTIME_COLUMN = 3;
	public static final int ACTION_ENDTIME_COLUMN = 4;
	public static final int ACTION_CREATED_AT_COLUMN = 5;
	public static final int ACTION_NAME_COLUMN = 6;
	public static final int ACTION_DELAY_COLUMN = 7;
	public static final int ACTION_ROOT_ID_COLUMN = 8;
	
	public static final String DATABASE_CREATE_TABLE_ACTION = "create table " +
	ConfDatabase.DATABASE_TABLE_ACTION + " (" + ConfDatabase.ACTION_ID + " integer primary key autoincrement, " +
	ConfDatabase.ACTION_DOMO_ID + " integer not null, " + ConfDatabase.ACTION_PARENT_ID + " integer not null, " +
	ConfDatabase.ACTION_NAME + " text not null, " +
	ConfDatabase.ACTION_DELAY + " integer, " + ConfDatabase.ACTION_ROOT_ID + " integer not null, " +
	ConfDatabase.ACTION_STARTTIME + " date, " + ConfDatabase.ACTION_ENDTIME + " date, " + 
	ConfDatabase.ACTUATOR_CREATED_AT + " date not null);";
	// ---------------------------------------------------------
	
	// ---------------------------------------------------------
	// counter
	public static final String COUNTER_ID = "_ID";
	public static final String COUNTER_DOMO_ID = "domo_id";
	public static final String COUNTER_VALUE = "value";
	public static final String COUNTER_CREATED_AT = "created_at";
	
	public static final int COUNTER_ID_COLUMN = 0;
	public static final int COUNTER_DOMO_ID_COLUMN = 1;
	public static final int COUNTER_VALUE_COLUMN = 2;
	public static final int COUNTER_CREATED_AT_COLUMN = 3;
	
	public static final String DATABASE_CREATE_TABLE_COUNTER = "create table " +
	ConfDatabase.DATABASE_TABLE_COUNTER + " (" + ConfDatabase.COUNTER_ID + " integer primary key autoincrement, " +
	ConfDatabase.COUNTER_DOMO_ID + " integer not null, " + ConfDatabase.COUNTER_VALUE + " text not null, " +
	ConfDatabase.ACTUATOR_CREATED_AT + " date not null);";	
	// ---------------------------------------------------------	

}
