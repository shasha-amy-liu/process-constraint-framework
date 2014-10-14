package edu.uga.cs.pcf.operations.logger;

import org.apache.log4j.Logger;
import org.switchyard.component.bean.Service;

@Service(FailedConstraintOperationLogger.class)
public class FailedConstraintOperationLoggerBean implements
        FailedConstraintOperationLogger {

    private static final Logger logger = Logger.getLogger(FailedConstraintOperationLoggerBean.class);

    @Override
    public String log(String input) {
        String result = String.format("FailedConstraintOperationLogger: log failure for input\n%s", input);
        logger.info(result);

        return result;
    }

}
