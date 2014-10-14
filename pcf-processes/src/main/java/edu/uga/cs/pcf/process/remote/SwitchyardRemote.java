package edu.uga.cs.pcf.process.remote;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.switchyard.remote.RemoteInvoker;
import org.switchyard.remote.RemoteMessage;
import org.switchyard.remote.http.HttpInvoker;

public class SwitchyardRemote {

    private RemoteInvoker invoker;

    public SwitchyardRemote(String url) {
        super();
        this.invoker = new HttpInvoker(url);
    }

    public void invokeAndForget(String tns, String serviceName, String content) throws IOException {
        invoker.invoke(new RemoteMessage().setService(
                new QName(tns, serviceName))
                .setContent(content));
    }

    public String invoke(String tns, String serviceName, String content) throws IOException {
        RemoteMessage msg = invoker.invoke(new RemoteMessage().setService(
                new QName(tns, serviceName))
                .setContent(content));
        if (msg != null) {
            return msg.getContent().toString();
        }

        return null;
    }
}
