package lu.mastorak.luxinfowidget.managers;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import android.util.Log;

import lu.mastorak.luxinfowidget.content.WeatherData;
import lu.mastorak.luxinfowidget.util.Constants;
import lu.mastorak.luxinfowidget.util.AppResources;
import lu.mastorak.luxinfowidget.util.LocalContent;
import lu.mastorak.luxinfowidget.widget.R;

/**
 * Class to manage the remote xml weather data information
 * @author Konstantinos Mastorakis
 *
 */
public class WeatherManager {

	private LocalContent localContent;
	
	/**
	 * Default constructor for WeatherManager
	 */
	public WeatherManager(){
		
		localContent=new LocalContent();
		
	}
	
	/**
	 * A method that parses the yahoo weather forecast xml feed and returns the weather data
	 * @param Document document The xml document that contains the yahoo weather forecast info
	 * @return WeatherData data The weather data object containing the weather information
	 */
	private WeatherData parseYahooWeather(Document document){
		
		WeatherData data;
		
		String temperature="";
		String high="";
		String low="";
		String description="";
		String code="";
		String tomorrowHigh="";
		String tomorrowLow="";
		String tomorrowDescription="";
	
		String imageUrl=AppResources.getStringResource(R.string.weather_default_image_url); //the default N/A image
		
		if(document!=null){
			//Get current weather
			Node conditions= document.getElementsByTagName("yweather:condition").item(0);
			if(conditions!=null){
				temperature = conditions.getAttributes().getNamedItem("temp").getNodeValue().toString();
				temperature+="°C ";
				description = conditions.getAttributes().getNamedItem("text").getNodeValue().toString();
				code = conditions.getAttributes().getNamedItem("code").getNodeValue().toString();
			}
			//Get today's forecast
			Node today = document.getElementsByTagName("yweather:forecast").item(0);
			if(today!=null){
				high = today.getAttributes().getNamedItem("high").getNodeValue().toString();
				high+="°C ";
				low = today.getAttributes().getNamedItem("low").getNodeValue().toString();
				low+="°C ";
			}
			//Get tomorrow's forecast
			Node tomorrow=document.getElementsByTagName("yweather:forecast").item(1);
			if(tomorrow!=null){
				tomorrowHigh = tomorrow.getAttributes().getNamedItem("high").getNodeValue().toString();
				tomorrowHigh+="°C ";
				tomorrowLow = tomorrow.getAttributes().getNamedItem("low").getNodeValue().toString();
				tomorrowLow+="°C ";
				tomorrowDescription = tomorrow.getAttributes().getNamedItem("text").getNodeValue().toString();
			}
			
			//construct image url
			if(code!=null && !code.equals("")){
				imageUrl=AppResources.getStringResource(R.string.weather_image_base_url)+code+".gif";
			}
		}
		
		//create the weather data 
		data=new WeatherData(temperature, high, low, description, tomorrowHigh, tomorrowLow, tomorrowDescription, imageUrl);
		
		Log.i(Constants.UTIL_LOG, "Weather:"+temperature+" "+description);
		
		//store weather data
		localContent.storeWeatherData(data);
		
		return data;
		
	}
	
	/**
	 * The public method to call the method that parses the weather forecast xml feed and returns the weather data
	 * @param Document document The xml document that contains the yahoo weather forecast info
	 * @return WeatherData The weather data object containing the weather information
	 */
	public WeatherData getWeatherData(Document document){
		return parseYahooWeather(document);
	}
}
