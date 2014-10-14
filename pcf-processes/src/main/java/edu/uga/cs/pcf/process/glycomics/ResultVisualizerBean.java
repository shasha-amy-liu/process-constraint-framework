package edu.uga.cs.pcf.process.glycomics;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.switchyard.component.bean.Service;

import edu.uga.cs.pcf.common.PcfStatSender;

@Service(ResultVisualizer.class)
public class ResultVisualizerBean implements ResultVisualizer {

    private static final Logger logger = Logger.getLogger(ResultVisualizerBean.class);

    @Override
    public String visualize(String input) {
        logger.info("conduct result visulization, please wait...");
        Random rand = new Random();
        String result = UUID.randomUUID().toString();
        logger.info(String.format("executed result visulization with result %s...", result));

        // Creates a gaussian distribution with a mean of 3 and Standard deviation of 3
    	// Refers to http://www.javamex.com/tutorials/random_numbers/gaussian_distribution_2.shtml
        long elapsed = new Double(rand.nextGaussian() * 3 + 3).longValue() * 1000;
        String processName = "glycomicsWorkflow";
		String serviceName = "resultVisualizer";
		Date creationDate = new Date();
		PcfStatSender.sendStat(processName, serviceName, elapsed, creationDate);

		return result;
    }

}
