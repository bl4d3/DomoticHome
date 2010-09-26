package nerdydog.domoHome;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import nerdydog.domoHome.db.ConfDatabase;
import nerdydog.domoHome.db.ToDoDBAdapter;
import nerdydog.domoHome.json.ParseJSON;
import nerdydog.domoHome.object.Actuator;
import nerdydog.domoHome.object.Counter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
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
		toDoDBAdapter = new ToDoDBAdapter(getApplicationContext());
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
                        
                        try {
							if (ConfDatabase.aryActuatorsForMeter.size() > 0) {
								for (int i = 0; i < ConfDatabase.aryActuatorsForMeter
										.size(); i++) {
									Actuator tmpActuator = ConfDatabase.aryActuatorsForMeter
											.get(i);
									Log.i(TAG, tmpActuator.getName());
									

									String url = "http://"+ConfDatabase.CURRENT_IP_KEY+"/?out="
											+ tmpActuator.getOut()
											+ "&status=1";

									String value = ParseJSON.getMeterValueFromArduino(url);
									if (value.length() > 0) {
										Log.i(TAG, "id " + tmpActuator.getId());
										Counter counter = new Counter(-1,
												tmpActuator.getId(), value,
												new Date());
										toDoDBAdapter.open();

										toDoDBAdapter.insertCounter(counter);
										// remove old records
										toDoDBAdapter.removeOldCounter(1);
										toDoDBAdapter.close();

									}

								}
							} else {
								Log.i(TAG, "no meters");
							}
						} catch (Exception e) {
							Log.i(TAG, e.toString());
						}
                        	
                }
        },
        0,
        30000);
	}
	
}
