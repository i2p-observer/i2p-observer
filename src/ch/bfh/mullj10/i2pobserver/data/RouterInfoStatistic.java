package ch.bfh.mullj10.i2pobserver.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

/**
 * Data class to store all RouterInfoDatasets of every program run. Also creates
 * an average Dataset over all entries.
 * 
 * @author mullj10
 *
 */
public class RouterInfoStatistic implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String datasetDate;
	private List<RouterInfoDataset> datasets = new ArrayList<>();
	private boolean monthlyStatistic = false;

	/**
	 * Create a new statistic.
	 * 
	 * @param monthlyStatistic
	 *            Specify, if statistic is a monthly one
	 */
	public RouterInfoStatistic(String date, boolean monthlyStatistic) {
		this.datasetDate = date;
		this.monthlyStatistic = monthlyStatistic;
	}

	public void addDataset(String hourlyDate, int numberOfEntries, boolean processingError,
			Map<String, Integer> netDbInfo, Map<String, Integer> versions, Map<String, Integer> countries) {
		datasets.add(
				new RouterInfoDataset(hourlyDate, numberOfEntries, processingError, netDbInfo, versions, countries));
	}

	/**
	 * Replace an entry if one with the same entry date already exists, else add
	 * it to the list.
	 * 
	 * @param rid
	 *            new RouterInfoDataset
	 */
	public void replaceDataset(RouterInfoDataset rid) {
		for (int i = 0; i < datasets.size(); i++) {
			if (datasets.get(i).getEntryDate().equals(rid.getEntryDate())) {
				datasets.remove(i);
			}
		}
		datasets.add(rid);
	}

	public List<RouterInfoDataset> getDatasets() {
		return datasets;
	}

	public String getDatasetDate() {
		return datasetDate;
	}

	/**
	 * Indicates whether the statistic is a daily or a monthly one
	 * 
	 * @return true: the statistic is a monthly one; false: the statistic is a
	 *         daily one
	 */
	public boolean isMonthlyStatistic() {
		return monthlyStatistic;
	}

	/**
	 * Compute the averages over all datasets and return them as
	 * RouterInfoDataset.
	 * 
	 * @param date
	 *            Date to be added to the RouterInfoDataset that is returned.
	 * @return RouterInfoDataset that contains the averages of all values and
	 *         the date given as parameter.
	 */
	public RouterInfoDataset buildAverage(String date) {

		int entries = 0;
		int numberOfErrors = 0;
		boolean processingError = false;
		for (RouterInfoDataset rid : datasets) {
			entries += rid.getNumberOfEntries();
			if (rid.hadProcessingError()) {
				numberOfErrors++;
				if (numberOfErrors > 2) {
					processingError = true;
				}
			}
		}

		return new RouterInfoDataset(date, Integer.divideUnsigned(entries, datasets.size()), processingError,
				computeAverage("netDbInfos"), computeAverage("versions"), computeAverage("countries"));
	}

	/**
	 * Compute the average for the Map of RouterInfoDataset specified by the
	 * parameter name.
	 * 
	 * @param name
	 *            PSecify which Map to use
	 * @return new Map containing the average values.
	 */
	private Map<String, Integer> computeAverage(String name) {

		Map<String, Integer> result = new TreeMap<>();
		Set<String> content = new TreeSet<>();

		// collect all keys of the map
		for (RouterInfoDataset rid : datasets) {
			Iterator<Entry<String, Integer>> iterator = getIterator(rid, name);
			while (iterator.hasNext()) {
				content.add(iterator.next().getKey());
			}
		}

		// iterate over all collected keys and collect entry values
		Iterator<String> it = content.iterator();

		while (it.hasNext()) {

			String currentEntry = it.next();
			if (result.get(currentEntry) == null) {
				result.put(currentEntry, 0);
			}

			for (RouterInfoDataset rid : datasets) {
				int value = getValue(currentEntry, rid, name);
				if (value != 0) {
					result.put(currentEntry, result.get(currentEntry) + value);
				}
			}
		}

		// iterate over result and build average
		Iterator<Entry<String, Integer>> iterator = result.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<String, Integer> entry = iterator.next();
			int value = Integer.divideUnsigned(entry.getValue(), datasets.size());
			entry.setValue(value);
		}

		return result;
	}

	private int getValue(String currentEntry, RouterInfoDataset rid, String name) {

		int value = 0;

		switch (name) {
		case "netDbInfos":
			if (rid.getNetDbInfos().get(currentEntry) != null) {
				value = rid.getNetDbInfos().get(currentEntry);
			}
			break;

		case "versions":
			if (rid.getVersions().get(currentEntry) != null) {
				value = rid.getVersions().get(currentEntry);
			}
			break;

		case "countries":
			if (rid.getCountries().get(currentEntry) != null) {
				value = rid.getCountries().get(currentEntry);
			}
			break;
		}

		return value;
	}

	private Iterator<Entry<String, Integer>> getIterator(RouterInfoDataset rid, String name) {

		Iterator<Entry<String, Integer>> iterator = null;

		switch (name) {
		case "netDbInfos":
			iterator = rid.getNetDbInfos().entrySet().iterator();
			break;

		case "versions":
			iterator = rid.getVersions().entrySet().iterator();
			break;

		case "countries":
			iterator = rid.getCountries().entrySet().iterator();
			break;
		}

		return iterator;
	}

}