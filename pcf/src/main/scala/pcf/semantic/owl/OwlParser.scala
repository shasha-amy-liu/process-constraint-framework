package pcf.semantic.owl

import java.io.File

import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.model.IRI
import org.semanticweb.owlapi.model.OWLOntology
import org.slf4j.LoggerFactory

class OwlParser(val path: String) {

  val logger = LoggerFactory.getLogger(getClass())

  val m = OWLManager.createOWLOntologyManager()

  def parse: Option[OWLOntology] = {
    var result: Option[OWLOntology] = None
    val owlFile = new File(path)
    if (owlFile.exists()) {
      val ontoIri = IRI.create(owlFile)
      result = Some(m.loadOntologyFromOntologyDocument(ontoIri))
    }

    result
  }
}