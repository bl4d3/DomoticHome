package nerdydog.domoHomeProd.json;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import nerdydog.domoHomeProd.db.ConfDatabase;
import nerdydog.domoHomeProd.object.Actuator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ParseJSON {
	private static final String LOG_TAG = "parse";
	private static String server;
	
	public static void setServer(String s){
		server = s;
	}
	
	public static String getServer(){
		return server;
	}
	
	ParseJSON(){}
	
	public static boolean doRequestToArduino(String url){
		JSONObject jsonChannels = getHttpJson(url);
		try{
			String status = jsonChannels.optString("status");
			if(status.length() > 0){
				Log.i(LOG_TAG, "status " + status);
				if(status.length()>0)
					return true; 
			}else{
				return false;
			}
			
		}catch(Exception e){
			Log.e(LOG_TAG, e.toString());
		}
		return false;
	}
	
	public static ArrayList<Actuator> getAllActuator(ArrayList<String> ips){
		
		String ip, out, type, name;
		Date starttime = null, endtime = null, created_at;
		int status;
		
		ArrayList<Actuator> aryActuator = new ArrayList<Actuator>();
		
		String jsonData = "{\"ip\" : \"192.168.10.15\", \"devices\" : [" +
				"{ \"type\" : \"gate\", \"name\" : \"cancellino ingresso\", \"out\" : \"5\"}, " +
				"{\"type\" : \"door\", \"name\" : \"porta ingresso\", \"out\" : \"4\"}, " +
				"{\"type\" : \"light\", \"name\" : \"luce soggiorno\", \"out\" : \"6\"}, " +
				"{\"type\" : \"light\", \"name\" : \"luce cucina\", \"out\" : \"7\"}," +
				"{\"type\" : \"temperature\", \"name\" : \"camera da letto\", \"out\" : \"0\"}," +
				"{\"type\" : \"temperature\", \"name\" : \"camera bimbi\", \"out\" : \"9\"}," +
				"{\"type\" : \"wattmeter\", \"name\" : \"consumo energetico casa\", \"out\" : \"10\"}]}";

		String jsonDataSofa = "{\"ip\" : \"192.168.10.19\", \"devices\" : [" +
			"{ \"type\" : \"sofa\", \"name\" : \"poltrona pizzetti\", \"out\" : \"0\"}]}";
		
		//String jsonData ="{\"ip\" : \"192.168.10.15\", \"devices\" : [{ \"type\" : \"gate\", \"name\" : \"cancellino ingresso\", \"out\" : \" 4 \"} , {\"type\" : \"door\", \"name\" : \"porta ingresso\", \"out\" : \" 5 \"} ]}";
		for(int j = 0; j<ips.size(); j++){
			try {
				//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				//JSONObject jsonObject = new JSONObject(jsonDataSofa);
				
				String url_login =  "http://"+ips.get(j)+"/?out=all";
				Log.i(LOG_TAG, "...sync with " + ips.get(j));
				JSONObject jsonObject = getHttpJson(url_login);
				
				if(jsonObject != null){
					
					ip = jsonObject.optString("ip");
					
					JSONArray aryDevices = jsonObject.getJSONArray("devices");
					
					// go through all the actuators
					for (int i=0;i<aryDevices.length();i++){
						
						JSONObject actuator = aryDevices.getJSONObject(i);
						
						type = actuator.optString("type");
						name = actuator.optString("name");
						out = actuator.optString("out");
						
						if(actuator.optString("status") == null || actuator.optString("out") == ""){
							status = 0;
						}else{
							status = 1;
						}
						
						Date dateNow = new Date();
						created_at = dateNow;
						
						Actuator result = new Actuator(-1, ip, out, type, name, status, created_at);
						aryActuator.add(result);
					}
				}
				
			} catch (JSONException e) {
				Log.e(LOG_TAG, e.toString());
			}
		}
		Log.i(LOG_TAG, " getAllActuator size " + aryActuator.size());
		return aryActuator;

	}
	
	public static String getMeterValueFromArduino(String url){
		JSONObject jsonChannels = getHttpJson(url);
		String value = "";
		try {
			value = jsonChannels.optString("value");
			
		} catch (/*JSONException e*/ Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}	

	
    public static JSONObject getHttpJson(String url) {
        JSONObject json = null;
        String result = getHttp(url);
        if(result != null)
        {
	        try {
	                json = new JSONObject(result);
	        } catch (JSONException e) {
	                Log.e(LOG_TAG, "There was a Json parsing based error", e);
	        }
        }
        return json;
    }	
	
    public static String getHttp(String url) {
        Log.d(LOG_TAG, "getHttp : " + url);
        String result = null;
        HttpClient httpclient = new DefaultHttpClient();
        
       
		URI u;
		try {
			u = new URI(url);
			HttpGet httpget = new HttpGet(u);
			
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 3000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			int timeoutSocket = 5000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			
			
			httpclient = new DefaultHttpClient(httpParameters);
			
			HttpResponse response;
	        Log.i(LOG_TAG, "http get " + httpget.toString());
	        
	        try {
	                response = httpclient.execute(httpget);
	                HttpEntity entity = response.getEntity();
	                if (entity != null) {
	                        InputStream instream = entity.getContent();
	                        result = convertStreamToString(instream);
	                        Log.i(LOG_TAG, result);
	                        instream.close();
	                }
	        } catch (ClientProtocolException e) {
	                Log.e(LOG_TAG, "There was a protocol based error", e);
	        } catch (IOException e) {
	                Log.e(LOG_TAG, "There was an IO Stream related error", e);
	        }			
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return result;
    }    
    
    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
                while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                }
        } catch (IOException e) {
                e.printStackTrace();
        } finally {
                try {
                        is.close();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
        return sb.toString();
    }
}


