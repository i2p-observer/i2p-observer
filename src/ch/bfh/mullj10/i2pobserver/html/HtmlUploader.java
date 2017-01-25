package ch.bfh.mullj10.i2pobserver.html;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import ch.bfh.mullj10.i2pobserver.data.ObserverProperties;

public class HtmlUploader {

	private final int BUFFER_SIZE = 4096;
	private final String USERNAME;
	private final String PASSWORD;
	private final String HOSTNAME;
	private final String DOCUMENTPATH;
	
	public HtmlUploader(ObserverProperties observerProperties){
		this.USERNAME = observerProperties.getFTP_USERNAME();
		this.PASSWORD = observerProperties.getFTP_PASSWORD();
		this.HOSTNAME = observerProperties.getFTP_HOSTNAME();
		this.DOCUMENTPATH = observerProperties.getDOCUMENTPATH();
	}
	
	public void uploadFile(String filename, String serverPath) {
		
		String ftpUrl = "ftp://%s:%s@%s/%s;type=i";
		String file = DOCUMENTPATH + "/html/" + filename;
		ftpUrl = String.format(ftpUrl, USERNAME, PASSWORD, HOSTNAME, serverPath);
		
		try {
			URL url = new URL(ftpUrl);
			URLConnection conn = url.openConnection();
			OutputStream outputStream = conn.getOutputStream();
			FileInputStream inputStream = new FileInputStream(file);

			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}

			inputStream.close();
			outputStream.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
