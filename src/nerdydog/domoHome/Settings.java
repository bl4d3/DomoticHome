package nerdydog.domoHome;

import nerdydog.domoHome.db.ConfDatabase;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Settings extends Activity{
	
	EditText edittextIp;
	Button buttonSave;
	String ip;
    private final static String TAG = "Settings";	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        Utility.getIpFromPreferencesData(this);
        
        edittextIp = (EditText)this.findViewById(R.id.edittextip);
        edittextIp.setText(ConfDatabase.CURRENT_IP_KEY);
        
        buttonSave = (Button)this.findViewById(R.id.buttonsave);
        buttonSave.setOnClickListener(saveListener);
        
        updatePreferencesData();
    }
	
	View.OnClickListener saveListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			savePreferencesData();
		}
	};
	
   public void savePreferencesData() {
        SharedPreferences prefs = getSharedPreferences(ConfDatabase.MY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        ip = edittextIp.getText().toString();
        if (ip != null) {
        		Log.i(TAG, "Saving data...");
                editor.putString(ConfDatabase.IP_KEY, ip);
                editor.commit();
    			Intent i = new Intent(Settings.this, DomoHome.class);
    			startActivity(i);	
        }
    }
    
    private void updatePreferencesData(){
        SharedPreferences prefs = getSharedPreferences(ConfDatabase.MY_PREFERENCES, Context.MODE_PRIVATE);
        String p_ip = prefs.getString(ConfDatabase.IP_KEY, "");
        boolean update_ip = false;
        
        if (p_ip != null && p_ip.length() > 0){
        	Log.i(TAG, "Update ip " + p_ip);
        	edittextIp.setText(p_ip);
        	update_ip = true;
        }  
    }    	
	
}
