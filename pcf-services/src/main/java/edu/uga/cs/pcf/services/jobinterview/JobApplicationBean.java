package edu.uga.cs.pcf.services.jobinterview;

import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;
import org.switchyard.component.bean.Service;

import edu.uga.cs.pcf.common.PcfStatSender;

@Service(JobApplication.class)
public class JobApplicationBean implements JobApplication {

	private static final Logger logger = Logger
			.getLogger(JobApplicationBean.class);
	private static final String[] interviewers = { "smith", "jack", "miller",
			"white" };

	@Override
	public Interview apply(Interview interview) {
		logger.info(String.format("interviewee %s applied for this position.",
				interview.getInterviewee()));
		logger.info("wait for an interviewer to choose this interviewee...");

		interview = process(interview);
		return interview;
	}

	private Interview process(Interview interview) {
		Random rand = new Random();

		// Creates a gaussian distribution with a mean of 4 and Standard deviation of 2
		// Refers to http://www.javamex.com/tutorials/random_numbers/gaussian_distribution_2.shtml
		long elapsed = new Double(rand.nextGaussian() * 2 + 4).longValue() * 1000;

		interview.setInterviewer(interviewers[rand.nextInt(interviewers.length)]);
		logger.info(String.format(
				"interviewee %s has been assgined to an interviewer %s randomly. ",
				interview.getInterviewee(),
				interview.getInterviewer()));

		String processName = "jobInterviewProcess";
		String serviceName = "jobApplication";
		Date creationDate = new Date();
		PcfStatSender.sendStat(processName, serviceName, elapsed, creationDate);

		return interview;
	}

}
