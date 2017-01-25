package ch.bfh.mullj10.i2pobserver.data.test;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ch.bfh.mullj10.i2pobserver.data.RouterInfoDataset;
import ch.bfh.mullj10.i2pobserver.data.RouterInfoStatistic;

public class RouterInfoStatisticTest {
	
	private RouterInfoStatistic routerInfoStatistic;

	@Before
	public void setUp() throws Exception {
		routerInfoStatistic = new RouterInfoStatistic(null, true);
	}
	
	@Test
	public void testReplaceDataset(){
		Map<String, Integer> entry = new HashMap<String, Integer>();
		entry.put("1", 2);
		routerInfoStatistic.addDataset("1", 1, false, entry, entry, entry);
		Map<String, Integer> newEntry = new HashMap<String, Integer>();
		newEntry.put("1", 4);
		routerInfoStatistic.replaceDataset(new RouterInfoDataset("1", 1, false, newEntry, newEntry, newEntry));
		routerInfoStatistic.replaceDataset(new RouterInfoDataset("2", 1, false, newEntry, newEntry, newEntry));
		List<RouterInfoDataset> entries = routerInfoStatistic.getDatasets();
		// Test entry dates
		assertTrue(Integer.valueOf(entries.get(0).getEntryDate()) == 1);
		assertTrue(Integer.valueOf(entries.get(1).getEntryDate()) == 2);
		//test if entry was replaced correctly
		assertTrue(entries.get(0).getNetDbInfos().get("1") == 4);
		assertTrue(entries.get(0).getVersions().get("1") == 4);
		assertTrue(entries.get(0).getCountries().get("1") == 4);
		// test if second entry was added correctly
		assertTrue(entries.get(1).getNetDbInfos().get("1") == 4);
		assertTrue(entries.get(1).getVersions().get("1") == 4);
		assertTrue(entries.get(1).getCountries().get("1") == 4);
	}
	
	@Test
	public void testBuildAverage() {
		Map<String, Integer> entry = new HashMap<String, Integer>();
		entry.put("1", 2);
		routerInfoStatistic.addDataset("1", 1, false, entry, entry, entry);
		// average over single entry
		RouterInfoDataset average = routerInfoStatistic.buildAverage("1");
		assertTrue(average.getNetDbInfos().size() == 1);
		assertTrue(average.getNetDbInfos().get("1") == 2);
		assertTrue(average.getVersions().size() == 1);
		assertTrue(average.getVersions().get("1") == 2);
		assertTrue(average.getCountries().size() == 1);
		assertTrue(average.getCountries().get("1") == 2);
		Map<String, Integer> newEntry = new HashMap<String, Integer>();
		newEntry.put("1", 5);
		routerInfoStatistic.addDataset("2", 1, false, newEntry, newEntry, entry);
		// average over two entries with different values (even and odd)
		RouterInfoDataset average2 = routerInfoStatistic.buildAverage("1");
		assertTrue(average2.getNetDbInfos().size() == 1);
		assertTrue(average2.getNetDbInfos().get("1") == 3);
		assertTrue(average2.getVersions().size() == 1);
		assertTrue(average2.getVersions().get("1") == 3);
		assertTrue(average2.getCountries().size() == 1);
		assertTrue(average2.getCountries().get("1") == 2);
	}
}
