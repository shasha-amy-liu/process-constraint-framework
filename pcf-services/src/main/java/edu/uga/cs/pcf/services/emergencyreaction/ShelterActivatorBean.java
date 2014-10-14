package edu.uga.cs.pcf.services.emergencyreaction;

import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;
import org.switchyard.component.bean.Service;

import edu.uga.cs.pcf.common.PcfStatSender;

@Service(ShelterActivator.class)
public class ShelterActivatorBean implements ShelterActivator {

    private static final Logger logger = Logger.getLogger(ShelterActivatorBean.class);

    @Override
    public String activate(String input) {
        String result = String.format("activate shelter with with input %s", input);
        logger.info(result);
        logger.info("Activate shelter......");

        Random rand = new Random();
		// Creates a gaussian distribution with a mean of 7 and Standard deviation of 3
		// Refers to http://www.javamex.com/tutorials/random_numbers/gaussian_distribution_2.shtml
		long elapsed = new Double(rand.nextGaussian() * 3 + 7).longValue() * 1000;
		String processName = "emergencyReactionWorkflow";
		String serviceName = "shelterActivator";
		Date creationDate = new Date();
		PcfStatSender.sendStat(processName, serviceName, elapsed, creationDate);

		return result;
    }

}
