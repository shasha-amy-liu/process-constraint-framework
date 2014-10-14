package edu.uga.cs.pcf.stat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import edu.uga.cs.pcf.common.PcfStatSender;
import edu.uga.cs.pcf.services.emergencyreaction.ws.EmergencyReactionWorkflow;
import edu.uga.cs.pcf.services.emergencyreaction.ws.EmergencyReactionWorkflowRequest;
import edu.uga.cs.pcf.services.emergencyreaction.ws.EmergencyReactionWorkflowService;

public class GlycomicsWorkflowStatCollector {

	private static final Logger logger = Logger.getLogger(GlycomicsWorkflowStatCollector.class);

	public static void main(String[] args) {
		runAll();
		// invoke();
	}
	private static void runAll() {
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
		PcfStatSender.triggerGlycomicsWorkflow();
	}
}
