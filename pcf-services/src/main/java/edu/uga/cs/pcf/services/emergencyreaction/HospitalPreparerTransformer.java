package edu.uga.cs.pcf.services.emergencyreaction;

import org.switchyard.annotations.Transformer;
import org.w3c.dom.Element;

import edu.uga.cs.pcf.services.util.TransformerUtil;

public final class HospitalPreparerTransformer {

    private static final String SOAP_RESPONSE_TEMPLATE =
            "<prepareResponse xmlns=\"urn:edu.uga.cs.pcf:pcf-services:1.0\">\n" +
            "  <string>%s</string>\n" +
            "</prepareResponse>";

    @Transformer(to = "{urn:edu.uga.cs.pcf:pcf-services:1.0}prepareResponse")
    public Element transformStringToPrepareResponse(String from) {
        return TransformerUtil.toElement(String.format(SOAP_RESPONSE_TEMPLATE, from));
    }

    @Transformer(from = "{urn:edu.uga.cs.pcf:pcf-services:1.0}prepare")
    public String transformPrepareToString(Element from) {
        return TransformerUtil.getTextContent(from);
    }

}
