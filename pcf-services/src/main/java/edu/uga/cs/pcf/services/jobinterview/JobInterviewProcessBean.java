package edu.uga.cs.pcf.services.jobinterview;

import org.switchyard.component.bean.Service;

@Service(JobInterviewProcess.class)
public class JobInterviewProcessBean implements JobInterviewProcess {

    @Override
    public InterviewResult process(Interview interview) {
        InterviewResult result = new InterviewResult();
        result.setInterviewee(interview.getInterviewee());
        result.setInterviewer(interview.getInterviewer());
        result.setResult("passed");
        return result;
    }

}
