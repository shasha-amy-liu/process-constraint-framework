package edu.uga.cs.pcf.operations.jobinterview;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.switchyard.component.bean.Service;

@Service(NepotismChecker.class)
public class NepotismCheckerBean implements NepotismChecker {

    private static final Logger logger = Logger.getLogger(NepotismCheckerBean.class);

    @Override
    public String check(String input) {
        logger.info("check nepotism!!!!");

        String response = fetchCheckerResult();
        boolean result = Boolean.parseBoolean(response);
        logger.info(String.format("check result is %s", Boolean.toString(result)));
        return String.valueOf(result);
    }

    private String fetchCheckerResult() {
        String url = "http://localhost:8080/pcf/NepotismChecker";
        URLConnection connection = null;
        InputStream response = null;
        Scanner s = null;
        try {
            connection = new URL(url).openConnection();
            String charset = "UTF-8";
            connection.setRequestProperty("Accept-Charset", charset);
            response = connection.getInputStream();
            s = new Scanner(response).useDelimiter("\\A");
            String result = (s.hasNext() ? s.next() : "").trim();            
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        	if (response != null) {
        		try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        	if (s != null) {
        		s.close();
        	}
        }

        return Boolean.toString(Boolean.FALSE);
    }

    public static void main(String[] args) {
        BasicConfigurator.configure();
        new NepotismCheckerBean().check("any input");
    }
}
