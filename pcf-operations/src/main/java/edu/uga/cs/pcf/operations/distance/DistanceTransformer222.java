package edu.uga.cs.pcf.operations.distance;

import org.switchyard.annotations.Transformer;
import org.w3c.dom.Element;

import edu.uga.cs.pcf.operations.util.TransformerUtil;

public final class DistanceTransformer222 {

    private static final String SOAP_RESPONSE_TEMPLATE =
            "<calculateDistanceResponse xmlns=\"urn:edu.uga.cs.pcf:pcf-operations:1.0\">\n" +
            "  <string>%s</string>\n" +
            "</calculateDistanceResponse>";

    @Transformer(to = "{urn:edu.uga.cs.pcf:pcf-operations:1.0}calculateDistanceResponse")
    public Element transformStringToCalculateDistanceResponse(String from) {
        return TransformerUtil.toElement(String.format(SOAP_RESPONSE_TEMPLATE, from));
    }

    @Transformer(from = "{urn:edu.uga.cs.pcf:pcf-operations:1.0}calculateDistance")
    public String transformCalculateDistanceToString(Element from) {
        return TransformerUtil.getTextContent(from);
    }

}
