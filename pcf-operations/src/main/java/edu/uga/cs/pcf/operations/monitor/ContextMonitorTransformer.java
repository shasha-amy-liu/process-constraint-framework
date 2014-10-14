package edu.uga.cs.pcf.operations.monitor;

import org.switchyard.annotations.Transformer;
import org.w3c.dom.Element;

import edu.uga.cs.pcf.operations.util.TransformerUtil;

public final class ContextMonitorTransformer {

    private static final String SET_SOAP_RESPONSE_TEMPLATE =
            "<setResponse xmlns=\"urn:edu.uga.cs.pcf:pcf-operations:1.0\">\n" +
            "  <string>%s</string>\n" +
            "</setResponse>";

    @Transformer(to = "{urn:edu.uga.cs.pcf:pcf-operations:1.0}setResponse")
    public Element transformStringToSetResponse(String from) {
        return TransformerUtil.toElement(String.format(SET_SOAP_RESPONSE_TEMPLATE, from));
    }

    @Transformer(from = "{urn:edu.uga.cs.pcf:pcf-operations:1.0}set")
    public String transformSetToString(Element from) {
        return TransformerUtil.getTextContent(from);
    }

}
