package edu.uga.cs.pcf.services.helloworld;

import org.switchyard.component.bean.Service;

@Service(HelloWorld.class)
public class HelloWorldBean implements HelloWorld {

    @Override
    public String say(String name) {
        return "hello " + name;
    }

}
