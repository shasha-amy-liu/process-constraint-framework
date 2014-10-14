package edu.uga.cs.pcf.process.emergencyreaction;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;
import org.switchyard.component.bean.Service;

import edu.uga.cs.pcf.common.PcfStatSender;
import edu.uga.cs.pcf.process.remote.SwitchyardRemote;

@Service(FailedConstraintOperationLoggerInvoker.class)
public class FailedConstraintOperationLoggerInvokerBean implements
        FailedConstraintOperationLoggerInvoker {

    private static final Logger logger = Logger.getLogger(FailedConstraintOperationLoggerInvokerBean.class);
    private static final String SWITCHYARD_REMOTE_URL = "http://localhost:8080/switchyard-remote";
    private static final String REMOTE_SERVICE_NAME = "FailedConstraintOperationLogger";
    private static final String TARGET_NAMESPACE = "urn:edu.uga.cs.pcf:pcf-operations:1.0";

    @Override
    public void log(String input) {
        logger.info("enter FailedConstraintOperationLoggerInvokerBean");
        logger.info("log constraint check failure...");

        try {
            SwitchyardRemote remote = new SwitchyardRemote(SWITCHYARD_REMOTE_URL);
            String content = "Constraint operation failed....";
            logger.info(content);
            remote.invoke(TARGET_NAMESPACE, REMOTE_SERVICE_NAME, content);
        } catch (IOException e) {
            logger.error("failed to log constraint operation failure...", e);
        }

        Random rand = new Random();
    	// Creates a gaussian distribution with a mean of 1 and Standard deviation of 1
    	// Refers to http://www.javamex.com/tutorials/random_numbers/gaussian_distribution_2.shtml
        long elapsed = new Double(rand.nextGaussian() + 1).longValue() * 1000;

        String processName = "emergencyReactionWorkflow";
		String serviceName = "failedConstraintOperationLogger";
		Date creationDate = new Date();
		PcfStatSender.sendStat(processName, serviceName, elapsed, creationDate);
    }

}
