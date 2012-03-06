package nerdydog.domoHomeProd;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import nerdydog.domoHomeProd.db.ConfDatabase;
import nerdydog.domoHomeProd.db.ToDoDBAdapter;
import nerdydog.domoHomeProd.json.ParseJSON;
import nerdydog.domoHomeProd.object.Action;
import nerdydog.domoHomeProd.object.Actuator;
import nerdydog.domoHomeProd.object.Counter;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Looper;
import android.text.format.Time;
import android.util.Log;

public class DataService extends Service{
	private static final String TAG = "DataService";
	Timer timer=new Timer();
	ToDoDBAdapter toDoDBAdapter;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate(){
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
        timer.cancel(); 
	}
	
	@Override
	public void onStart(Intent intent, int startid) {
        
        timer.schedule(new TimerTask() {
                
                @Override
                public void run() {
                        Log.v(TAG, "tic");
                        
                        Utility.updateActionTriggered(getApplicationContext());
                        
                        toDoDBAdapter = new ToDoDBAdapter(getApplicationContext());
                        
                        try {
                        	toDoDBAdapter.open();
                        	// meters
                        	Log.i(TAG, "meters size "+ConfDatabase.aryActuatorsForMeter.size());
							if (ConfDatabase.aryActuatorsForMeter.size() > 0) {
								for (int i = 0; i < ConfDatabase.aryActuatorsForMeter.size(); i++) {
									try{
										Actuator tmpActuator = ConfDatabase.aryActuatorsForMeter.get(i);
										Log.i(TAG, tmpActuator.getName());
										
	
										String url = "http://"+tmpActuator.getIp()+"/?out="
												+ tmpActuator.getOut()
												+ "&status=1";
	
										String value = ParseJSON.getMeterValueFromArduino(url);
										if (value.length() > 0) {
											Log.i(TAG, "id " + tmpActuator.getId());
											Counter counter = new Counter(-1,
													tmpActuator.getId(), value,
													new Date());
	
											toDoDBAdapter.insertCounter(counter);
											Log.i(TAG, "insert value " + counter.getValue() + " domo_id " + counter.getDomo_id());
											
											// remove old records
											toDoDBAdapter.removeOldCounter(1);
										}
									}catch(Exception e){}

								}
							} else {
								Log.i(TAG, "no meters");
							}

						} catch (Exception e) {
							Log.i(TAG, e.toString());
							if (toDoDBAdapter!=null) toDoDBAdapter.close();
						}finally{
							if (toDoDBAdapter!=null) toDoDBAdapter.close();
						}
                        Log.v(TAG, "toc");
						try {
							toDoDBAdapter.open();
							// trigger
							ArrayList<Action> actions = toDoDBAdapter.getAllAction(ConfDatabase.ACTION_ENDTIME + "!=" + ConfDatabase.ACTION_STARTTIME, null, null);
							Log.v(TAG, "actions size " + actions.size());
							
							for(int j = 0; j < actions.size(); j++){
								Action p_action = actions.get(j);
								
								SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
								Calendar now = Calendar.getInstance();
								now.add(Calendar.MINUTE, -1);
								
								Date dateNow = new Date();
								Time timeNow = new Time();
								timeNow.set(dateNow.getTime());
								
								Log.i(TAG, "=== START === date now " + dateNow.toLocaleString() + " compare to " + p_action.getStarttime().toLocaleString() + " result " + p_action.getStarttime().after(dateNow) + " scheduled " + p_action.getScheduled());
								Log.i(TAG, "=== END === date now " + dateNow.toLocaleString() + " compare to " + p_action.getEndtime().toLocaleString() + " result " + p_action.getEndtime().after(dateNow) + " scheduled " + p_action.getScheduled());
								
								if( p_action.getScheduled().equals(new String("0000000")))
								{
									Log.i(TAG, "single run");
									if(!p_action.getStarttime().after(dateNow) && p_action.getPos() == 0){
										// ON ONE TIME
										Log.i(TAG, "trigger on " + p_action.getName() + " action id "+ p_action.getId());
										
										ArrayList<Actuator> actuators = toDoDBAdapter.getAllActuators(ConfDatabase.ACTUATOR_ID + "=" + p_action.getDomo_id(), null, null);
										if(actuators.size() > 0){
											Actuator actuator = actuators.get(0);
																					
											String url = "http://" + actuator.getIp() + "/?out=" + actuator.getOut() + "&status=1";
											ParseJSON.doRequestToArduino(url);
											toDoDBAdapter.updateAction(p_action, ConfDatabase.ACTION_POS, "1");
										}
									}
									
									if(!p_action.getEndtime().after(dateNow) && p_action.getPos() == 1){
										// OFF ONE TIME
										Log.i(TAG, "trigger off " + p_action.getName() + " action id "+ p_action.getId());
										
										ArrayList<Actuator> actuators = toDoDBAdapter.getAllActuators(ConfDatabase.ACTUATOR_ID + "=" + p_action.getDomo_id(), null, null);
										if(actuators.size() > 0){
											Actuator actuator = actuators.get(0);
																					
											String url = "http://" + actuator.getIp() + "/?out=" + actuator.getOut() + "&status=0";
											ParseJSON.doRequestToArduino(url);
											toDoDBAdapter.updateAction(p_action, ConfDatabase.ACTION_POS, "2");
										}
									}
								}else{
									Log.i(TAG, "scheduled run");
									Time t_start = new Time();
									t_start.set(p_action.getStarttime().getTime());
									
									Time t_end = new Time();
									t_end.set(p_action.getEndtime().getTime());
									
									Log.i(TAG, " start time " + t_start.toString() + " end time " + timeNow.toString() + " result " + t_start.after(timeNow) + " status " + p_action.getPos() + " s " + p_action.getScheduled() +" is a day " + Utility.is_day_to_trigger(p_action.getScheduled()));
									
									
									if(!t_start.after(timeNow) && p_action.getPos() == 0 && Utility.is_day_to_trigger(p_action.getScheduled())){
										// ON SCHEDULED
										Log.i(TAG, "trigger on " + p_action.getName() + " action id "+ p_action.getId());
										
										ArrayList<Actuator> actuators = toDoDBAdapter.getAllActuators(ConfDatabase.ACTUATOR_ID + "=" + p_action.getDomo_id(), null, null);
										if(actuators.size() > 0){
											Actuator actuator = actuators.get(0);
																					
											String url = "http://" + actuator.getIp() + "/?out=" + actuator.getOut() + "&status=1";
											ParseJSON.doRequestToArduino(url);
											toDoDBAdapter.updateAction(p_action, ConfDatabase.ACTION_POS, "1");
										}
									}
									
									
									if(!t_end.after(timeNow) && p_action.getPos() == 1 && Utility.is_day_to_trigger(p_action.getScheduled())){
										// OFF SCHEDULED
										Log.i(TAG, "trigger off " + p_action.getName() + " action id "+ p_action.getId());
										
										ArrayList<Actuator> actuators = toDoDBAdapter.getAllActuators(ConfDatabase.ACTUATOR_ID + "=" + p_action.getDomo_id(), null, null);
										if(actuators.size() > 0){
											Actuator actuator = actuators.get(0);
																					
											String url = "http://" + actuator.getIp() + "/?out=" + actuator.getOut() + "&status=0";
											ParseJSON.doRequestToArduino(url);
											toDoDBAdapter.updateAction(p_action, ConfDatabase.ACTION_POS, "2");
										}
									}
								}
							}
							
							
						} catch (SQLiteException e) {
							Log.i(TAG, e.toString());
							toDoDBAdapter.close();
						}finally{
							toDoDBAdapter.close();
						}
                }
        },
        0,
        10000);
	}
	
	
}
