package lu.mastorak.luxinfowidget.managers;

import lu.mastorak.luxinfowidget.content.RssData;
import lu.mastorak.luxinfowidget.util.Constants;
import lu.mastorak.luxinfowidget.util.AppResources;
import lu.mastorak.luxinfowidget.util.LocalContent;
import lu.mastorak.luxinfowidget.widget.R;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.util.Log;

/**
 * A class to manage rss feed items
 * @author Konstantinos Mastorakis
 *
 */
public class RssManager {

	private LocalContent localContent;
	/**
	 * The default constructor
	 */
	public RssManager(){
		
		localContent=new LocalContent();
	}
	
	/**
	 * A method to retrieve the rss feed data of the latest information item from an rss feed
	 * @param Document doc the document containing the  rss feed
	 * @return RssData The data of the  rss item
	 */
	public RssData getLatestItemRssData(Document doc){
		
		RssData data=null;
		String title=null;
		String url=null;
		String description=null;

		if (doc != null)
		{
			// go to document -> rss node -> channel node
			Node firstChild = doc.getFirstChild();
			Node feedItem = null;
			if (firstChild != null)
			{
				feedItem = firstChild.getChildNodes().item(1);
				if (feedItem != null)
				{
					// get the children (contains feed info and news items)
					NodeList info = feedItem.getChildNodes();

					// iterate through the nodes under channel node
					for (int i = 0; i < info.getLength(); i++)
					{
						Node node = info.item(i);
						// after feed info the item nodes start on the same level
						if (node != null && node.getNodeName().equals("item"))
						{
							NodeList infoList = node.getChildNodes();
							// iterate through the info nodes of individual info item nodes
							for (int j = 0; j < infoList.getLength(); j++)
							{
								Node infoNode = infoList.item(j);
								
								//get the title
								if (infoNode != null && infoNode.getNodeName().equals("title"))
								{
									title = infoNode.getTextContent();
									
								}
								
								//get the url
								if (infoNode != null && infoNode.getNodeName().equals("link"))
								{
									url = infoNode.getTextContent();
									
								}
								
								//get the description
								if (infoNode != null && infoNode.getNodeName().equals("description"))
								{
									description = infoNode.getTextContent();
									if(description.length()>100){
										description=description.substring(0, 100);
										description+="...";
									}
								}
							}
							break;
						}
					}
				}
			}
			
			if(title!=null && url!=null && description!=null){
				data=new RssData(title, url, description);
			}else{
				Log.w(Constants.UTIL_LOG, "Rss data object could not be created");
			}
		}
		
		return data;
		
	}
	
	
	public RssData getRssData(Document doc){
		
		RssData data= getLatestItemRssData(doc);
		
		//create the rss data object
		if (data==null|| data.getTitle().equals("") || data.getUrl().equals("")){
			data=new RssData(AppResources.getStringResource(R.string.not_available), "", "");
		}
		
		Log.i(Constants.UTIL_LOG, "rss title:"+data.getTitle());
		Log.i(Constants.UTIL_LOG, "rss description:"+data.getDescription());
		Log.i(Constants.UTIL_LOG, "rss url:"+data.getUrl());
		
		//store weather data
		localContent.storeRssData(data);
		
		return data;
	}
	
}
