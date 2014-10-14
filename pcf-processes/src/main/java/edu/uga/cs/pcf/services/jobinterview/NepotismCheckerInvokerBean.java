package edu.uga.cs.pcf.services.jobinterview;

import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;
import org.switchyard.component.bean.Service;

import edu.uga.cs.pcf.common.PcfStatSender;
import edu.uga.cs.pcf.process.remote.SwitchyardRemote;

@Service(NepotismCheckerInvoker.class)
public class NepotismCheckerInvokerBean implements NepotismCheckerInvoker {

    private static final Logger logger = Logger.getLogger(NepotismCheckerInvokerBean.class);
    private static final String SWITCHYARD_REMOTE_URL = "http://localhost:8080/switchyard-remote";
    private static final String REMOTE_SERVICE_NAME = "NepotismChecker";
    private static final String TARGET_NAMESPACE = "urn:edu.uga.cs.pcf:pcf-operations:1.0";

    @Override
    public String check() {
        logger.info("enter NepotismCheckerInvokerBean");
        logger.info("check constraint operation...");
        logger.info("check ProContO for constraint...");

    	String result = process();
        return result;
    }
    
    private String process() {
    	String result = null;
        try {
            SwitchyardRemote remote = new SwitchyardRemote(SWITCHYARD_REMOTE_URL);
            String content = "{\"interview\": {\"interviewer\": \"Tom\", \"interviewee\": \"Jerry\"}}";
            // logger.info(String.format("context info\n%s", content));
            result = remote.invoke(TARGET_NAMESPACE, REMOTE_SERVICE_NAME, content);
            logger.info(String.format("nepotism check result = %s", result));
        } catch (Exception ex) {
            logger.error("failed to save to nepotism checker", ex);
        }

        Random rand = new Random();
    	// Creates a gaussian distribution with a mean of 2 and Standard deviation of 1
    	// Refers to http://www.javamex.com/tutorials/random_numbers/gaussian_distribution_2.shtml
        long elapsed = new Double(rand.nextGaussian() + 2).longValue() * 1000;

        String processName = "jobInterviewProcess";
		String serviceName = "nepotismChecker";
		Date creationDate = new Date();
		PcfStatSender.sendStat(processName, serviceName, elapsed, creationDate);

        return result;
	}

	public static void main(String[] args) {
    	Object result = new NepotismCheckerInvokerBean().check();
        System.out.println(String.format("result = %s", result));
    }
}
