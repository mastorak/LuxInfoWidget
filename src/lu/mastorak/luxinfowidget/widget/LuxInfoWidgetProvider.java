package lu.mastorak.luxinfowidget.widget;

import lu.mastorak.luxinfowidget.util.Constants;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * The main widget provider class
 * @author Konstantinos Mastorakis
 *
 */
public class LuxInfoWidgetProvider extends AppWidgetProvider {

 

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager,
      int[] appWidgetIds) {

    Log.w(Constants.WIDGET_LOG, "onUpdate called");
    //get widgets
    ComponentName thisWidget = new ComponentName(context,
        LuxInfoWidgetProvider.class);
    int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
    //build intent
    Intent intent = new Intent(context.getApplicationContext(),
        UpdateWidgetService.class);
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

    // Update the widget via the service
    context.startService(intent);
  }
} 