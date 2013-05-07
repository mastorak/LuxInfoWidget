package lu.mastorak.luxinfowidget.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.util.ByteArrayBuffer;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import lu.mastorak.luxinfowidget.content.GigData;
import lu.mastorak.luxinfowidget.content.RssData;
import lu.mastorak.luxinfowidget.content.VdlData;
import lu.mastorak.luxinfowidget.content.WeatherData;

/**
 * This class contains methods to store and retrieve data from local storage
 * 
 * @author Konstantinos Mastorakis
 *
 */
public class LocalContent {

	/**
	 * The default Constructor
	 */
	public LocalContent(){
		
	}
	
	
	/**
	 * Method to store the weather data
	 * @param WeatherData data
	 * @return boolean
	 */
	public boolean storeWeatherData(WeatherData data){
		boolean success=false;
		
		SharedPreferences weather= AppResources.getSharedPreferences(Constants.WEATHER_STORAGE);
		Editor edit =weather.edit();
		//Store the prefences
		edit.putString(Constants.TEMPERATURE, data.getTemperature());
		edit.putString(Constants.HIGH, data.getHigh());
		edit.putString(Constants.LOW, data.getLow());
		edit.putString(Constants.DESCRIPTION, data.getDescription());
		edit.putString(Constants.TOMORROW_HIGH, data.getTomorrowHigh());
		edit.putString(Constants.TOMORROW_LOW, data.getTomorrowLow());
		edit.putString(Constants.TOMORROW_DESCRIPTION, data.getTomorrowDescription());
		edit.putString(Constants.IMAGE_URL, data.getImageUrl());
		success=edit.commit();
		
		//Store the image
		String FILENAME = Constants.WEATHER_IMAGE_FILE;
		RemoteContent remoteContent=new RemoteContent();
		ByteArrayBuffer baf=remoteContent.getRemoteContent(data.getImageUrl());
		
		if (baf!=null){
			try {
				FileOutputStream fos = AppResources.getContext().openFileOutput(FILENAME, AppResources.getContext().MODE_PRIVATE);
				fos.write(baf.toByteArray());
				fos.close();
			} catch (FileNotFoundException e) {
				success=false;
				Log.e(Constants.UTIL_LOG, "Error writing weather image:", e);
			} catch (IOException e) {
				success=false;
				Log.e(Constants.UTIL_LOG, "Error writing weather image:", e);
			}
		}
		
		if(success){
			Log.i(Constants.UTIL_LOG, "Succesfully stored weather data");
		}else{
			Log.w(Constants.UTIL_LOG, "Problem storing weather data");
		}
			
		return success;
	}
	
	/**
	 * Method to store the rss data
	 * @param RssData data
	 * @return boolean
	 */
	public boolean storeRssData(RssData data){
		boolean success=false;
		
		SharedPreferences rss=AppResources.getSharedPreferences(Constants.RSS_STORAGE);
		Editor edit =rss.edit();
		edit.putString(Constants.TITLE, data.getTitle());
		edit.putString(Constants.DESCRIPTION, data.getDescription());
		edit.putString(Constants.URL, data.getUrl());
		success=edit.commit();
		
		if(success){
			Log.i(Constants.UTIL_LOG, "Succesfully stored rss data");
		}else{
			Log.w(Constants.UTIL_LOG, "Problem storing rss data");
		}
		
		return success;
	}
	
	/**
	 * Method to store the gig data
	 * @param GigData data
	 * @return boolean
	 */
	public boolean storeGigData(GigData data){
		boolean success=false;
		
		SharedPreferences gig=AppResources.getSharedPreferences(Constants.GIG_STORAGE);
		Editor edit=gig.edit();
		edit.putString(Constants.TITLE, data.getTitle());
		edit.putString(Constants.DATE, data.getDate());
		edit.putString(Constants.URL, data.getUrl());
		success=edit.commit();
		
		if(success){
			Log.i(Constants.UTIL_LOG, "Succesfully stored gig data");
		}else{
			Log.w(Constants.UTIL_LOG, "Problem storing gig data");
		}
		
		return success;
	}
	
	/**
	 * Method to Store the vdl data
	 * @param VdlData data
	 * @return boolean
	 */
	public boolean storeVdlData(VdlData data){
		boolean success=false;
		
		SharedPreferences vdl=AppResources.getSharedPreferences(Constants.VDL_STORAGE);
		Editor edit =vdl.edit();
		edit.putString(Constants.TITLE, data.getTitle());
		edit.putString(Constants.DESCRIPTION, data.getDescription());
		edit.putString(Constants.URL, data.getUrl());
		success=edit.commit();
		
		if(success){
			Log.i(Constants.UTIL_LOG, "Succesfully stored vdl data");
		}else{
			Log.w(Constants.UTIL_LOG, "Problem storing vdl data");
		}
		
		
		return success;
	}
	
	/**
	 * Method to retrieved the stored vdl data
	 * 
	 * @return VdlData
	 */
	public VdlData getVdlData(){
		
		VdlData data=null;
		String title;
		String description;
		String url;
		
		SharedPreferences vdl = AppResources.getSharedPreferences(Constants.VDL_STORAGE);
		title=vdl.getString(Constants.TITLE, "");
		description=vdl.getString(Constants.DESCRIPTION, Constants.NO_DATA_AVAILABLE);
		url=vdl.getString(Constants.URL, "");
		
		data=new VdlData(title, url, description);
		
		return data;
		
	}
	
	/**
	 * Method to retrieved the stored rss data
	 * 
	 * @return RssData
	 */
	public RssData getRssData(){
		RssData data=null;
		String title;
		String description;
		String url;
		
		SharedPreferences rss = AppResources.getSharedPreferences(Constants.RSS_STORAGE);
		title=rss.getString(Constants.TITLE, "");
		description=rss.getString(Constants.DESCRIPTION, Constants.NO_DATA_AVAILABLE);
		url=rss.getString(Constants.URL, "");
		
		data=new RssData(title, url, description);
		
		return data;
	}
	
	/**
	 * Method to retrieved the stored gig data
	 * 
	 * @return GigData
	 */
	public GigData getGigData(){
		GigData data=null;
		String title;
		String date;
		String url;
		
		SharedPreferences gig = AppResources.getSharedPreferences(Constants.GIG_STORAGE);
		title=gig.getString(Constants.TITLE, "");
		date=gig.getString(Constants.DATE, Constants.NO_DATA_AVAILABLE);
		url=gig.getString(Constants.URL, "");
		
		data=new GigData(title, date, url);
		
		return data;
	}
	
	/**
	 * Method to retrieved the stored weather data
	 * 
	 * @return WeatherData
	 */
	public WeatherData getWeatherData(){
		
		WeatherData data=null;
		String temperature;
		String high;
		String low;
		String description;
		String tomorrowHigh;
		String tomorrowLow;
		String tomorrowDescription;
		String imageUrl;
		
		SharedPreferences weather = AppResources.getSharedPreferences(Constants.WEATHER_STORAGE);
		temperature=weather.getString(Constants.TEMPERATURE, Constants.NO_DATA_AVAILABLE);
		high=weather.getString(Constants.HIGH, "");
		low=weather.getString(Constants.LOW, "");
		description=weather.getString(Constants.DESCRIPTION, "");
		tomorrowHigh=weather.getString(Constants.TOMORROW_HIGH, "");
		tomorrowLow=weather.getString(Constants.TOMORROW_LOW, "");
		tomorrowDescription=weather.getString(Constants.TOMORROW_DESCRIPTION, "");
		imageUrl=weather.getString(Constants.IMAGE_URL, "");
		
		data=new WeatherData(temperature, high, low, description, tomorrowHigh, tomorrowLow, tomorrowDescription, imageUrl);
		
		return data;
	}
	
	/**
	 * A method to retrieve the stored weather image from local storage
	 * @return
	 */
	public Bitmap getWeatherImage(){
		
		Bitmap image=null;
		FileInputStream fis=null;
		try {
			fis = AppResources.getContext().openFileInput(Constants.WEATHER_IMAGE_FILE);
			image=BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			Log.e(Constants.UTIL_LOG, "Error reading weather image:", e);
		}
		
		return image;
		
	}
	
}
