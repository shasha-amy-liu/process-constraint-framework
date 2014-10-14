package edu.uga.cs.pcf.process.glycomics;

import org.apache.camel.builder.RouteBuilder;

public class DataProcessorCamelServiceRoute extends RouteBuilder {

    /**
     * The Camel route is configured via this method. 
     * The from endpoint is required to be a SwitchYard service.
     */
    public void configure() {
        from("switchyard://DataProcessorService")
        .log("Received message for 'DataProcessorService' : ${body}")
        // Send context information to context monitor using wiretap pattern
        .log("wiretap to context monitor")
        .wireTap("switchyard://ContextMonitorInvoker")
        // Apply content based router pattern and invoke constraint operation
        .log("Invoke constraint operation...")
        .to("switchyard://FileSizeCheckerInvoker")
        .choice()
        .when(body().contains("true"))
            .log("go to slow data processor.....")
            .to("switchyard://SlowDataProcessorInvoker")
        .otherwise()
            .log("go to fast data processor.....")
            .to("switchyard://FastDataProcessorInvoker");
    }

}
