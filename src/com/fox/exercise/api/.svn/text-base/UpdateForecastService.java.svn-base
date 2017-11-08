/*package com.fox.exercise.api;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.fox.exercise.api.entity.SportWeatherInfo;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

public class UpdateForecastService extends Service implements Runnable {
	private static final String TAG = "UpdateForecastService";
	private Uri currentUri = null;
	private String city = "Beijing";
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		
		super.onCreate();
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		
		currentUri = intent.getData();		
		Cursor cur = getContentResolver().query(currentUri, null, Weathers._ID +"="+ currentUri.getPathSegments().get(1), null, null);
	
		if(cur!=null && cur.getCount()>0){
			cur.moveToFirst();
			city = cur.getString(2);
		}		
		Log.d(TAG,"-----------city = "+city);
		
		new Thread(this).start();	
		
	}
	
	public void run(){
		Log.d(TAG,"----------------run --------------");
		SAXParserFactory spf = SAXParserFactory.newInstance();
        
		try {
	        SAXParser sp = spf.newSAXParser();
	        XMLReader reader = sp.getXMLReader();
	        
	        XMLHandler  handler = new XMLHandler();
	        reader.setContentHandler(handler);            
	
	        URL url = new URL(Weathers.WEB_URI + URLEncoder.encode(city));
	        InputStream is = url.openStream();
	        InputStreamReader isr = new InputStreamReader(is,"GBK");
	        InputSource source = new InputSource(isr);
	
	        reader.parse(source);
	        
	        SportWeatherInfo currentWeather = handler.getCurrentWeather();
	        Log.d(TAG,"------tempc = "+currentWeather.getTempc()+"    condition = "+currentWeather.getCondition());
	        
	        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
					"sports" + mSportsApp.getSportUser().getUid(), 0);
			Editor editor = sharedPreferences.edit();
			if (isChecked) {
				editor.putBoolean("isSports", true);
			} else {
				editor.putBoolean("isSports", false);
			}
			editor.commit();
	        
	        WeatherWidgetProvider.UpdateWeather(this,currentUri);
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG,"not complete the parser");
		}
		
		
		stopSelf();
	}
}
*/