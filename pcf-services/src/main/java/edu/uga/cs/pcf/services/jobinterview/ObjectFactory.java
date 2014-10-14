package edu.uga.cs.pcf.services.jobinterview;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

    private static final QName _Interview_QNAME = new QName(
            "urn:edu.uga.cs.pcf:pcf-services:1.0", "interview");
    private static final QName _Interview_Result_QNAME = new QName(
            "urn:edu.uga.cs.pcf:pcf-services:1.0", "interviewResult");

    public ObjectFactory() {}

    public Interview createInterview() {
        Interview result = new Interview();
        return result;
    }

    public InterviewResult createInterviewResult() {
        InterviewResult result = new InterviewResult();
        return result;
    }

    @XmlElementDecl(namespace = "urn:edu.uga.cs.pcf:pcf-services:1.0", name = "interview")
    public JAXBElement<Interview> createInterview(Interview value) {
        return new JAXBElement<Interview>(_Interview_QNAME, Interview.class,
                null, value);
    }

    @XmlElementDecl(namespace = "urn:edu.uga.cs.pcf:pcf-services:1.0", name = "interviewResult")
    public JAXBElement<InterviewResult> createInterviewResult(
            InterviewResult value) {
        return new JAXBElement<InterviewResult>(_Interview_Result_QNAME,
                InterviewResult.class, null, value);
    }
}
