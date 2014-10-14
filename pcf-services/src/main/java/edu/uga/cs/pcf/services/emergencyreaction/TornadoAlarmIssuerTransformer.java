package edu.uga.cs.pcf.services.emergencyreaction;

import org.switchyard.annotations.Transformer;
import org.w3c.dom.Element;

import edu.uga.cs.pcf.services.util.TransformerUtil;

public final class TornadoAlarmIssuerTransformer {

    private static final String SOAP_RESPONSE_TEMPLATE =
            "<issueResponse xmlns=\"urn:edu.uga.cs.pcf:pcf-services:1.0\">\n" +
            "  <string>%s</string>\n" +
            "</issueResponse>";

    @Transformer(to = "{urn:edu.uga.cs.pcf:pcf-services:1.0}issueResponse")
    public Element transformStringToIssueResponse(String from) {
        return TransformerUtil.toElement(String.format(SOAP_RESPONSE_TEMPLATE, from));
    }

    @Transformer(from = "{urn:edu.uga.cs.pcf:pcf-services:1.0}issue")
    public String transformIssueToString(Element from) {
        return TransformerUtil.getTextContent(from);
    }
}
