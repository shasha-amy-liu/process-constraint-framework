package edu.uga.cs.pcf.services.emergencyreaction;

import org.switchyard.annotations.Transformer;
import org.w3c.dom.Element;

import edu.uga.cs.pcf.services.util.TransformerUtil;

public final class ShelterActivatorTransformer {

    private static final String SOAP_RESPONSE_TEMPLATE =
            "<activateResponse xmlns=\"urn:edu.uga.cs.pcf:pcf-services:1.0\">\n" +
            "  <string>%s</string>\n" +
            "</activateResponse>";

    @Transformer(to = "{urn:edu.uga.cs.pcf:pcf-services:1.0}activateResponse")
    public Element transformStringToActivateResponse(String from) {
        return TransformerUtil.toElement(String.format(SOAP_RESPONSE_TEMPLATE, from));
    }

    @Transformer(from = "{urn:edu.uga.cs.pcf:pcf-services:1.0}activate")
    public String transformActivateToString(Element from) {
        return TransformerUtil.getTextContent(from);
    }

}
