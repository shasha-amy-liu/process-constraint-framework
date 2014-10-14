package edu.uga.cs.pcf.services.util;

import java.io.StringReader;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TransformerUtil {

    private static final Logger logger = Logger.getLogger(TransformerUtil.class);

    public static Element toElement(String xml) {
        logger.info(String.format("xml to be transformed\n%s", xml));
        DOMResult dom = new DOMResult();
        try {
            TransformerFactory.newInstance().newTransformer().transform(new StreamSource(new StringReader(xml)), dom);
        } catch (Exception e) {
            logger.error(String.format("failed to transform element\n%s", xml), e);
        }
        return ((Document) dom.getNode()).getDocumentElement();
    }

    public static String getTextContent(Element ele) {
        NodeList list = ele.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                return node.getTextContent();
            }
        }
        return "";
    }
}
