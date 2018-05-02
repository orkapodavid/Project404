package core.comp3111;

/**
 * The Chart interface provides two methods for access to titles of charts.
 * 
 * @author OR Ka Po, kpor
 * @see LineChartClass
 * @see PieChartClass
 */
public interface Chart {
	/**
	 * Set the title of a chart.
	 * @param m_title title of a chart
	 */
	public void setTitle(String m_title);
	
	/**
	 * Return the title of a chart.
	 * @return String or Empty String
	 */
	public String getTitle();
}
