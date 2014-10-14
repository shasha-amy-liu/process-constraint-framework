package edu.uga.cs.pcf.services.glycomics;

import org.switchyard.component.bean.Service;

@Service(FastDataProcessor.class)
public class FastDataProcessorBean implements FastDataProcessor {

    @Override
    public String fast(String input) {
        String result = "fast process " + input;
        System.out.println(result);
        return result;
    }

}
