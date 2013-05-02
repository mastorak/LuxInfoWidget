package lu.mastorak.luxinfowidget.managers;

import android.util.Log;
import lu.mastorak.luxinfowidget.content.GigData;
import lu.mastorak.luxinfowidget.util.Constants;
import lu.mastorak.luxinfowidget.util.LocalContent;
import lu.mastorak.luxinfowidget.util.RemoteContent;
import lu.mastorak.luxinfowidget.util.AppResources;
import lu.mastorak.luxinfowidget.widget.R;

/**
 * A class to manage the gig item 
 * @author Konstantinos Mastorakis
 *
 */
public class GigManager {

	private LocalContent localContent;
	
	/**
	 * The default constructor
	 */
	public GigManager(){
		
		localContent=new LocalContent();
	}
	
	public GigData getNextRockhalGig(){
		
		GigData data =null;
		String page=null;
		String feedUrl= AppResources.getStringResource(R.string.rockhal_agenda_url);
		String title=null;
		String url=null;
		String date=null;
		
		
		RemoteContent remoteContentManager = new RemoteContent();
		page=remoteContentManager.getRemoteTextContent(feedUrl);
		
		if(page!=null){
			//find where the entries start
			int startItems=page.indexOf("<div class=\"news-agenda-text\">");
			if(startItems!=-1){
				page=page.substring(startItems);
				page=page.replaceFirst("<div class=\"news-agenda-text\">", "");
				
				//find the start of the info
				int startInfo=page.indexOf("<a href=\"");
				int endInfo=page.indexOf(">");
				if(startInfo!=-1 && endInfo!=-1 && endInfo>startInfo){
					String info=page.substring(startInfo,endInfo);
					info=info.replace("<a href=\"", "");
					int endOfUrl=info.indexOf("\"");
					//get the url
					if(endOfUrl!=-1 ){
						url=info.substring(0, endOfUrl);
						url=AppResources.getStringResource(R.string.rockhal_base_url)+url;
						Log.i(Constants.UTIL_LOG, "Gig url:"+url);
					}
					int startTitle=info.indexOf("title=\"");
					//get the title
					if(startTitle!=-1){
						title=info.substring(startTitle);
						title=title.replace("title=\"", "");
						title=title.replace("\"", "");
						Log.i(Constants.UTIL_LOG, "Gig title:"+title);
					}
					int dateStart=page.indexOf("<p class=\"news-list-date\">");
					int dateEnd=page.indexOf("</p>");
					//get date
					if(dateStart!=-1 &&dateEnd!=-1 && dateEnd>dateStart){
						date=page.substring(dateStart,dateEnd);
						//remove html tags
						date=date.replaceAll("<[^>]+>", "");
						Log.i(Constants.UTIL_LOG, "Gig date:"+date);
					}
					
				}
			}	
		}
		
		if(title!=null && date!=null && url!=null){
			data=new GigData(title, date, url);
		}else{
			data=new GigData(AppResources.getStringResource(R.string.not_available), "", "");
			Log.w(Constants.UTIL_LOG, "Could not get gig data");
		}
		
		localContent.storeGigData(data);
		return data;
	}
	
	
}
