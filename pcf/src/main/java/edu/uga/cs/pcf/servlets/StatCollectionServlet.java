package edu.uga.cs.pcf.servlets;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import edu.uga.cs.pcf.stat.PcfStat;
import edu.uga.cs.pcf.stat.PcfStatUtil;

/**
 * Provides http access for writing stat information.
 * http://localhost:8080/pcf/stat?processName=123&serviceName=sdfsdf&duration=23&creationDate=08-25-2014%2011:40:27.332
 *
 */
@WebServlet("/stat")
public class StatCollectionServlet extends HttpServlet {

	private static final long serialVersionUID = 3029249319263433530L;
	private static final Logger logger = Logger
			.getLogger(StatCollectionServlet.class);
	private static final ConcurrentMap<String, PcfStat> collection = new ConcurrentHashMap<>();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String action = req.getParameter("action");
		if (action != null && "dump".equals(action)) {
			// dump the whole into file			
			try {
				PcfStatUtil.saveStats(collection.values());
				collection.clear();
			} catch (Exception e) {
				logger.error("failed to save stat file", e);
			}
		} else {
			try {
				String serviceName = req.getParameter("serviceName");
				String processName = req.getParameter("processName");
				String id = UUID.randomUUID().toString();
				long duration = Long.parseLong(req.getParameter("duration"));
				Date creationDate = PcfStatUtil.DATE_FORMATTER.parse(req.getParameter("creationDate"));
				
				PcfStat stat = new PcfStat(processName, serviceName, id, duration, creationDate);
				collection.putIfAbsent(id, stat);
				resp.getWriter().print("1");
			} catch (ParseException e) {
				logger.error("failed to parse creationDate", e);
			}
		}
	}
}
