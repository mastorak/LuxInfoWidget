package lu.mastorak.luxinfowidget.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import lu.mastorak.luxinfowidget.managers.GigManager;
import lu.mastorak.luxinfowidget.managers.RssManager;
import lu.mastorak.luxinfowidget.managers.VdlManager;
import lu.mastorak.luxinfowidget.managers.WeatherManager;
import lu.mastorak.luxinfowidget.widget.R;


import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * Class to retrieve and format remote content
 * @author Konstantinos Mastorakis
 *
 */
public class RemoteContent {

	/**
	 * Default constructor
	 */
	public RemoteContent(){
		
	}
	
	/**
	 * Method to retrieve remote text content 
	 * @param String url The url of the remote text content
	 * @return String The remote  content string
	 */
	public String getRemoteTextContent(String url){
		
		String content=null;
		
		HttpClient httpClient = new DefaultHttpClient();
	     HttpGet httpGet = new HttpGet(url);
	  
	    
	      try {
			HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();
			
			  if (httpEntity != null){
			   InputStream inputStream = httpEntity.getContent();
			   Reader in = new InputStreamReader(inputStream);
			   BufferedReader bufferedreader = new BufferedReader(in);
			   StringBuilder stringBuilder = new StringBuilder();
			 
			   String stringReadLine = null;

			   while ((stringReadLine = bufferedreader.readLine()) != null) {
			    stringBuilder.append(stringReadLine + "\n");
			   }
			 
			   content = stringBuilder.toString();
			  }
		} catch (ClientProtocolException e) {
			Log.e(Constants.UTIL_LOG, "Error retrieving remote content from url:"+url, e);			
		} catch (IllegalStateException e) {
			Log.e(Constants.UTIL_LOG, "Error retrieving remote content from url:"+url, e);			
		} catch (IOException e) {
			Log.e(Constants.UTIL_LOG, "Error retrieving remote content from url:"+url, e);			
		}
		
		return content;		
	}
	
	/**
	 * Method to build an xml document from a string 
	 * @param String The feed-string that contains the xml data
	 * @return Document The xml document 
	 */
	private Document stringToXmlDoc(String feed){
		
		  Document document = null;
		  DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		  DocumentBuilder builder;

		  try {
			    builder = builderFactory.newDocumentBuilder();
				document = builder.parse(new ByteArrayInputStream(feed.getBytes()));
				} catch (ParserConfigurationException e) {
					Log.e(Constants.UTIL_LOG, "Error creating xml document", e);			
				} catch (SAXException e) {
					Log.e(Constants.UTIL_LOG, "Error creating xml document", e);	
				} catch (IOException e) {
					Log.e(Constants.UTIL_LOG, "Error creating xml document", e);	
				}

		  return document;
		 
	}
	
	/**
	 * A method to get a remote text content and build an xml document from it 
	 * @param String url The url of the remote text content
	 * @return Document The xml document 	 */
	public Document getXmlContent(String url){
		
		Document document=null;
		String feed=null;
		
		if (url != null) {
			feed = getRemoteTextContent(url);
			if (feed != null) {
				document = stringToXmlDoc(feed);
			}
		}

		return document;
		
	}
	
	/**
	 * A method to retrieve content from the network as ByteArrayBuffer
	 * @param String urlString
	 * @return ByteArrayBuffer
	 */
	public ByteArrayBuffer getRemoteContent(String urlString){
		
		ByteArrayBuffer baf =null;
		
		try {
			URL url=new URL(urlString);
			
			URLConnection ucon = url.openConnection();
			
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			
			baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1) {
			        baf.append((byte) current);
			}
		} catch (MalformedURLException e) {
			Log.e(Constants.UTIL_LOG, "Error creating url:"+urlString, e);
		} catch (IOException e) {
			Log.e(Constants.UTIL_LOG, "Error retrieving content:"+urlString, e);
		}
        
        return baf;
	}
	
	/**
	 * A method to retrieve an image from the network 
	 * 
	 * @param String urlString
	 * @return Bitmap
	 */
	public Bitmap getRemoteImage(String urlString){
		Bitmap image=null;
		
		try {
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();
			image = BitmapFactory.decodeStream(conn.getInputStream());
		} catch (MalformedURLException e) {
			Log.e(Constants.UTIL_LOG, "Error creating url:"+urlString, e);
		} catch (IOException e) {
			Log.e(Constants.UTIL_LOG, "Error retrieving remote image:"+urlString, e);
		}
		
        return image;
	}
	
	/**
	 * A method to check if the device is connected to the network 
	 * @return boolean
	 */
	public static boolean isOnline() {
		
		boolean isConnected=false;
		ConnectivityManager connectivityManager =(ConnectivityManager) AppResources.getContext().getSystemService(AppResources.getContext().CONNECTIVITY_SERVICE);
	    NetworkInfo info = connectivityManager.getActiveNetworkInfo();
	    
	    if (info != null && info.isConnected()) {
	        isConnected=true;
	    }
	    return isConnected;
	}
	
	
	
}
