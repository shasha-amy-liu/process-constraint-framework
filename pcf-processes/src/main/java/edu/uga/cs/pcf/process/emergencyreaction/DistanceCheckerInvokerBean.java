package edu.uga.cs.pcf.process.emergencyreaction;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;
import org.switchyard.component.bean.Service;

import edu.uga.cs.pcf.common.PcfStatSender;
import edu.uga.cs.pcf.process.remote.SwitchyardRemote;

@Service(DistanceCheckerInvoker.class)
public class DistanceCheckerInvokerBean implements DistanceCheckerInvoker {

    private static final Logger logger = Logger
            .getLogger(DistanceCheckerInvokerBean.class);
    private static final String SWITCHYARD_REMOTE_URL = "http://localhost:8080/switchyard-remote";
    private static final String REMOTE_SERVICE_NAME = "DistanceChecker";
    private static final String TARGET_NAMESPACE = "urn:edu.uga.cs.pcf:pcf-operations:1.0";

    @Override
    public String check() {
        logger.info("enter DistanceCheckerInvokerBean");
        logger.info("check constraint operation...");
        logger.info("check ProContO for constraint...");

        String result = "";
        try {
            SwitchyardRemote remote = new SwitchyardRemote(
                    SWITCHYARD_REMOTE_URL);
            String content = "{\"geolocation\": {\"lat\": 33.948005, \"long\": -83.377322}}";
            logger.info(String.format("context info\n%s", content));
            result = remote.invoke(TARGET_NAMESPACE, REMOTE_SERVICE_NAME,
                    content);
            logger.info("distance check result = " + result);
        } catch (IOException e) {
            logger.error("failed to save to distance checker", e);
        }

        Random rand = new Random();
        // Creates a gaussian distribution with a mean of 2 and Standard deviation of 1
        // Refers to http://www.javamex.com/tutorials/random_numbers/gaussian_distribution_2.shtml
        long elapsed = new Double(rand.nextGaussian() + 2).longValue() * 1000;

        String processName = "emergencyReactionWorkflow";
        String serviceName = "distanceChecker";
        Date creationDate = new Date();
        PcfStatSender.sendStat(processName, serviceName, elapsed, creationDate);

        return result;
    }
}
