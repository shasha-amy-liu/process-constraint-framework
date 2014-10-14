package edu.uga.cs.pcf.services.emergencyreaction;

import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;
import org.switchyard.component.bean.Service;

import edu.uga.cs.pcf.common.PcfStatSender;

@Service(HospitalPreparer.class)
public class HospitalPreparerBean implements HospitalPreparer {

    private static final Logger logger = Logger.getLogger(HospitalPreparerBean.class);

    @Override
    public String prepare(String input) {
        String result = String.format("prepare hospital with input %s", input);
        logger.info(result);
        logger.info("Prepare hospital......");

        Random rand = new Random();
		// Creates a gaussian distribution with a mean of 5 and Standard deviation of 2
		// Refers to http://www.javamex.com/tutorials/random_numbers/gaussian_distribution_2.shtml
		long elapsed = new Double(rand.nextGaussian() * 2 + 5).longValue() * 1000;
		String processName = "emergencyReactionWorkflow";
		String serviceName = "hospitalPreparer";
		Date creationDate = new Date();
		PcfStatSender.sendStat(processName, serviceName, elapsed, creationDate);

        return result;
    }

}
