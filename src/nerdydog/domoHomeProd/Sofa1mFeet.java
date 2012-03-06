package nerdydog.domoHomeProd;

import nerdydog.domoHomeProd.json.ParseJSON;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class Sofa1mFeet extends Activity{

	String IP = "";
	String LOG = "Sofa1mFeet";
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sofa1mfeet);
        
        Button upButton = (Button) findViewById(R.id.buttonFeetUp1m);
        upButton.setOnTouchListener(touchUp);
        
        Button downButton = (Button) findViewById(R.id.buttonFeetDown1m);
        downButton.setOnTouchListener(touchDown);     
                
		Bundle extras = getIntent().getExtras();
		if(extras != null){
	        IP = extras.getString("ip");
	        Log.i("TAG", "sofa " + IP );
		}
    }
    
    View.OnTouchListener touchUp = new View.OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			Log.i(LOG, "event touchUp " + event.getAction());
			String status="", out="", url="";
			if(event.getAction() == (MotionEvent.ACTION_DOWN)) {
            	status = "1";
            	out = "5";           
	        } else if (event.getAction() == (MotionEvent.ACTION_UP)) {
            	status = "0";
            	out = "5";
	        }
			url = "http://" + IP + "/?out=" + out + "&status=" + status;
			Log.i(LOG, url);
			new DownloadFilesTask().execute(IP, out, status);
			return false;
		}
	};
	
    View.OnTouchListener touchDown = new View.OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			Log.i(LOG, "event touchDown " + event.getAction());
			String status="", out="", url="";
			if(event.getAction() == (MotionEvent.ACTION_DOWN)) {
            	status = "1";
            	out = "4";           
	        } else if (event.getAction() == (MotionEvent.ACTION_UP)) {
            	status = "0";
            	out = "4";
	        }
			url = "http://" + IP + "/?out=" + out + "&status=" + status;
			Log.i(LOG, url);
			new DownloadFilesTask().execute(IP, out, status);
			return false;
		}
	};	
	
		
	    private class DownloadFilesTask extends AsyncTask<String, String, Boolean> {
			 
		     protected Boolean doInBackground(String... strings) {
		    	
	   		String url = "http://" + strings[0] + "/?out=" + strings[1] + "&status=" + strings[2];
	   		Log.e(LOG, "executing..." + url);
		        return ParseJSON.doRequestToArduino(url);
		     }

		     protected void onPostExecute(Boolean result) {
		    	 Log.i("VOICE", " DownloadFilesTask  " +result);
		    	 if(!result){
						/*Toast.makeText(
						c,
						R.string.err_timeout,
						Toast.LENGTH_SHORT).show();*/
		    	 }
		     }
		 }	
}
