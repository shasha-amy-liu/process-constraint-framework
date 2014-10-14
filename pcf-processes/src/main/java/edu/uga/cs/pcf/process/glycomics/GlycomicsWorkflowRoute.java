package edu.uga.cs.pcf.process.glycomics;

import org.apache.camel.builder.RouteBuilder;

public class GlycomicsWorkflowRoute extends RouteBuilder {

    /**
     * The Camel route is configured via this method. The from endpoint is
     * required to be a SwitchYard service.
     */
    public void configure() {
        from("switchyard://GlycomicsWorkflow")
        .log("Received message for 'GlycomicsWorkflow' : ${body}")
        // Invoke raw data producer service
        .to("switchyard://RawDataProducer")
        .log("created raw file with size ${body}G")
        // Invoke data processor service
        .to("switchyard://DataProcessorService")
        // Invoke SimulationOptimizationExecutor service
        .to("switchyard://SimulationOptimizationExecutor")
        // Invoke ResultVisualizer service
        .to("switchyard://ResultVisualizer")
        .log("finish glycomics workflow and quit.....")
        ;
    }
}
