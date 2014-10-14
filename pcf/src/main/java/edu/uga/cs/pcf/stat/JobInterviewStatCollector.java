package edu.uga.cs.pcf.stat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import edu.uga.cs.pcf.common.PcfStatSender;
import edu.uga.cs.pcf.services.jobinterview.ws.Interview;
import edu.uga.cs.pcf.services.jobinterview.ws.JobInterviewProcess;
import edu.uga.cs.pcf.services.jobinterview.ws.JobInterviewProcessPortType;

public class JobInterviewStatCollector {

	private static final Logger logger = Logger.getLogger(JobInterviewStatCollector.class);

	public static void main(String[] args) {
		final int iterations = 10000;
		int poolSize = 1;
		ExecutorService executor = Executors.newFixedThreadPool(poolSize);

		for (int i = 0; i < iterations; i++) {
			final int processed = i;
			executor.submit(new Runnable() {
				
				@Override
				public void run() {
					invoke();
					logger.info(String.format("%d has been processed and %d to go.", processed, iterations - processed));
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		}
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Send the dump command
		PcfStatSender.sendDump();
	}
	private static void invoke() {
		System.out.println("***********************");
		Interview interview = new Interview();
		interview.setInterviewee("sdfsdfdf");
		System.out.println("Create Web Service Client...");
		JobInterviewProcess service1 = new JobInterviewProcess();
		System.out.println("Create Web Service...");
		JobInterviewProcessPortType port1 = service1
				.getJobInterviewProcessPort();
		System.out.println("Call Web Service Operation...");
		try {
			System.out.println("Server said: " + port1.process(interview));
		} catch (Exception ex) {
			System.out.println("exception caught!!!");
			ex.printStackTrace();
		}

		System.out.println("***********************");
		System.out.println("Call Over!");
	}
}
