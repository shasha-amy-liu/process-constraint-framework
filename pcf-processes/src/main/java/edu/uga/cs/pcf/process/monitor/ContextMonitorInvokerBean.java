package edu.uga.cs.pcf.process.monitor;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.switchyard.component.bean.Service;

import edu.uga.cs.pcf.process.remote.SwitchyardRemote;

/**
 * As of switchyard's current stable version (v1.1), it is difficult to configure
 * camel in switchyard environment and make the camel's existing plugins
 * camel-http and camel-soap to work within switchyard and support sending soap
 * or http requests directly within camel route.
 * 
 * See <a href="https://community.jboss.org/wiki/SwitchYardAS7CamelIntegration">
 * camel integration in switchyard</a>.
 * Use a remote invoker as a temporary workaround and should move to camel route in the future.
 */
@Service(ContextMonitorInvoker.class)
public class ContextMonitorInvokerBean implements ContextMonitorInvoker {

    private static final Logger logger = Logger.getLogger(ContextMonitorInvokerBean.class);
    private static final String OPERATIONS_REMOTE_URL = "http://localhost:8080/switchyard-remote";
    private static final String REMOTE_SERVICE_NAME = "ContextMonitor";
    private static final String TARGET_NAMESPACE = "urn:edu.uga.cs.pcf:pcf-operations:1.0";

    @Override
    public void invoke(String input) {
        logger.info(String.format("send input to remote ContextMonitor\n%s", input));
        String content = String.format(
                "{\n" + 
                "  \"context\": \"%s\"\n" +
                "}", input);

        try {
            SwitchyardRemote remote = new SwitchyardRemote(OPERATIONS_REMOTE_URL);
            remote.invokeAndForget(TARGET_NAMESPACE, REMOTE_SERVICE_NAME, content);
        } catch (IOException e) {
            logger.error("failed to save to context monitor", e);
        }
    }
}
