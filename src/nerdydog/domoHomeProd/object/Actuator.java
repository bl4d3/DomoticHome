package nerdydog.domoHomeProd.object;

import java.util.Date;

import android.util.Log;

import nerdydog.domoHomeProd.db.ConfDatabase;


public class Actuator {
	

	private static final String TAG = "Actuator";
	String ip, out, type, name;
	Date created_at;
	int id, status;
	
	public Actuator(int id, String ip, String out, String type, String name, int status, Date created_at){
		this.id = id;
		this.ip = ip;
		this.out = out;
		this.type = type;
		this.name = name;
		this.status = status;
		this.created_at = created_at;
	}
	
	public String toIta(){
		if( this.type.equals(new String(ConfDatabase.TYPE_DOOR)))
			return ConfDatabase.TYPE_DOOR_IT;
		if( this.type.equals(new String(ConfDatabase.TYPE_GATE)))
			return ConfDatabase.TYPE_GATE_IT;
		if( this.type.equals(new String(ConfDatabase.TYPE_TEMPERATURE)))
			return ConfDatabase.TYPE_TEMPERATURE_IT;		
		if( this.type.equals(new String(ConfDatabase.TYPE_LIGHT)))
			return ConfDatabase.TYPE_LIGHT_IT;
		if( this.type.equals(new String(ConfDatabase.TYPE_WATTMETER)))
			return ConfDatabase.TYPE_WATTMETER_IT;
		if( this.type.equals(new String(ConfDatabase.TYPE_ACTION)))
			return ConfDatabase.TYPE_ACTION_IT;	
		if( this.type.equals(new String(ConfDatabase.TYPE_SOFA)))
			return ConfDatabase.TYPE_SOFA_IT;	
		if( this.type.equals(new String(ConfDatabase.TYPE_IRRIGATION_IT)))
			return ConfDatabase.TYPE_IRRIGATION_IT;
		if( this.type.equals(new String(ConfDatabase.TYPE_WINDOW_IT)))
			return ConfDatabase.TYPE_WINDOW_IT;	
		return "";
	}
	
	public boolean isSingleButton(){
		if( this.type.equals(new String(ConfDatabase.TYPE_SOFA)) || this.type.equals(new String(ConfDatabase.TYPE_DOOR)) || this.type.equals(new String(ConfDatabase.TYPE_GATE))){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isMeter(){
		Log.i(TAG, this.type);
		if( this.type.equals(new String(ConfDatabase.TYPE_WATTMETER)) || this.type.equals(new String(ConfDatabase.TYPE_TEMPERATURE)) || this.type.equals(new String(ConfDatabase.TYPE_HUMIDITY))){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isTrigger(){
		if(this.type.equals(new String(ConfDatabase.TYPE_TEMPERATURE)) || this.type.equals(new String(ConfDatabase.TYPE_WATTMETER)) || this.type.equals(new String(ConfDatabase.TYPE_HUMIDITY)) || this.type.equals(new String(ConfDatabase.TYPE_SOFA)))
			return false;
		return true;
	}

	public int getId() {
		return id;
	}

	public void setId(int ip) {
		this.id = id;
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getOut() {
		return out;
	}

	public void setOut(String out) {
		this.out = out;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date createdAt) {
		created_at = createdAt;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	
	

}
