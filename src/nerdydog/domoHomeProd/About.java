package nerdydog.domoHomeProd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class About extends Activity{
	
	TextView tv;
	Button moreinfo;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        tv = (TextView)findViewById(R.id.abouttextview0);
        moreinfo = (Button)findViewById(R.id.buttomoreinfo);
        String text = "<h3>What is it?</h3>" +
        			  "<p>This app is used to comunicate with your Arduino, obviously it must be linked to your local network via ethernet or wifi shield.</p>" +
        			  "<p>You can control the Arduino output sending an http call, those outs are wired to relays and you can hit light on, open gate or whatever else you want.</p>" +
        			  "<h3>How to use</h3>" +
        			  "<p>Set Arduino IP on Settings page and then sync DomoticHome with Arduino. Your Arduino should be configured to handle incoming http request.</p>" + 
        			  "<p>More information about communication protocol and integration on:</p>" +
        			  "<a href='http://www.arduino.cc/cgi-bin/yabb2/YaBB.pl?num=1284455681'>Arduino Blog</a><br>"+
        			  "<a href='http://nerdydog.it/posts/28-domotica-con-android-e-arduino-aprire-porte-cancelli'>NerdyDog (just in italian)</a><br>" + 
        			  "Developed by <b>Mattia Lipreri</b>";
        tv.setText(Html.fromHtml(text));
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        moreinfo.setOnClickListener(moreinfoClicked);
    }
	
	View.OnClickListener moreinfoClicked = new View.OnClickListener() {
		
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			String[] recipients = new String[]{"mattia.lipreri@gmail.com", "",};
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "[DomoticHome] More info");
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hi Mattia, ...");
			emailIntent.setType("text/plain");
			startActivity(Intent.createChooser(emailIntent, "Send mail..."));
			finish();
		}
	};

}
