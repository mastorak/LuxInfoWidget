package lu.mastorak.luxinfowidget.content;

/**
 * The vdl information object  
 * @author Konstantinos Mastorakis
 *
 */
public class VdlData {

	private String title;
	private String url;
	private String description;
	
	/**
	 * VdlData constructor
	 * @param String title
	 * @param String url
	 * @param String description
	 */
	public VdlData(String title, String url,String description) {
		
		this.title = title;
		this.url = url;
		this.description=description;
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
	public String getUrl() {
		return url;
	}
	
	/**
	 * 
	 * @return String
	 */
	public String getDescription() {
		return description;
	}
	
	
	
	
}
