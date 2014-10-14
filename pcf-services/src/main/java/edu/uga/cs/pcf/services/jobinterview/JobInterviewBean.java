package edu.uga.cs.pcf.services.jobinterview;

import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;
import org.switchyard.component.bean.Service;

import edu.uga.cs.pcf.common.PcfStatSender;


@Service(JobInterview.class)
public class JobInterviewBean implements JobInterview {

	private static final Logger logger = Logger.getLogger(JobInterviewBean.class);

    @Override
    public InterviewResult process(Interview interview) {
    	logger.info(String.format(
    			"interviewee %s and interviewer %s are in the interviewing procedure",
    			interview.getInterviewee(),
    			interview.getInterviewer()));
        
    	InterviewResult result = perform(interview);   

		return result;
    }

	private InterviewResult perform(Interview interview) {
		InterviewResult result = new InterviewResult(interview);

        Random rand = new Random();
    	// Creates a gaussian distribution with a mean of 10 and Standard deviation of 3
    	// Refers to http://www.javamex.com/tutorials/random_numbers/gaussian_distribution_2.shtml
    	long elapsed = new Double(rand.nextGaussian() * 3 + 10).longValue() * 1000;

        if (rand.nextBoolean()) {
            result.setResult("passed");
        } else {
            result.setResult("failed");
        }

        logger.info(String.format("interview result is %s", result.getResult()));

        String processName = "jobInterviewProcess";
		String serviceName = "jobInterview";
		Date creationDate = new Date();
		PcfStatSender.sendStat(processName, serviceName, elapsed, creationDate);

        return result;
	}

}
