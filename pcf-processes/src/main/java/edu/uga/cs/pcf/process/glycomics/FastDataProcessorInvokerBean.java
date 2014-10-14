package edu.uga.cs.pcf.process.glycomics;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;
import org.switchyard.component.bean.Service;

import edu.uga.cs.pcf.common.PcfStatSender;
import edu.uga.cs.pcf.process.remote.SwitchyardRemote;

@Service(FastDataProcessorInvoker.class)
public class FastDataProcessorInvokerBean implements FastDataProcessorInvoker {

    private static final Logger logger = Logger.getLogger(FastDataProcessorInvokerBean.class);
    private static final String SWITCHYARD_REMOTE_URL = "http://localhost:8080/switchyard-remote";
    private static final String REMOTE_SERVICE_NAME = "FastDataProcessor";
    private static final String TARGET_NAMESPACE = "urn:edu.uga.cs.pcf:pcf-services:1.0";

    @Override
    public String invoke(String input) {
        logger.info("enter FastDataProcessorInvokerBean");
        logger.info("invoke service...");

        String result = "";
        try {
            SwitchyardRemote remote = new SwitchyardRemote(SWITCHYARD_REMOTE_URL);
            String content = "input to fast data processor";
            result = remote.invoke(TARGET_NAMESPACE, REMOTE_SERVICE_NAME, content);
        } catch (IOException e) {
            logger.error("failed to invoke fast data processor.", e);
        }

        Random rand = new Random();
    	// Creates a gaussian distribution with a mean of 4 and Standard deviation of 2
    	// Refers to http://www.javamex.com/tutorials/random_numbers/gaussian_distribution_2.shtml
        long elapsed = new Double(rand.nextGaussian() * 2 + 4).longValue() * 1000;
        String processName = "glycomicsWorkflow";
		String serviceName = "fastDataProcessor";
		Date creationDate = new Date();
		PcfStatSender.sendStat(processName, serviceName, elapsed, creationDate);

        return result;
    }

}
