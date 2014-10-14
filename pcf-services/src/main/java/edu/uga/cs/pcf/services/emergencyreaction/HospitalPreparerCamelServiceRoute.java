package edu.uga.cs.pcf.services.emergencyreaction;

import org.apache.camel.builder.RouteBuilder;

public class HospitalPreparerCamelServiceRoute extends RouteBuilder {

    /**
     * The Camel route is configured via this method. The from endpoint is
     * required to be a SwitchYard service.
     */
    public void configure() {
        from("switchyard://HospitalPreparerCamelService")
        .log("Received message for 'HospitalPreparerService' : ${body}")
        .to("switchyard://HospitalPreparer");
    }

}
