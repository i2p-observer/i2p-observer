package ch.bfh.mullj10.i2pobserver.data;

import java.util.Map;

/**
 * Data class for storing the dataset of an program run (date, netDbInfos, versions and countries) 
 * @author mullj10
 *
 */
public class RouterInfoDataset implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String entryDate;
	private int numberOfEntries;
	private boolean processingError;
	private Map<String, Integer> netDbInfos;
	private Map<String, Integer> versions;
	private Map<String, Integer> countries;
	
	public RouterInfoDataset(String date, int numberOfEntries, boolean processingError, Map<String, Integer> netDbInfos, Map<String, Integer> versions,
			Map<String, Integer> countries) {
		super();
		this.entryDate = date;
		this.numberOfEntries = numberOfEntries;
		this.processingError = processingError;
		this.netDbInfos = netDbInfos;
		this.versions = versions;
		this.countries = countries;
	}

	public String getEntryDate() {
		return entryDate;
	}
	
	public int getNumberOfEntries(){
		return numberOfEntries;
	}
	
	public boolean hadProcessingError(){
		return processingError;
	}
	
	public Map<String, Integer> getNetDbInfos() {
		return netDbInfos;
	}

	public Map<String, Integer> getVersions() {
		return versions;
	}

	public Map<String, Integer> getCountries() {
		return countries;
	}	
}
