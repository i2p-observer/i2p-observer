package ch.bfh.mullj10.i2pobserver.data;

import java.util.Set;

import net.i2p.data.router.RouterInfo;

public class NetDbEntries {

	private Set<RouterInfo> netDbEntries;
	private boolean processingError;
	
	public NetDbEntries(Set<RouterInfo> netDbEntries, boolean processingError) {
		super();
		this.netDbEntries = netDbEntries;
		this.processingError = processingError;
	}

	public Set<RouterInfo> getNetDbEntries() {
		return netDbEntries;
	}

	public boolean hadProcessingError() {
		return processingError;
	}
}
