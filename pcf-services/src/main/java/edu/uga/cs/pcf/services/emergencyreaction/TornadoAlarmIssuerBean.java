package edu.uga.cs.pcf.services.emergencyreaction;

import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;
import org.switchyard.component.bean.Service;

import edu.uga.cs.pcf.common.PcfStatSender;

@Service(TornadoAlarmIssuer.class)
public class TornadoAlarmIssuerBean implements TornadoAlarmIssuer {
    private static final Logger logger = Logger.getLogger(TornadoAlarmIssuerBean.class);

    @Override
    public String issue(String input) {
        String result = String.format("issue tornado alarm with input %s", input);
        logger.info(result);
        logger.info("Issue tornado alarm.....");
        
        Random rand = new Random();
		// Creates a gaussian distribution with a mean of 8 and Standard deviation of 4
		// Refers to http://www.javamex.com/tutorials/random_numbers/gaussian_distribution_2.shtml
		long elapsed = new Double(rand.nextGaussian() * 4 + 8).longValue() * 1000;
		String processName = "emergencyReactionWorkflow";
		String serviceName = "tornadoAlarmIssuer";
		Date creationDate = new Date();
		PcfStatSender.sendStat(processName, serviceName, elapsed, creationDate);

        return result;
    }

}
