package lu.mastorak.luxinfowidget.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

/**
 * Listener class to check for network connectivity
 * If network is available calls the service to update the widget
 * @author Konstantinos Mastorakis
 *
 */
public class NetworkListener extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		

		   ConnectivityManager cm = (ConnectivityManager) context
	                .getSystemService(Context.CONNECTIVITY_SERVICE);

		   android.net.NetworkInfo wifi=null;
	       android.net.NetworkInfo mobile=null;
		   
	       
	       if(cm!=null){
	       wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	       mobile= cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	       }
	        //check for connectivity
	       
	       
	       if ((wifi!=null && wifi.isConnected()) || (mobile!=null && mobile.isConnected())) {
	         	
	        		//check if service exists because on widget startup the broadcast
	        		//receiver might start before the service
	        		if(UpdateWidgetService.instance()!=null){
	            	 Intent in = new Intent(context,UpdateWidgetService.class);
	            	 //invoke service
	            	 context.startService(in);
	        		}
	        }
		
	}
	
	
	
}
