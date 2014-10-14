package edu.uga.cs.pcf.services.emergencyreaction;

import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;
import org.switchyard.component.bean.Service;

import edu.uga.cs.pcf.common.PcfStatSender;

@Service(TornadoDetector.class)
public class TornadoDetectorBean implements TornadoDetector {
    private static final Logger logger = Logger.getLogger(TornadoDetectorBean.class);

    @Override
    public String detect(String input) {
        String result = String.format("tornado detected with input %s", input);
        logger.info(result);
        logger.info("Trigger tornado emergency reaction workflow.....");

        Random rand = new Random();
		// Creates a gaussian distribution with a mean of 3 and Standard deviation of 2
		// Refers to http://www.javamex.com/tutorials/random_numbers/gaussian_distribution_2.shtml
		long elapsed = new Double(rand.nextGaussian() * 2 + 3).longValue() * 1000;
		String processName = "emergencyReactionWorkflow";
		String serviceName = "tornadoDetector";
		Date creationDate = new Date();
		PcfStatSender.sendStat(processName, serviceName, elapsed, creationDate);

        return result;
    }

}
