package edu.uga.cs.pcf.operations.glycomics;

import java.util.Random;

import org.apache.log4j.Logger;
import org.switchyard.component.bean.Service;

@Service(FileSizeChecker.class)
public class FileSizeCheckerBean implements FileSizeChecker {

    private static final Logger logger = Logger.getLogger(FileSizeCheckerBean.class);

    @Override
    public String check(String input) {
        logger.info("check file size!!!!");
        Random rand = new Random();
        boolean result = rand.nextBoolean();
        logger.info(String.format("check result is %s!", String.valueOf(result)));
        return String.valueOf(result);
    }
}
