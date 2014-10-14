package edu.uga.cs.pcf.stat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import edu.uga.cs.pcf.common.PcfStatSender;
import edu.uga.cs.pcf.services.emergencyreaction.ws.EmergencyReactionWorkflow;
import edu.uga.cs.pcf.services.emergencyreaction.ws.EmergencyReactionWorkflowRequest;
import edu.uga.cs.pcf.services.emergencyreaction.ws.EmergencyReactionWorkflowService;

public class EmergencyReactionWorkflowStatCollector {

	private static final Logger logger = Logger.getLogger(EmergencyReactionWorkflowStatCollector.class);

	public static void main(String[] args) {
		runAll();
		// invoke();
	}
	private static void runAll() {
		final int iterations = 5000;
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
        System.out.println("Create Web Service Client...");
        EmergencyReactionWorkflowService service1 = new EmergencyReactionWorkflowService();
        System.out.println("Create Web Service...");
        EmergencyReactionWorkflow port1 = service1.getEmergencyReactionWorkflowPort();
        System.out.println("Call Web Service Operation...");
        EmergencyReactionWorkflowRequest request = new EmergencyReactionWorkflowRequest();
        request.setInput("sdfsdfsdf");
        System.out.println("Server said: " + port1.process(request));
        //Please input the parameters instead of 'null' for the upper method!

        System.out.println("***********************");
        System.out.println("Call Over!");
	}
}
