package edu.uga.cs.pcf.services.glycomics;

import org.switchyard.annotations.Transformer;
import org.w3c.dom.Element;

import edu.uga.cs.pcf.services.util.TransformerUtil;

public final class FastDataProcessorTransformer {

    private static final String SOAP_RESPONSE_TEMPLATE =
            "<fastResponse xmlns=\"urn:edu.uga.cs.pcf:pcf-services:1.0\">\n" +
            "  <string>%s</string>\n" +
            "</fastResponse>";

    @Transformer(to = "{urn:edu.uga.cs.pcf:pcf-services:1.0}fastResponse")
    public Element transformStringToFastResponse(String from) {
        return TransformerUtil.toElement(String.format(SOAP_RESPONSE_TEMPLATE, from));
    }

    @Transformer(from = "{urn:edu.uga.cs.pcf:pcf-services:1.0}fast")
    public String transformFastToString(Element from) {
        return TransformerUtil.getTextContent(from);
    }

}
