package edu.uga.cs.pcf.services.jobinterview;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="interviewResult", namespace="urn:edu.uga.cs.pcf:pcf-services:1.0")
public class InterviewResult {

    private String interviewer;
    private String interviewee;
    private String result;

    public InterviewResult() {}

    public String getInterviewer() {
        return interviewer;
    }

    public void setInterviewer(String interviewer) {
        this.interviewer = interviewer;
    }

    public String getInterviewee() {
        return interviewee;
    }

    public void setInterviewee(String interviewee) {
        this.interviewee = interviewee;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "InterviewResult [interviewer=" + interviewer + ", interviewee="
                + interviewee + ", result=" + result + "]";
    }
}
