package ch.bfh.mullj10.i2pobserver.netdb;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.bfh.mullj10.i2pobserver.data.NetDbEntries;
import net.i2p.data.DataFormatException;
import net.i2p.data.router.RouterInfo;

/**
 * Reads files from the I2P netDB folder and converts them into I2Ps own class
 * RouterInfo.
 * 
 * @author mullj10
 * @return NetDbEntries with all imported routerInfos
 */
public class NetDBImporter {

	private String netDBFolder;
	List<File> inputFiles = new ArrayList<File>();

	public NetDBImporter(String netDBFolder) {
		this.netDBFolder = netDBFolder;
	}

	public NetDbEntries readNetDB() {

		Set<RouterInfo> netDbEntries = new HashSet<>();
		int numberOfErrors = 0;
		boolean processingError = false;
		File netDBRoot = new File(netDBFolder);
		if (!netDBRoot.exists()) {
			System.err.println("NetDB folder not found!");
			System.exit(1);
		}

		// collect all files
		listFiles(netDBFolder);

		for (File file : inputFiles) {
			
			// try reading files.
			try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(file))) {
				RouterInfo router = new RouterInfo();
				router.readBytes(input);
				netDbEntries.add(router);
			} catch (DataFormatException | IOException ex) {
				if (ex.getClass().equals(FileNotFoundException.class)) {
					// skip this file / do nothing. It is possible that I2P
					// alters the netDB files after the file list was created.
				} else {
					System.err.println("Failed to load one netDB entry.");
					ex.printStackTrace();
				}
				numberOfErrors++;
			}
		}

		if (numberOfErrors >= 20){
			processingError = true;
		}
		
		return new NetDbEntries(netDbEntries, processingError);
	}

	/**
	 * recursively read all files inside directory
	 * 
	 * @param directory
	 *            folder to search
	 */
	private void listFiles(String directory) {

		File folder = new File(directory);

		// filter out directories
		File[] files = folder.listFiles();
		for (File f : files) {

			if (f.isFile()) {
				inputFiles.add(f);
			} else if (f.isDirectory()) {
				listFiles(f.getAbsolutePath());
			}

			if (!f.isDirectory())
				inputFiles.add(f);
		}
	}
}
