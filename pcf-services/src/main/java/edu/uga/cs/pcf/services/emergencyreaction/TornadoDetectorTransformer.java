package edu.uga.cs.pcf.services.emergencyreaction;

import org.switchyard.annotations.Transformer;
import org.w3c.dom.Element;

import edu.uga.cs.pcf.services.util.TransformerUtil;

public final class TornadoDetectorTransformer {

    private static final String SOAP_RESPONSE_TEMPLATE =
            "<detectResponse xmlns=\"urn:edu.uga.cs.pcf:pcf-services:1.0\">\n" +
            "  <string>%s</string>\n" +
            "</detectResponse>";

    @Transformer(to = "{urn:edu.uga.cs.pcf:pcf-services:1.0}detectResponse")
    public Element transformStringToDetectResponse(String from) {
        return TransformerUtil.toElement(String.format(SOAP_RESPONSE_TEMPLATE, from));
    }

    @Transformer(from = "{urn:edu.uga.cs.pcf:pcf-services:1.0}detect")
    public String transformDetectToString(Element from) {
        return TransformerUtil.getTextContent(from);
    }
}
