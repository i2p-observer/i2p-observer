package ch.bfh.mullj10.i2pobserver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ch.bfh.mullj10.i2pobserver.data.ObserverProperties;
import ch.bfh.mullj10.i2pobserver.data.RouterInfoStatistic;
import ch.bfh.mullj10.i2pobserver.html.HtmlOutputCreator;
import ch.bfh.mullj10.i2pobserver.netdb.NetDBImporter;

/**
 * Main class of I2P-Observer.
 * 
 * @author mullj10
 *
 */
public class Observer {

	private static ObserverProperties observerProperties = new ObserverProperties();
	private static RouterInfoStatistic dailyStatistic;
	private static RouterInfoStatistic monthlyStatistic;

	public static void main(String[] args) {

		// load the previous state of the RouterInfoStatistic object (old
		// datasets)
		try {
			loadRouterInfoStatistics();
		} catch (Exception e) {
			dailyStatistic = new RouterInfoStatistic(observerProperties.getDailyDate(), false);
			if (!e.getClass().equals(FileNotFoundException.class)) {
				System.err.println(
						"Failed to load RouterInfoStatistic file! Starting without data. Reason: " + e.toString());
			}
		}

		// import the current netDB files
		NetDBImporter importer = new NetDBImporter(observerProperties.getNetDBFolderPath());

		// analyze the imported netDB entries.
		RouterInfoAnalyzer routerInfoAnalyzer = new RouterInfoAnalyzer(observerProperties, importer.readNetDB(),
				dailyStatistic);
		routerInfoAnalyzer.analyzeRouterInfo();

		// create the HTML page
		HtmlOutputCreator htmlOutputCreator = new HtmlOutputCreator(observerProperties);
		htmlOutputCreator.buildStatisticPage(dailyStatistic);

		// create daily average for monthly statistic
		monthlyStatistic.replaceDataset(dailyStatistic.buildAverage(observerProperties.getDailyDate()));
		htmlOutputCreator.buildStatisticPage(monthlyStatistic);

		// save the current state / datasets of the RouterInfoStatistic
		try {
			saveRouterInfoStatistic(dailyStatistic);
			saveRouterInfoStatistic(monthlyStatistic);
		} catch (IOException e) {
			System.err.println("Failed to save RouterInfoStatistic file! Next run will start without data. Reason: "
					+ e.toString());
		}
	}

	/**
	 * Save the object to the WORKING_DIR on disk.
	 * 
	 * @throws IOException
	 */
	private static void saveRouterInfoStatistic(RouterInfoStatistic routerInfoStatistic) throws IOException {

		String path;
		String date;
		if (routerInfoStatistic.isMonthlyStatistic()) {
			path = "monthly";
			date = observerProperties.getMonthlyDate();
		} else {
			path = "daily";
			date = observerProperties.getDailyDate();
		}
		FileOutputStream f_out = new FileOutputStream(
				observerProperties.getWORKING_DIR() + "data/" + path + "/routerInfoStatistic-" + date + ".data");
		ObjectOutputStream obj_out = new ObjectOutputStream(f_out);
		obj_out.writeObject(routerInfoStatistic);
		obj_out.close();
	}

	/**
	 * Load the object from the WORKING_DIR on disk.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private static void loadRouterInfoStatistics() throws ClassNotFoundException, IOException {

		// import daily statistic
		FileInputStream f_in = null;
		try {
			f_in = new FileInputStream(observerProperties.getWORKING_DIR() + "data/daily/routerInfoStatistic-"
					+ observerProperties.getDailyDate() + ".data");
		} catch (FileNotFoundException e) {
			dailyStatistic = new RouterInfoStatistic(observerProperties.getDailyDate(), false);
		}

		ObjectInputStream obj_in = null;
		Object obj = null;
		if (f_in != null) {
			obj_in = new ObjectInputStream(f_in);
			obj = obj_in.readObject();

			if (obj instanceof RouterInfoStatistic) {
				dailyStatistic = (RouterInfoStatistic) obj;
			}
		}

		// import monthly statistic
		f_in = null;
		try {
			f_in = new FileInputStream(observerProperties.getWORKING_DIR() + "data/monthly/routerInfoStatistic-"
					+ observerProperties.getMonthlyDate() + ".data");
		} catch (FileNotFoundException e) {
			monthlyStatistic = new RouterInfoStatistic(observerProperties.getMonthlyDate(), true);
		}
		if (f_in != null) {
			obj_in = new ObjectInputStream(f_in);
			obj = obj_in.readObject();

			if (obj instanceof RouterInfoStatistic) {
				monthlyStatistic = (RouterInfoStatistic) obj;
			}
		}
		obj_in.close();
	}
}
