package ch.bfh.mullj10.i2pobserver.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.bfh.mullj10.i2pobserver.RouterInfoAnalyzer;
import ch.bfh.mullj10.i2pobserver.data.NetDbEntries;
import ch.bfh.mullj10.i2pobserver.data.ObserverProperties;
import ch.bfh.mullj10.i2pobserver.data.RouterInfoDataset;
import ch.bfh.mullj10.i2pobserver.data.RouterInfoStatistic;
import net.i2p.data.Hash;
import net.i2p.data.router.RouterAddress;
import net.i2p.util.OrderedProperties;

public class RouterInfoAnalyzerTest {

	// mock RouterInfo class
	private class RouterInfo extends net.i2p.data.router.RouterInfo {

		private static final long serialVersionUID = 1L;
		private RouterIdentity identity;
		private Collection<net.i2p.data.router.RouterAddress> addresses;
		private String options_caps;
		private String options_leaseSets;
		private String options_knownRouters;
		private String version;

		public RouterInfo(RouterIdentity identity, Collection<RouterAddress> addresses, String options_caps,
				String options_leaseSets, String options_knownRouters, String version) {
			super();
			this.identity = identity;
			this.addresses = addresses;
			this.options_caps = options_caps;
			this.options_leaseSets = options_leaseSets;
			this.options_knownRouters = options_knownRouters;
			this.version = version;
		}

		public RouterIdentity getIdentity() {
			return identity;
		}

		public Collection<net.i2p.data.router.RouterAddress> getAddresses() {
			return addresses;
		}

		public String getOption(String option) {
			switch (option) {
			case "caps":
				return options_caps;
			case "netdb.knownLeaseSets":
				return options_leaseSets;
			case "netdb.knownRouters":
				return options_knownRouters;
			}
			return null;
		}

		public String getVersion() {
			return version;
		}
	}

	// mock RouterIdentity class
	private class RouterIdentity extends net.i2p.data.router.RouterIdentity {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Hash getHash() {
			return Hash.FAKE_HASH;
		}
	}

	// end class mocking

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAnalyzeRouterInfo() {

		// create 2 RouterInfo for testing
		RouterIdentity routerIdentity = new RouterIdentity();
		List<RouterAddress> addresses = new ArrayList<>();
		OrderedProperties orderedProperties = new OrderedProperties();
		orderedProperties.put("PROP_HOST", "1.2.3.4");
		RouterAddress routerAddress = new RouterAddress(null, orderedProperties, 1);
		orderedProperties.put("PROP_HOST", "5.6.7.8");
		RouterAddress routerAddress2 = new RouterAddress(null, orderedProperties, 1);
		addresses.add(routerAddress);
		addresses.add(routerAddress2);
		String options_caps = "AB";
		String options_leaseSets = "0";
		String options_knownRouters = "0";
		String version = "1";
		RouterInfo normalRouter = new RouterInfo(routerIdentity, addresses, options_caps, options_leaseSets,
				options_knownRouters, version);
		String options_caps2 = "fG";
		String options_leaseSets2 = "10";
		String options_knownRouters2 = "20";
		String version2 = "2";
		RouterInfo floodfilRouter = new RouterInfo(routerIdentity, addresses, options_caps2, options_leaseSets2,
				options_knownRouters2, version2);

		Set<net.i2p.data.router.RouterInfo> entries = new HashSet<>();
		entries.add((RouterInfo) normalRouter);
		entries.add((RouterInfo) floodfilRouter);

		NetDbEntries netDbEntries = new NetDbEntries(entries, false);
		RouterInfoStatistic routerInfoStatistic = new RouterInfoStatistic("12", false);
		ObserverProperties observerProperties = new ObserverProperties();
		RouterInfoAnalyzer routerInfoAnalyzer = new RouterInfoAnalyzer(observerProperties, netDbEntries,
				routerInfoStatistic);

		routerInfoAnalyzer.analyzeRouterInfo();

		List<RouterInfoDataset> routerInfoDatasets = routerInfoStatistic.getDatasets();
		assertTrue(routerInfoDatasets.size() == 1);
		assertTrue(routerInfoDatasets.get(0).getNetDbInfos()
				.get(observerProperties.getFLOODFIL_ROUTERS_DENOTATION()) == 1);
		assertTrue(
				routerInfoDatasets.get(0).getNetDbInfos().get(observerProperties.getKNOWN_ROUTERS_DENOTATION()) == 20);
		assertTrue(routerInfoDatasets.get(0).getNetDbInfos().get(observerProperties.getLEASESETS_DENOTATION()) == 10);
		assertTrue(routerInfoDatasets.get(0).getVersions().containsKey("1"));
		assertTrue(routerInfoDatasets.get(0).getVersions().get("1") == 1);
		assertTrue(routerInfoDatasets.get(0).getVersions().containsKey("2"));
		assertTrue(routerInfoDatasets.get(0).getVersions().get("2") == 1);
		assertTrue(routerInfoDatasets.get(0).getCountries().size() == 1);
	}
}
