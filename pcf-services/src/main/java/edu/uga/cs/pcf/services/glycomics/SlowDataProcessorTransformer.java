package edu.uga.cs.pcf.services.glycomics;

import org.switchyard.annotations.Transformer;
import org.w3c.dom.Element;

import edu.uga.cs.pcf.services.util.TransformerUtil;

public final class SlowDataProcessorTransformer {

    private static final String SOAP_RESPONSE_TEMPLATE =
            "<slowResponse xmlns=\"urn:edu.uga.cs.pcf:pcf-services:1.0\">\n" +
            "  <string>%s</string>\n" +
            "</slowResponse>";

    @Transformer(to = "{urn:edu.uga.cs.pcf:pcf-services:1.0}slowResponse")
    public Element transformStringToSlowResponse(String from) {
        return TransformerUtil.toElement(String.format(SOAP_RESPONSE_TEMPLATE, from));
    }

    @Transformer(from = "{urn:edu.uga.cs.pcf:pcf-services:1.0}slow")
    public String transformSlowToString(Element from) {
        return TransformerUtil.getTextContent(from);
    }

}
