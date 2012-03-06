package nerdydog.domoHomeProd;

import nerdydog.domoHomeProd.db.ConfDatabase;
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
	
	EditText edittextIp, edittextIpEnd, edittextPowerAdj;
	Button buttonSave;
	String ip,ip_end,power_adj;
    private final static String TAG = "Settings";	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        Utility.getIpFromPreferencesData(this);
        
        edittextIp = (EditText)this.findViewById(R.id.edittextip);
        edittextIp.setText(ConfDatabase.CURRENT_IP_KEY);
        
        edittextIpEnd = (EditText)this.findViewById(R.id.edittextipend);
        
        edittextPowerAdj = (EditText)this.findViewById(R.id.edittextpoweradj);
        
        buttonSave = (Button)this.findViewById(R.id.buttonsave);
        buttonSave.setOnClickListener(saveListener);
        
        updatePreferencesData();
    }
	
	View.OnClickListener saveListener = new View.OnClickListener() {
		
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			savePreferencesData();
		}
	};
	
   public void savePreferencesData() {
        SharedPreferences prefs = getSharedPreferences(ConfDatabase.MY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        ip = edittextIp.getText().toString();
        ip_end = edittextIpEnd.getText().toString();
        power_adj = edittextPowerAdj.getText().toString();
        if (ip != null) {
        		Log.i(TAG, "Saving data...");
                editor.putString(ConfDatabase.IP_KEY, ip);
                editor.putString(ConfDatabase.IP_END_KEY, ip_end);
                editor.putString(ConfDatabase.IP_POWER_ADJ_KEY, power_adj);
                editor.commit();
    			Intent i = new Intent(Settings.this, DomoHome.class);
    			startActivity(i);	
        }
    }
    
    private void updatePreferencesData(){
        SharedPreferences prefs = getSharedPreferences(ConfDatabase.MY_PREFERENCES, Context.MODE_PRIVATE);
        String p_ip = prefs.getString(ConfDatabase.IP_KEY, "");
        String p_ip_end = prefs.getString(ConfDatabase.IP_END_KEY, "");
        String p_power_adj = prefs.getString(ConfDatabase.IP_POWER_ADJ_KEY, "");
        
        if (p_ip != null && p_ip_end != null && p_ip.length() > 0){
        	Log.i(TAG, "Update ip " + p_ip + " ip end " + p_ip_end);
        	edittextIp.setText(p_ip);
        	edittextIpEnd.setText(p_ip_end);
        	edittextPowerAdj.setText(p_power_adj);
        }  
    }    	
	
}
