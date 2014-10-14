package edu.uga.cs.pcf.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import scala.util.Random;
import edu.uga.cs.pcf.services.jobinterview.NepotismChecker;

@WebServlet("/NepotismChecker")
public class NepotismCheckerServlet extends HttpServlet {

	private static final long serialVersionUID = 6037356370007322433L;
	private static final Logger logger = Logger
			.getLogger(NepotismCheckerServlet.class);

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		NepotismChecker checker = new NepotismChecker();
		Random rand = new Random();
		boolean result = false;
		int idx = rand.nextInt(4);
		
		switch (idx) {
		case 0:
			result = checker.hasUncle("Diana", "Ben");
//			logger.info("----- Does Diana hasUncle Ben? "
//					+ Boolean.toString(result));
			break;
		case 1:
			result = checker.hasNiece("Bob", "Cathy");
//			logger.info("----- Does Bob hasNiece Cathy? "
//					+ Boolean.toString(result));
			break;
		case 2:
			result = checker.hasCousin("Cathy", "Emma");
//			logger.info("----- Does Cathy hasCousin Emma? "
//					+ Boolean.toString(result));
			break;
		case 3:
			result = checker.isRelative("Emma", "Cathy");
//			logger.info("----- Are Emma & Cathy relatives? "
//					+ Boolean.toString(result));
			break;
		default:
			break;
		}

		response.getWriter().print(Boolean.toString(result));
	}
}
