package edu.uga.cs.pcf.operations.glycomics;

import org.switchyard.annotations.Transformer;
import org.w3c.dom.Element;

import edu.uga.cs.pcf.operations.util.TransformerUtil;

public final class FileSizeCheckerTransformer {

    private static final String SOAP_RESPONSE_TEMPLATE =
            "<checkResponse xmlns=\"urn:edu.uga.cs.pcf:pcf-operations:1.0\">\n" +
            "  <string>%s</string>\n" +
            "</checkResponse>";

    @Transformer(to = "{urn:edu.uga.cs.pcf:pcf-operations:1.0}checkResponse")
    public Element transformStringToCheckResponse(String from) {
        return TransformerUtil.toElement(String.format(SOAP_RESPONSE_TEMPLATE, from));
    }

    @Transformer(from = "{urn:edu.uga.cs.pcf:pcf-operations:1.0}check")
    public String transformCheckToString(Element from) {
        return TransformerUtil.getTextContent(from);
    }

}
