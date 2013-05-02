package lu.mastorak.luxinfowidget.content;

/**
 * The weather data object
 * @author Konstantinos Mastorakis
 *
 */
public class WeatherData {
	
	private String temperature;
	private String high;
	private String low;
	private String description;
	private String tomorrowHigh;
	private String tomorrowLow;
	private String tomorrowDescription;
	private String imageUrl;
	
	/**
	 * The WeatherData constructor
	 * @param String temperature
	 * @param String high
	 * @param String low
	 * @param String description
	 * @param String tomorrowHigh
	 * @param String tomorrowLow
	 * @param String tomorrowDescription
	 * @param String imageUrl
	 */
	public WeatherData(String temperature, String high, String low,
			String description, String tomorrowHigh, String tomorrowLow,
			String tomorrowDescription, String imageUrl) {
		
		this.temperature = temperature;
		this.high = high;
		this.low = low;
		this.description = description;
		this.tomorrowHigh = tomorrowHigh;
		this.tomorrowLow = tomorrowLow;
		this.tomorrowDescription = tomorrowDescription;
		this.imageUrl = imageUrl;
	}

	/**
	 * 
	 * @return String
	 */
	public String getTemperature() {
		return temperature;
	}
	
	/**
	 * 
	 * @return String
	 */
	public String getHigh() {
		return high;
	}
	
	/**
	 * 
	 * @return String
	 */
	public String getLow() {
		return low;
	}

	/**
	 * 
	 * @return String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @return String
	 */
	public String getTomorrowHigh() {
		return tomorrowHigh;
	}

	/**
	 * 
	 * @return String
	 */
	public String getTomorrowLow() {
		return tomorrowLow;
	}

	/**
	 * 
	 * @return String
	 */
	public String getTomorrowDescription() {
		return tomorrowDescription;
	}

	/**
	 * 
	 * @return String
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	

	
	
}
