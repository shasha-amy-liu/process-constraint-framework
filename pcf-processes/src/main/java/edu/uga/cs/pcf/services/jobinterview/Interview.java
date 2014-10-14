package edu.uga.cs.pcf.services.jobinterview;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="interview", namespace="urn:edu.uga.cs.pcf:pcf-services:1.0")
public class Interview {

    private String interviewer;
    private String interviewee;

    public Interview() {}

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

    @Override
    public String toString() {
        return "Interview [interviewer=" + interviewer + ", interviewee="
                + interviewee + "]";
    }
}
