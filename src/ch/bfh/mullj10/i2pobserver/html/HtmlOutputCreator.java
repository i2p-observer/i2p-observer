package ch.bfh.mullj10.i2pobserver.html;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import ch.bfh.mullj10.i2pobserver.data.ObserverProperties;
import ch.bfh.mullj10.i2pobserver.data.RouterInfoDataset;
import ch.bfh.mullj10.i2pobserver.data.RouterInfoStatistic;

/**
 * Create a HTML page that lists the collected data
 * 
 * @author mullj10
 *
 */
public class HtmlOutputCreator {

	private ObserverProperties observerProperties;
	private StringBuilder page;
	private List<RouterInfoDataset> routerInfoDatasets;
	private String date;
	private HtmlUploader htmlUploader;

	public HtmlOutputCreator(ObserverProperties observerProperties) {
		this.observerProperties = observerProperties;
		this.htmlUploader = new HtmlUploader(observerProperties);
	}
	
	/**
	 * Create the HTML page with charts and tables for specified RouterInfoStatistic
	 * @param routerInfoStatistic data base used to create page
	 */
	public void buildStatisticPage(RouterInfoStatistic routerInfoStatistic) {

		routerInfoDatasets = routerInfoStatistic.getDatasets();

		// define correct date
		if (routerInfoStatistic.isMonthlyStatistic()) {
			date = observerProperties.getMonthlyDate();
		} else {
			date = observerProperties.getDailyDate();
		}

		// start constructing the page
		writePage(createHTML(routerInfoStatistic), (date + ".html"));
		
		// build overview page if necessary
		if (routerInfoDatasets.size() == 1) {
			// first run of the day, create new entry on overview page
			if (routerInfoStatistic.isMonthlyStatistic()) {
				buildOverviewPage(routerInfoStatistic.getDatasetDate().substring(0, 4),
						Integer.valueOf(routerInfoStatistic.getDatasetDate().substring(5)),
						observerProperties.getPAGENAME_MONTHLY_OVERVIEW());
				if ((Integer.valueOf(observerProperties.getDailyDate().substring(8)) == 1)
						&& (Integer.valueOf(date.substring(5)) == 1)) {
					// first day of the year: create archive_last_year with
					// monthly statistic site of last year
					String yearDate = String
							.valueOf((Integer.valueOf(routerInfoStatistic.getDatasetDate().substring(0, 4)) - 1));
					buildOverviewPage(yearDate, 12, "archive_last_year.html");
				}
			} else {
				buildOverviewPage(routerInfoStatistic.getDatasetDate().substring(0, 7),
						Integer.valueOf(routerInfoStatistic.getDatasetDate().substring(8)),
						observerProperties.getPAGENAME_DAILY_OVERVIEW());
				if (Integer.valueOf(date.substring(8)) == 1) {
					// first run on the first day of month: create
					// archive_last_month with daily statistics site of last
					// month
					int month = Integer.valueOf(routerInfoStatistic.getDatasetDate().substring(6, 7)) - 1;
					int year = Integer.valueOf(routerInfoStatistic.getDatasetDate().substring(0, 4));
					if (month == 0) {
						// currently January, revert to December of last year 
						month = 12;
						year = year - 1;
					}
					String monthDate = year + "-" + String.format("%02d", month);
					buildOverviewPage(monthDate, 31, "archive_last_month.html");
				}
			}
		}
	}

	/**
	 * create an empty String and add top part (fixed from ObserverProperties), the body, the bottom (fix) and the code for the charts 
	 * @param routerInfoStatistic data for the site
	 * @return complete page
	 */
	private StringBuilder createHTML(RouterInfoStatistic routerInfoStatistic) {

		page = new StringBuilder();

		page.append(observerProperties.getPAGE_TOP());
		page.append("<h1>" + date + "</h1>\n");
		createBody(routerInfoStatistic);
		page.append(observerProperties.getPAGE_BOTTOM());

		// code for chart
		page.append(
				"<script src=\"../theme/morrisjs/morris.min.js\"></script>\n<script src=\"../theme/morrisjs/raphael-min.js\"></script>");
		page.append("\n\n" + buildChartData(routerInfoStatistic) + "\n");
		page.append("\n</body>\n</html>");
		
		return page;
	}

	/**
	 * create the body part of the webpage. 
	 * @param routerInfoStatistic
	 */
	private void createBody(RouterInfoStatistic routerInfoStatistic) {
		
		// code for chart
		page.append(
				"<div class=\"panel panel-default\">\n<div class=\"panel-heading\">\nNetDB entries\n</div>\n<!-- /.panel-heading -->\n<div class=\"panel-body\">\n<div id=\"line-chart-complete\"></div>\n</div>\n<!-- /.panel-body -->\n</div>");
		page.append(
				"<div class=\"panel panel-default\">\n<div class=\"panel-heading\">\nRouters in NetDB\n</div>\n<!-- /.panel-heading -->\n<div class=\"panel-body\">\n<div id=\"line-chart-router\"></div>\n</div>\n<!-- /.panel-body -->\n</div>");
		page.append("\nPlease find a short explanation of the items on the <a href=\"../help.html\"> Help page</a>.<br>Entries marked with (!) may be more inaccurate as there were errors when importing the netDB files or processing them.\n");
		// add the tables
		createNetDbTable();
		createVersionsTable();
		createCountriesTable();

	}

	/**
	 * create the table for the netDB entries
	 */
	private void createNetDbTable() {

		page.append("<h2>NetDB Entries</h2>\n");
		page.append("<table>\n");
		page.append("<thead>\n");
		page.append("<tr>");
		page.append("<th>Time</th>");
		page.append("<th>Analyzed NetDB Entries</th>");
		page.append("<th>Routers (max.)</th>");
		page.append("<th>Floodfil Routers</th>");
		page.append("<th>known leaseSets</th>");
		page.append("</tr>\n");
		page.append("</thead>\n");
		page.append("<tbody>\n");

		for (RouterInfoDataset rid: routerInfoDatasets){
			page.append("<tr>");
			page.append("<td>" + rid.getEntryDate());
			if (rid.hadProcessingError()){
				page.append("<b>(!)</b>");
			}
			page.append("</td>");
			page.append("<td>" + rid.getNumberOfEntries() + "</td>");
			page.append("<td>"
					+ rid.getNetDbInfos().get(observerProperties.getKNOWN_ROUTERS_DENOTATION())
					+ "</td>");
			page.append("<td>"
					+ rid.getNetDbInfos().get(observerProperties.getFLOODFIL_ROUTERS_DENOTATION())
					+ "</td>");
			page.append(
					"<td>" + rid.getNetDbInfos().get(observerProperties.getLEASESETS_DENOTATION())
							+ "</td>");
			page.append("</tr>\n");
		}
		page.append("</tbody>\n");
		page.append("</table>\n\n");
	}

	/**
	 * create the table for the versions.
	 */
	private void createVersionsTable() {

		page.append("<h2>Versions</h2>\n");
		page.append("<table>\n");
		page.append("<thead>\n");
		page.append("<tr>");
		page.append("<th>Version</th>\n");

		Set<String> versions = new TreeSet<>();

		for (RouterInfoDataset rid : routerInfoDatasets) {
			page.append("<th>" + rid.getEntryDate());
			if (rid.hadProcessingError()){
				page.append("<b>(!)</b>");
			}
			page.append("</th>");

			// collect versions of all entries
			Iterator<Entry<String, Integer>> it = rid.getVersions().entrySet().iterator();
			while (it.hasNext()) {
				versions.add(it.next().getKey());
			}
		}

		page.append("</tr>\n");
		page.append("</thead>\n");
		page.append("<tbody>\n");

		Iterator<String> it = versions.iterator();
		List<Integer> numberOfEntries = new ArrayList<>();
		numberOfEntries.add(0);

		while (it.hasNext()) {
			String currentVersion = it.next();

			page.append("<tr>");
			page.append("<td>" + currentVersion + "</td>\n");

			for (int i = 0; i < routerInfoDatasets.size(); i++) {

				int count = 0;

				if (numberOfEntries.size() <= i) {
					numberOfEntries.add(0);
				} else {
					count = numberOfEntries.remove(i);
				}

				Map<String, Integer> entry = routerInfoDatasets.get(i).getVersions();
				Integer value = entry.get(currentVersion);
				if (value != null) {
					page.append("<td>" + value + "</td>");
					count += value;
				} else {
					page.append("<th>0</th>");
				}
				numberOfEntries.add(i, count);
			}
			page.append("</tr>\n");
		}
		page.append("</tbody>\n");
		page.append("<tfoot>\n");
		page.append("<tr>");
		page.append("<th>Total</th>\n");
		for (int i = 0; i < routerInfoDatasets.size(); i++) {
			page.append("<th>" + numberOfEntries.get(i) + "</th>");
		}
		page.append("</tr>\n");

		page.append("</tfoot>\n");
		page.append("</table>\n\n");
	}

	/**
	 * create the table for countries.
	 */
	private void createCountriesTable() {

		page.append("<h2>Routers per Country</h2>\n");
		page.append("<table>\n");
		page.append("<thead>\n");
		page.append("<tr>");
		page.append("<th>Country</th>");

		Set<String> countries = new TreeSet<>();

		for (RouterInfoDataset rid : routerInfoDatasets) {
			page.append("<th>" + rid.getEntryDate());
			if (rid.hadProcessingError()){
				page.append("<b>(!)</b>");
			}
			page.append("</th>");

			// collect countries of all entries
			Iterator<Entry<String, Integer>> it = rid.getCountries().entrySet().iterator();
			while (it.hasNext()) {
				countries.add(it.next().getKey());

			}
		}

		page.append("</tr>\n");
		page.append("</thead>\n");
		page.append("<tbody>\n");

		Iterator<String> it = countries.iterator();
		List<Integer> numberOfEntries = new ArrayList<>();

		while (it.hasNext()) {
			String currentCountry = it.next();
			page.append("<tr>");
			page.append("<td>" + currentCountry + "</td>");

			for (int i = 0; i < routerInfoDatasets.size(); i++) {

				int count = 0;

				if (numberOfEntries.size() <= i) {
					numberOfEntries.add(0);
				} else {
					count = numberOfEntries.remove(i);
				}

				Map<String, Integer> entry = routerInfoDatasets.get(i).getCountries();
				Integer value = entry.get(currentCountry);
				if (value != null) {
					page.append("<td>" + value + "</td>");
					count += value;
				} else {
					page.append("<td>0</td>");
				}
				numberOfEntries.add(i, count);
			}
			page.append("</tr>\n");

		}

		page.append("</tbody>\n");
		page.append("<tfoot>\n");
		page.append("<tr>");
		page.append("<th>Total</th>");
		for (int i = 0; i < routerInfoDatasets.size(); i++) {
			page.append("<th>" + numberOfEntries.get(i) + "</th>");
		}
		page.append("</tr>\n");

		page.append("</tfoot>\n");
		page.append("</table>\n\n");
	}

	/**
	 * Write the given code to the specified filename and upload it.
	 * @param page HTML code used to create the webpage.
	 * @param filename name of the file where the page is written to.
	 */
	private void writePage(StringBuilder page, String filename) {
				
		try (FileWriter fstream = new FileWriter(observerProperties.getDOCUMENTPATH() + "/html/" + filename); 
				BufferedWriter out = new BufferedWriter(fstream)){
			out.write(page.toString());
		} catch (IOException e) {
			System.err.println("Failed to create page " + filename + ". Reason: " + e.getMessage());
		}
		htmlUploader.uploadFile(filename, "new-observer/statistics/" + filename);
	}

	/**
	 * Construct the overview page (overview_daily.html or overview_monthly.html)
	 * @param date current date, used as title
	 * @param numberOfEntries amount of datasets for the current month / year
	 * @param filename filename under which the page should be saved (needed for archives)
	 */
	private void buildOverviewPage(String date, int numberOfEntries,
			String filename) {

		StringBuilder page = new StringBuilder();
		page.append(observerProperties.getPAGE_TOP());

		page.append("<h2>" + date + "</h2>\n");

		for (int i = 1; i <= numberOfEntries; i++) {
			// create leading 0 for the first 9 entries
			String nextEntry = String.format("%02d", i);
			String nextDate = date + "-" + nextEntry;

			File nextFile = new File(observerProperties.getDOCUMENTPATH() + "/html/" + nextDate + ".html");
			if (nextFile.exists()) {
				// add next entry only if file exists
				page.append("<li><a href=\"" + nextDate + ".html\">" + nextDate + "</a></li>");
			}
		}
		page.append(observerProperties.getPAGE_BOTTOM());
		page.append("\n</body>\n</html>");

		writePage(page, filename);
	}

	/**
	 * Creates the code for the charts.
	 * @param routerInfoStatistic data source from which information is extracted.
	 * @return String with the HTML code for the two charts.
	 */
	private String buildChartData(RouterInfoStatistic routerInfoStatistic) {
		StringBuilder chart1 = new StringBuilder();
		StringBuilder chart2 = new StringBuilder();

		List<RouterInfoDataset> routerInfoDatasets = routerInfoStatistic.getDatasets();

		chart1.append("<script>Morris.Line({element: 'line-chart-complete',data: [");
		chart2.append("<script>Morris.Line({element: 'line-chart-router',data: [");

		for (int i = 0; i < routerInfoDatasets.size(); i++) {

			if (i == 0) {
				chart1.append("{");
				chart2.append("{");
			} else {
				chart1.append(",{");
				chart2.append(",{");
			}

			String time;

			if (routerInfoStatistic.isMonthlyStatistic()) {
				time = routerInfoDatasets.get(i).getEntryDate();
			} else {
				time = routerInfoStatistic.getDatasetDate() + " " + routerInfoDatasets.get(i).getEntryDate();
			}

			chart1.append("time: \"" + time + "\"");
			chart2.append("time: \"" + time + "\"");
			chart2.append(",netdb_entries:"
					+ routerInfoDatasets.get(i).getNumberOfEntries());
			chart1.append(",known_routers:"
					+ routerInfoDatasets.get(i).getNetDbInfos().get(observerProperties.getKNOWN_ROUTERS_DENOTATION()));
			chart2.append(",known_routers:"
					+ routerInfoDatasets.get(i).getNetDbInfos().get(observerProperties.getKNOWN_ROUTERS_DENOTATION()));
			chart1.append(",floodfil_routers:" + routerInfoDatasets.get(i).getNetDbInfos()
					.get(observerProperties.getFLOODFIL_ROUTERS_DENOTATION()));
			chart2.append(",floodfil_routers:" + routerInfoDatasets.get(i).getNetDbInfos()
					.get(observerProperties.getFLOODFIL_ROUTERS_DENOTATION()));
			chart1.append(",leaseSets:"
					+ routerInfoDatasets.get(i).getNetDbInfos().get(observerProperties.getLEASESETS_DENOTATION()));
			chart1.append("}");
			chart2.append("}");
		}

		chart1.append(
				"],xkey: 'time',ykeys: ['known_routers', 'floodfil_routers', 'leaseSets'],labels: ['Largest number of known routers', 'known floodfil routers', 'Total leaseSets'],lineColors:['#0066cc','gray','#00994c'],pointSize: 1,hideHover: 'auto',resize: true");
		chart2.append(
				"],xkey: 'time',ykeys: ['netdb_entries', 'known_routers', 'floodfil_routers'],labels: ['Amount of Entries in netDB', 'Largest number of known routers', 'known floodfil routers'],lineColors:['#cc6600','#0066cc','gray'],pointSize: 1,hideHover: 'auto',resize: true");

		// set labeling of x axis for monthly charts
		if (routerInfoStatistic.isMonthlyStatistic()) {
			chart1.append(",xLabels: 'day'");
			chart2.append(",xLabels: 'day'");
		}
		chart1.append("});</script>\n");
		chart2.append("});</script>\n");

		return chart1.append(chart2.toString()).toString();
	}
}
