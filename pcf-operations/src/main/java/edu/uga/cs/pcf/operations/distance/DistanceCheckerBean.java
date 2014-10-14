package edu.uga.cs.pcf.operations.distance;

import java.util.Random;

import org.apache.log4j.Logger;
import org.switchyard.component.bean.Service;

@Service(DistanceChecker.class)
public class DistanceCheckerBean implements DistanceChecker {

    private static final Logger logger = Logger.getLogger(DistanceCheckerBean.class);

    @Override
    public String distance(String input) {
        logger.info("check distance!!!!");
        // biased coin, 30% to be false
        Random rand = new Random();
        double ratio = 0.3;
        boolean result = false;
        if (rand.nextDouble() < ratio) {
        	result = false;
        } else {
        	result = true;
        }

        logger.info(String.format("check result is %s!", String.valueOf(result)));
        return String.valueOf(result);
    }

}
