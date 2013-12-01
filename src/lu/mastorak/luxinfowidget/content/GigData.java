package lu.mastorak.luxinfowidget.content;

/**
 *  The gig data object
 * @author Konstantinos Mastorakis
 *
 */
public class GigData {

	private String title;
	private String date;
	private String url;
	
	/**
	 * The GigData constructor 
	 * @param String name
	 * @param String date
	 * @param String url
	 */
	public GigData(String title, String date, String url) {
		this.title = title;
		this.date = date;
		this.url = url;
	}

	/**
	 * 
	 * @return String
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 
	 * @return String
	 */
	public String getDate() {
		return date;
	}

	/**
	 * 
	 * @return String
	 */
	public String getUrl() {
		return url;
	}

	
	
}
