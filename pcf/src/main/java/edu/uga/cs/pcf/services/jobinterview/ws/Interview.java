
package edu.uga.cs.pcf.services.jobinterview.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for interview complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="interview">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="interviewee" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="interviewer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "interview", propOrder = {
    "interviewee",
    "interviewer"
})
public class Interview {

    protected String interviewee;
    protected String interviewer;

    /**
     * Gets the value of the interviewee property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInterviewee() {
        return interviewee;
    }

    /**
     * Sets the value of the interviewee property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInterviewee(String value) {
        this.interviewee = value;
    }

    /**
     * Gets the value of the interviewer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInterviewer() {
        return interviewer;
    }

    /**
     * Sets the value of the interviewer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInterviewer(String value) {
        this.interviewer = value;
    }

	@Override
	public String toString() {
		return "Interview [interviewee=" + interviewee + ", interviewer="
				+ interviewer + "]";
	}

}
