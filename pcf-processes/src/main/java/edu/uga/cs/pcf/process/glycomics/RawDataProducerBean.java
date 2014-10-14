package edu.uga.cs.pcf.process.glycomics;

import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;
import org.switchyard.component.bean.Service;

import edu.uga.cs.pcf.common.PcfStatSender;

@Service(RawDataProducer.class)
public class RawDataProducerBean implements RawDataProducer {
    private static final Logger logger = Logger.getLogger(RawDataProducerBean.class);

    @Override
    public int produce() {
        logger.info("producuing raw experiment data, please wait...");
        Random rand = new Random();
        int result = rand.nextInt(10) + 1;
        logger.info(String.format("raw experiment data is generated and file size is %dG...", result));

    	// Creates a gaussian distribution with a mean of 10 and Standard deviation of 5
    	// Refers to http://www.javamex.com/tutorials/random_numbers/gaussian_distribution_2.shtml
        long elapsed = new Double(rand.nextGaussian() * 5 + 10).longValue() * 1000;
        String processName = "glycomicsWorkflow";
		String serviceName = "rawDataProducer";
		Date creationDate = new Date();
		PcfStatSender.sendStat(processName, serviceName, elapsed, creationDate);

		return result;
    }
}
