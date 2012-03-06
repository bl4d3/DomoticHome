package nerdydog.domoHomeProd;

import java.net.SocketTimeoutException;

import nerdydog.domoHomeProd.db.ConfDatabase;
import nerdydog.domoHomeProd.json.ParseJSON;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class Sofa extends Activity{
    /**
     * Called with the activity is first created.
     */
	
	String IP = "";
	String LOG = "Sofa";
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sofa);
        
        Button upButton = (Button) findViewById(R.id.buttonFeetUp);
        upButton.setOnTouchListener(touchUp);
        
        Button downButton = (Button) findViewById(R.id.buttonFeetDown);
        downButton.setOnTouchListener(touchDown);     
        
        Button upButtonBack = (Button) findViewById(R.id.buttonBackUp);
        upButtonBack.setOnTouchListener(touchUpBack);
        
        Button downButtonBack = (Button) findViewById(R.id.buttonBackDown);
        downButtonBack.setOnTouchListener(touchDownBack);
        
        Button downButtonBoth = (Button) findViewById(R.id.buttonBackBoth);
        downButtonBoth.setOnTouchListener(touchDownBoth);

        Button upButtonBoth = (Button) findViewById(R.id.buttonUpBoth);
        upButtonBoth.setOnTouchListener(touchUpBoth);
        
		Bundle extras = getIntent().getExtras();
		if(extras != null){
	        IP = extras.getString("ip");
	        Log.i("TAG", "sofa " + IP );
		}
    }
    
    View.OnTouchListener touchUp = new View.OnTouchListener() {
		
    	
		public boolean onTouch(View v, MotionEvent event) {
			Log.i("VOICE", "event touchUp " + event.getAction());
			String status="", out="", url="";
			if(event.getAction() == (MotionEvent.ACTION_DOWN)) {
            	status = "1";
            	out = "5";           
	        } else if (event.getAction() == (MotionEvent.ACTION_UP)) {
            	status = "0";
            	out = "5";
	        }
			url = "http://" + IP + "/?out=" + out + "&status=" + status;
			Log.i("VOICE", url);
			new DownloadFilesTask().execute(IP, out, status);
			return false;
		}
	};
	
    View.OnTouchListener touchUpBack = new View.OnTouchListener() {
		
		
		public boolean onTouch(View v, MotionEvent event) {
			Log.i("VOICE", "event touchUpBack " + event.getAction());
			String status="", out="", url="";
			if(event.getAction() == (MotionEvent.ACTION_DOWN)) {
            	status = "1";
            	out = "7";           
	        } else if (event.getAction() == (MotionEvent.ACTION_UP)) {
            	status = "0";
            	out = "7";
	        }
			url = "http://" + IP + "/?out=" + out + "&status=" + status;
			Log.i("VOICE", url);
			new DownloadFilesTask().execute(IP, out, status);
			return false;
		}
	};
	
    View.OnTouchListener touchDown = new View.OnTouchListener() {
		
		
		public boolean onTouch(View v, MotionEvent event) {
			Log.i("VOICE", "event touchDown " + event.getAction());
			String status="", out="", url="";
			if(event.getAction() == (MotionEvent.ACTION_DOWN)) {
            	status = "1";
            	out = "4";           
	        } else if (event.getAction() == (MotionEvent.ACTION_UP)) {
            	status = "0";
            	out = "4";
	        }
			url = "http://" + IP + "/?out=" + out + "&status=" + status;
			Log.i("VOICE", url);
			new DownloadFilesTask().execute(IP, out, status);
			return false;
		}
	};	

    View.OnTouchListener touchDownBack = new View.OnTouchListener() {
		
		
		public boolean onTouch(View v, MotionEvent event) {
			Log.i("VOICE", "event touchDownBack " + event.getAction());
			String status="", out="", url="";
			if(event.getAction() == (MotionEvent.ACTION_DOWN)) {
            	status = "1";
            	out = "6";           
	        } else if (event.getAction() == (MotionEvent.ACTION_UP)) {
            	status = "0";
            	out = "6";
	        }
			url = "http://" + IP + "/?out=" + out + "&status=" + status;
			Log.i("VOICE", url);
			
			new DownloadFilesTask().execute(IP, out, status);
			return false;
		}
	};	
	
    View.OnTouchListener touchDownBoth = new View.OnTouchListener() {
		
		
		public boolean onTouch(View v, MotionEvent event) {
			Log.i("VOICE", "event touchDownBoth " + event.getAction());
			String status="", outBack= "", outFeet = "", urlBack="",urlFeet="";
			if(event.getAction() == (MotionEvent.ACTION_DOWN)) {
            	status = "1";
            	outBack = "6";
            	outFeet = "5";
	        } else if (event.getAction() == (MotionEvent.ACTION_UP)) {
            	status = "0";
            	outBack = "6";
            	outFeet = "5";
	        }
			urlBack = "http://" + IP + "/?out=" + outBack + "&status=" + status;
			Log.i("VOICE", urlBack);
			
			urlFeet = "http://" + IP + "/?out=" + outFeet + "&status=" + status;
			Log.i("VOICE", urlFeet);
			
			new DownloadFilesTask().execute(IP, outBack, status);
			new DownloadFilesTask().execute(IP, outFeet, status);
			return false;
		}
	};	
	
    View.OnTouchListener touchUpBoth = new View.OnTouchListener() {
		
		
		public boolean onTouch(View v, MotionEvent event) {
			Log.i("VOICE", "event touchUpBoth " + event.getAction());
			String status="", outBack= "", outFeet = "", urlBack="",urlFeet="";
			if(event.getAction() == (MotionEvent.ACTION_DOWN)) {
            	status = "1";
            	outBack = "7";
            	outFeet = "4";
	        } else if (event.getAction() == (MotionEvent.ACTION_UP)) {
            	status = "0";
            	outBack = "7";
            	outFeet = "4";
	        }
			urlBack = "http://" + IP + "/?out=" + outBack + "&status=" + status;
			Log.i("VOICE", urlBack);
			
			urlFeet = "http://" + IP + "/?out=" + outFeet + "&status=" + status;
			Log.i("VOICE", urlFeet);
			
			new DownloadFilesTask().execute(IP, outBack, status);
			new DownloadFilesTask().execute(IP, outFeet, status);
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
