package edu.uga.cs.pcf.process.emergencyreaction;

import org.apache.camel.builder.RouteBuilder;

public class HospitalPreparerCamelServiceRoute extends RouteBuilder {

    /**
     * The Camel route is configured via this method.  The from endpoint is required to be a SwitchYard service.
     */
    public void configure() {
        from("switchyard://HospitalPreparer")
        .log("Received message for 'HospitalPreparer' : ${body}")
        .setProperty("origInBody", body())
        // Send context information to context monitor using wiretap pattern
        .log("wiretap to context monitor")
        .wireTap("switchyard://ContextMonitorInvoker")
        // Apply content based router pattern and invoke constraint operation
        .log("Invoke constraint operation...")
        .to("switchyard://DistanceCheckerInvoker")
        .choice()
        .when(body().contains("true"))
            .log("go to actual hospital preparer.......")
            .setBody(property("origInBody"))
            .to("switchyard://HospitalPreparerService")
        .otherwise()
            .log("constraint operation failed!!!!")
            .to("switchyard://FailedConstraintOperationLoggerInvoker");
    }

}
