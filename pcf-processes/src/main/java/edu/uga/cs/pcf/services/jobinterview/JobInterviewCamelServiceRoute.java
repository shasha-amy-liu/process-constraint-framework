package edu.uga.cs.pcf.services.jobinterview;

import org.apache.camel.builder.RouteBuilder;

public class JobInterviewCamelServiceRoute extends RouteBuilder {

    /**
     * The Camel route is configured via this method. The from endpoint is
     * required to be a SwitchYard service.
     */
    public void configure() {
        // flag to foward to the original service without constraint operation
        boolean forwardOnly = false;
        if (forwardOnly) {
            from("switchyard://JobInterview")
            .to("switchyard://JobInterviewService");
        } else {
            from("switchyard://JobInterview")
            .log("Received message for 'JobInterview' : ${body}")
            .setProperty("origInBody", body())
            // Send context information to context monitor using wiretap
            // pattern
            .log("wiretap to context monitor")
            .wireTap("switchyard://ContextMonitorInvoker")
            // Apply content based router pattern and invoke constraint
            // operation
            .log("Invoke constraint operation...")
            .to("switchyard://NepotismCheckerInvoker").choice()
            .when(body().contains("false"))
            .log("go to actual job interview.......")
            .setBody(property("origInBody"))
            .to("switchyard://JobInterviewService").otherwise()
            .log("constraint operation failed!!!!")
            .to("switchyard://FailedConstraintOperationLoggerInvoker");
        }
    }
}
