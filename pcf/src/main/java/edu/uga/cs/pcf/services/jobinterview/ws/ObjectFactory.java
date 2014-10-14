
package edu.uga.cs.pcf.services.jobinterview.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the edu.uga.cs.pcf.services.jobinterview.ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Interview_QNAME = new QName("urn:edu.uga.cs.pcf:pcf-services:1.0", "interview");
    private final static QName _InterviewResult_QNAME = new QName("urn:edu.uga.cs.pcf:pcf-services:1.0", "interviewResult");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: edu.uga.cs.pcf.services.jobinterview.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Interview }
     * 
     */
    public Interview createInterview() {
        return new Interview();
    }

    /**
     * Create an instance of {@link InterviewResult }
     * 
     */
    public InterviewResult createInterviewResult() {
        return new InterviewResult();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Interview }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:edu.uga.cs.pcf:pcf-services:1.0", name = "interview")
    public JAXBElement<Interview> createInterview(Interview value) {
        return new JAXBElement<Interview>(_Interview_QNAME, Interview.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InterviewResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:edu.uga.cs.pcf:pcf-services:1.0", name = "interviewResult")
    public JAXBElement<InterviewResult> createInterviewResult(InterviewResult value) {
        return new JAXBElement<InterviewResult>(_InterviewResult_QNAME, InterviewResult.class, null, value);
    }

}
