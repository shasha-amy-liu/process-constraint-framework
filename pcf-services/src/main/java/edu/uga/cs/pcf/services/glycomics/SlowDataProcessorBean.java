package edu.uga.cs.pcf.services.glycomics;

import org.switchyard.component.bean.Service;

@Service(SlowDataProcessor.class)
public class SlowDataProcessorBean implements SlowDataProcessor {

    @Override
    public String slow(String input) {
        String result = "slow process " + input;
        System.out.println(result);
        return result;
    }

}
