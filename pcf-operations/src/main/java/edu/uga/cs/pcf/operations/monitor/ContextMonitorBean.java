package edu.uga.cs.pcf.operations.monitor;

import org.apache.log4j.Logger;
import org.switchyard.component.bean.Service;

@Service(ContextMonitor.class)
public class ContextMonitorBean implements ContextMonitor {
    private static final Logger logger = Logger.getLogger(ContextMonitorBean.class);

    @Override
    public void set(String input) {
        logger.info("query ProContO");
        logger.info(String.format("set context for %s", input));
    }
}
