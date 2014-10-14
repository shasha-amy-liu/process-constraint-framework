package edu.uga.cs.pcf.process.glycomics;

public interface GlycomicsWorkflow {

    /**
     * dummy method to fix the ERROR for Camel Java implementation.
     * [org.switchyard.component.http] (http-localhost/127.0.0.1:8080-1) SWITCHYARD036004: Unexpected Exception invoking SwitchYard service: org.switchyard.SwitchYardException: SWITCHYARD036404: No operationSelector was configured for the Http Component and the Service Interface contains more than one operation: []. Please add an operationSelector element.
     * For camle xml route, no need to specify this.
     * @return
     */
    String test(String input);
}
