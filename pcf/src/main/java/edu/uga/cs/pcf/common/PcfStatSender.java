package edu.uga.cs.pcf.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class PcfStatSender {

	private static final String baseUrl = "http://localhost:8080/pcf/stat";
	private static final Logger logger = Logger.getLogger(PcfStatSender.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"MM-dd-yyyy HH:mm:ss.SSS");

	public static void triggerGlycomicsWorkflow() {
		String requestedUrl = "http://localhost:8080/processes/glycomics";
		getUrl(requestedUrl);
	}

	public static void sendStat(final String processName,
			final String serviceName, final long duration,
			final Date creationDate) {

		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					processStat(processName, serviceName, duration, creationDate);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
	}

	public static void sendDump() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					processDump();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
	}

	private static void processDump() {
		String requestedUrl = buildDumpUrl();
		getUrl(requestedUrl);		
	}

	private static String buildDumpUrl() {
		StringBuilder sb = new StringBuilder(baseUrl);
		sb.append("?");
		sb.append("action=dump");
		return sb.toString();
	}

	private static void processStat(String processName, String serviceName,
			long duration, Date creationDate) throws Exception {
		String requestedUrl = buildStatUrl(processName, serviceName, duration, creationDate);
		getUrl(requestedUrl);		
	}
	
	private static String getUrl(String requestedUrl) {
		StringBuilder response = new StringBuilder();
		BufferedReader reader = null;
		try {
			URL url = new URL(requestedUrl);
			URLConnection conn = url.openConnection();
			conn.setUseCaches(false);
			// the request will return a response
			conn.setDoInput(true);
			// set request method to GET
			conn.setDoOutput(false);
			// reads response, store line by line in an array of Strings
			reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				response.append(line);
				response.append("\n");
			}

			return response.toString();
		} catch (Exception ex) {
			logger.error(
					String.format("failed to send http request to ", baseUrl),
					ex);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	private static String buildStatUrl(String processName, String serviceName,
			long duration, Date creationDate) throws Exception {
		StringBuilder sb = new StringBuilder(baseUrl);
		sb.append("?");
		sb.append(String.format("processName=%s", URLEncoder.encode(processName, "UTF-8")));
		sb.append("&");
		sb.append(String.format("serviceName=%s", URLEncoder.encode(serviceName, "UTF-8")));
		sb.append("&");
		sb.append(String.format("duration=%d", duration));
		sb.append("&");
		sb.append(String.format("creationDate=%s", URLEncoder.encode(sdf.format(creationDate), "UTF-8")));
		return sb.toString();
	}

	public static void main(String[] args) {
		BasicConfigurator.configure();
		sendStat("sdf", "sdfsdf", 10000, new Date());
	}
}
