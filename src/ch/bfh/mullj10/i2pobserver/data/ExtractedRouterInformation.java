package ch.bfh.mullj10.i2pobserver.data;

import java.util.List;

/**
 * Data class to hold information about an I2P router to create statistics.
 * @author mullj10
 *
 */
public class ExtractedRouterInformation {

	private String hash;
	private List<String> ipaddresses;
	private boolean isFloodfil;
	private int knownLeaseSets;
	private int knownRouters;
	private String routerVersion;
	
	
	public ExtractedRouterInformation(String hash, List<String> ipaddresses, boolean isFloodfil,
			int knownLeaseSets, int knownRouters, String routerVersion) {
		super();
		this.hash = hash;
		this.ipaddresses = ipaddresses;
		this.isFloodfil = isFloodfil;
		this.knownLeaseSets = knownLeaseSets;
		this.knownRouters = knownRouters;
		this.routerVersion = routerVersion;
	}

	public String getHash() {
		return hash;
	}

	public List<String> getIpaddresses() {
		return ipaddresses;
	}

	public boolean isFloodfil() {
		return isFloodfil;
	}

	public int getKnownLeaseSets() {
		return knownLeaseSets;
	}
	
	public int getKnownRouters() {
		return knownRouters;
	}
	
	public String getRouterVersion() {
		return routerVersion;
	}
	
	public String toString(){
		return ("RouterInfo: Hash=" + hash + ", address=" + ipaddresses.toString() +  
				", floodfilRouter=" + isFloodfil + ", knownLeaseSets=" + knownLeaseSets + 
				", knownRouters=" + knownRouters +", routerVersion=" + routerVersion);
	}
}
