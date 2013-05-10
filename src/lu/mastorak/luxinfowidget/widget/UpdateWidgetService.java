package lu.mastorak.luxinfowidget.widget;

import java.text.SimpleDateFormat;
import java.util.Date;

import lu.mastorak.luxinfowidget.content.GigData;
import lu.mastorak.luxinfowidget.content.RssData;
import lu.mastorak.luxinfowidget.content.VdlData;
import lu.mastorak.luxinfowidget.content.WeatherData;
import lu.mastorak.luxinfowidget.managers.GigManager;
import lu.mastorak.luxinfowidget.managers.RssManager;
import lu.mastorak.luxinfowidget.managers.VdlManager;
import lu.mastorak.luxinfowidget.managers.WeatherManager;
import lu.mastorak.luxinfowidget.util.AppResources;
import lu.mastorak.luxinfowidget.util.Constants;
import lu.mastorak.luxinfowidget.util.LocalContent;
import lu.mastorak.luxinfowidget.util.RemoteContent;

import org.w3c.dom.Document;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * The widget update service class
 * 
 * @author Konstantinos Mastorakis
 *
 */
public class UpdateWidgetService extends Service {
  
	private static int[] widgetIds;
	private static UpdateWidgetService serviceInstance;
	
	
	/**
	 * Method to retrieve the instance of the service. 
	 * We use it to check if the service exists before invoking from elsewhere
	 * @return UpdateWidgetService
	 */
	public static UpdateWidgetService instance() {
        return serviceInstance;
	}
	
	@Override
	public void onCreate(){
		//set the context so non-activity classes can use it
		AppResources.setContext(this);
		//set the service instance so we can check if it exists from elsewhere
		serviceInstance=this;
	}
	
  @Override
  public void onStart(Intent intent, int startId) {
      
    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
        .getApplicationContext());

    int[] allWidgetIds = intent
        .getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
    
    //save the widgetids to use them when invoking the service elsewhere
    if(allWidgetIds!=null){
    	widgetIds=allWidgetIds;
    }
    	 
	    for (int widgetId : widgetIds) {
	    
	      RemoteViews remoteViews = new RemoteViews(this
	          .getApplicationContext().getPackageName(),
	          R.layout.widget_layout);
	      
	      // Set layout info with what is stored
	      setupLayoutIinfo(remoteViews);
	      appWidgetManager.updateAppWidget(widgetId, remoteViews);
	      
	      //only update if there is network connectivity
	      if(RemoteContent.isOnline()){
		      //Retrieve the info from the internet
		      retrieveRemoteInfo();
		      // Set layout info with the latest info after updating
		      setupLayoutIinfo(remoteViews);
		      appWidgetManager.updateAppWidget(widgetId, remoteViews);
	      }
	      
	      //set onclick listener to the weather image
	      Intent i = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://weather.yahoo.com/forecast/LUXX0003_c.html"));
	      PendingIntent pendingIntent = PendingIntent.getActivity(AppResources.getContext(),
	              0, i, PendingIntent.FLAG_CANCEL_CURRENT);	      
	      remoteViews.setOnClickPendingIntent(R.id.weather_image,pendingIntent);
	      
	      appWidgetManager.updateAppWidget(widgetId, remoteViews);
	    }
    
    stopSelf();

    super.onStart(intent, startId);
  }
  
  
  /**
   * Method to retrieve the remote data using the various data managers 
   */
  private static void retrieveRemoteInfo(){
	  
	  RemoteContent remoteContentManager=new RemoteContent();
	  LocalContent localContentManager=new LocalContent();
	  //retrieve weather info
	  Document weatherDoc=remoteContentManager.getXmlContent(AppResources.getContext().getResources().getString(R.string.yahoo_weather_url));
      WeatherManager weatherManager=new WeatherManager();
      weatherManager.getWeatherData(weatherDoc);
      
      //retrive rss info
      Document newsDoc=remoteContentManager.getXmlContent(AppResources.getContext().getResources().getString(R.string.wort_feed_url));
      RssManager rssManager=new RssManager();
      rssManager.getRssData(newsDoc);
      
      //retrieve gig info
      GigManager gigManager=new GigManager();
      gigManager.getNextRockhalGig();
      
      //retrieve vdl info
      Document villeDoc=remoteContentManager.getXmlContent(AppResources.getContext().getResources().getString(R.string.vdl_feed_url));
      VdlManager vdlManager=new VdlManager();
      vdlManager.getVdlData(villeDoc);
	  
      //store time of update
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM HH:mm");
	  Date date = new Date();
	  String lastUpdateTime=dateFormat.format(date);
	  localContentManager.storeLastUpdateTime(lastUpdateTime);
	  
      
  }
  
  
  /**
   * Method to setup the layout with the data
   * @param RemoteViews remoteViews
   */
  private void setupLayoutIinfo(RemoteViews remoteViews){
	  
	  LocalContent localContent=new LocalContent();
	 
	  //set weather info
	  Bitmap weatherImage=null;
	  weatherImage=localContent.getWeatherImage();
	  if(weatherImage!=null){
		  remoteViews.setImageViewBitmap(R.id.weather_image, weatherImage);
	  }
	  WeatherData weather=localContent.getWeatherData();
	  remoteViews.setTextViewText(R.id.weather_text,
	          weather.getTemperature()+weather.getDescription()+"\n"+
			  "Low:"+weather.getLow()+"High:"+weather.getHigh());
	
	  remoteViews.setTextViewText(R.id.tomorrow_weather_text, 
			  "Tomorrow:"+weather.getTomorrowDescription()+"\n"+
			  weather.getTomorrowLow()+"/"+weather.getTomorrowHigh());
	  
	  //set rss info
	  RssData rss=localContent.getRssData();
	  remoteViews.setTextViewText(R.id.news_title, rss.getTitle());
	  remoteViews.setTextViewText(R.id.news_description, rss.getDescription());
	  
	  Intent rssIntent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(rss.getUrl()));
      PendingIntent rssPendingIntent = PendingIntent.getActivity(AppResources.getContext(),
              0, rssIntent, PendingIntent.FLAG_CANCEL_CURRENT);
      
      remoteViews.setOnClickPendingIntent(R.id.news_title,rssPendingIntent);
      remoteViews.setOnClickPendingIntent(R.id.news_description,rssPendingIntent);
	  
	  
	  //set vdl info
	  VdlData vdl=localContent.getVdlData();
	  remoteViews.setTextViewText(R.id.vdl_title, vdl.getTitle());
	  remoteViews.setTextViewText(R.id.vdl_description, vdl.getDescription());
	  
	  Intent vdlIntent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(vdl.getUrl()));
      PendingIntent vdlPendingIntent = PendingIntent.getActivity(AppResources.getContext(),
              0, vdlIntent, PendingIntent.FLAG_CANCEL_CURRENT);
      
      remoteViews.setOnClickPendingIntent(R.id.vdl_title,vdlPendingIntent);
      remoteViews.setOnClickPendingIntent(R.id.vdl_description,vdlPendingIntent);
	  
	  //set gig info
	  GigData gig=localContent.getGigData();
	  remoteViews.setTextViewText(R.id.gig_title,gig.getTitle());
	  remoteViews.setTextViewText(R.id.gig_description, gig.getDate());
	  
	  Intent gigIntent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(gig.getUrl()));
      PendingIntent gigPendingIntent = PendingIntent.getActivity(AppResources.getContext(),
              0, gigIntent, PendingIntent.FLAG_CANCEL_CURRENT);
      
      remoteViews.setOnClickPendingIntent(R.id.gig_title,gigPendingIntent);
      remoteViews.setOnClickPendingIntent(R.id.gig_description,gigPendingIntent);
      
      //set lastupdate
      String lastUpdateTime=localContent.getLastUpdateTime();
	  remoteViews.setTextViewText(R.id.last_update_text,"Last update:"+lastUpdateTime);
  }

  
 
  
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
  
  @Override
	public void onDestroy(){
	
		AppResources.setContext(null);
	}
} 
