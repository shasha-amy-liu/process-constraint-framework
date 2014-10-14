package pcf.verifier

import models.pcf.pcl.PCLConstraint
import org.apache.ode.bpel.o.OProcess
import org.semanticweb.owlapi.model.OWLOntology

/**
 * After pcl and bpel files are parsed, perform static semantic verification against the ProContO:
 * <ul>
 * <li>declaration</li>
 * <li>navigability</li>
 * <li>reachability</li>
 * </ul>
 */
class SemanticVerifier(val pcl: PCLConstraint, val bpel: OProcess, val ontology: OWLOntology) {

}

object SemanticVerifier {
  // def 
}