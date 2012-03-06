package nerdydog.domoHomeProd;

import nerdydog.domoHomeProd.json.ParseJSON;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class Sofa2mFeet extends Activity{

	String IP = "";
	String LOG = "Sofa2mFeet";
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sofa2mfeet);
        
        Button upButton1 = (Button) findViewById(R.id.buttonFeet1Up);
        upButton1.setOnTouchListener(touchUp1);
        
        Button downButton1 = (Button) findViewById(R.id.buttonFeet1Down);
        downButton1.setOnTouchListener(touchDown1);     
        
        Button upButtonBack2 = (Button) findViewById(R.id.buttonFeet2Up);
        upButtonBack2.setOnTouchListener(touchUpBack2);
        
        Button downButtonBack2 = (Button) findViewById(R.id.buttonFeet2Down);
        downButtonBack2.setOnTouchListener(touchDownBack2);
                
		Bundle extras = getIntent().getExtras();
		if(extras != null){
	        IP = extras.getString("ip");
	        Log.i("TAG", "sofa " + IP );
		}
    }
    
    View.OnTouchListener touchUp1 = new View.OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			Log.i(LOG, "event touchUp1 " + event.getAction());
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
	
    View.OnTouchListener touchDown1 = new View.OnTouchListener() {
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
	
	
    View.OnTouchListener touchUpBack2 = new View.OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			Log.i(LOG, "event touchUpBack " + event.getAction());
			String status="", out="", url="";
			if(event.getAction() == (MotionEvent.ACTION_DOWN)) {
            	status = "1";
            	out = "7";           
	        } else if (event.getAction() == (MotionEvent.ACTION_UP)) {
            	status = "0";
            	out = "7";
	        }
			url = "http://" + IP + "/?out=" + out + "&status=" + status;
			Log.i(LOG, url);
			new DownloadFilesTask().execute(IP, out, status);
			return false;
		}
	};
	
	   View.OnTouchListener touchDownBack2 = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				Log.i(LOG, "event touchDownBack " + event.getAction());
				String status="", out="", url="";
				if(event.getAction() == (MotionEvent.ACTION_DOWN)) {
	            	status = "1";
	            	out = "6";           
		        } else if (event.getAction() == (MotionEvent.ACTION_UP)) {
	            	status = "0";
	            	out = "6";
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
