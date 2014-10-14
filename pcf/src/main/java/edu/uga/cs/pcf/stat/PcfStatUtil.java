package edu.uga.cs.pcf.stat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Collection;

import org.apache.log4j.Logger;

public class PcfStatUtil {

	public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");
	private static final String path = System.getProperty("java.io.tmpdir") + File.separator + "stat.csv";
	private static final File f = new File(path);
	private static final Logger logger = Logger.getLogger(PcfStatUtil.class);

	/**
	 * Saves it to a csv file. Uses a random access file and append to its end every time.
	 * Acquires a file lock before access it to ensure potential concurrent modification. 
	 * @param stat
	 */
	public static void saveStat(PcfStat stat) {
		RandomAccessFile file = null;
		 try {
			file = new RandomAccessFile(f, "rw");
			FileChannel channle = file.getChannel();
			channle.lock();
			long fileLength = file.length();
			file.seek(fileLength);
			file.writeChars(toCsvRecord(stat));
			file.writeChars("\n");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void saveStats(Collection<PcfStat> stats) throws Exception {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(f));
			String newline = System.getProperty("line.separator");
			for (PcfStat s : stats) {
				String line = toCsvRecord(s);
				writer.write(line);
				writer.write(newline);
			}
			writer.flush();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	private static String toCsvRecord(PcfStat stat) {
		StringBuilder sb = new StringBuilder();
		sb.append(stat.getId());
		sb.append(",");
		sb.append(stat.getProcessName());
		sb.append(",");
		sb.append(stat.getServiceName());
		sb.append(",");
		sb.append(stat.getDuration());
		sb.append(",");
		sb.append(DATE_FORMATTER.format(stat.getCreationDate()));
		return sb.toString();
	}
}
