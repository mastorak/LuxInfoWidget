package lu.mastorak.luxinfowidget.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * A util class to get context based resources
 *  
 * @author Konstantinos Mastorakis
 *
 */
public class AppResources {

	private static Context context;
	
	/**
	 * A method to set the context
	 * In the Service onCreate context is set
	 * In the Service on destroy is set to null 	
	 * @param Context context
	 */
	public static void setContext(Context context)
	{
	  AppResources.context = context;
	}

	/**
	 * Get the String value of the request resource 
	 * @param int id
	 * @return String
	 */
	public static String getStringResource(int id){
		
		return AppResources.context.getString(id);
	}
	
	/**
	 * Get the local storage  shared preferences
	 * @param String prefs
	 * @return
	 */
	public static SharedPreferences getSharedPreferences(String prefs){
		return AppResources.context.getSharedPreferences(prefs, 0);
	}

	/**
	 * A method to retrieve the App context
	 * @return Context
	 */
	public static Context getContext() {
		return context;
	}
	
	
	
}
