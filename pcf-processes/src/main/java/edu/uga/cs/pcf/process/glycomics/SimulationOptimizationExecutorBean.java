package edu.uga.cs.pcf.process.glycomics;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.switchyard.component.bean.Service;

import edu.uga.cs.pcf.common.PcfStatSender;

@Service(SimulationOptimizationExecutor.class)
public class SimulationOptimizationExecutorBean implements
        SimulationOptimizationExecutor {

    private static final Logger logger = Logger.getLogger(SimulationOptimizationExecutorBean.class);

    @Override
    public String execute(String input) {
        logger.info("conduct simulation and optimization, please wait...");
        Random rand = new Random();
        String result = UUID.randomUUID().toString();
        logger.info(String.format("executed simulation and optimization with result %s...", result));
        
        // Creates a gaussian distribution with a mean of 8 and Standard deviation of 4
    	// Refers to http://www.javamex.com/tutorials/random_numbers/gaussian_distribution_2.shtml
        long elapsed = new Double(rand.nextGaussian() * 4 + 8).longValue() * 1000;
        String processName = "glycomicsWorkflow";
		String serviceName = "simulationOptimizationExecutor";
		Date creationDate = new Date();
		PcfStatSender.sendStat(processName, serviceName, elapsed, creationDate);

        return result;
    }

}
