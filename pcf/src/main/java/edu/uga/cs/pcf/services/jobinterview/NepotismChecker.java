package edu.uga.cs.pcf.services.jobinterview;

import java.io.InputStream;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.vocab.PrefixOWLOntologyFormat;

import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

public class NepotismChecker {
    private static final String BASE_URI = "http://www.amy.pcf/nepotism/family.owl";
    private static final String NEPTISM_ONTO_FILE = "/nepotism-ontology.owl";

    private OWLOntologyManager manager;
    private OWLOntology ontology;
    private OWLDataFactory dataFacotry;
    private OWLReasonerFactory reasonerFacotry;
    private OWLReasoner reasoner;
    private PrefixOWLOntologyFormat pm;

    public NepotismChecker() {
        InputStream is = this.getClass().getResourceAsStream(NEPTISM_ONTO_FILE);

        manager = OWLManager.createOWLOntologyManager();
        try {
            ontology = manager.loadOntologyFromOntologyDocument(is);
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
        dataFacotry = manager.getOWLDataFactory();

        reasonerFacotry = PelletReasonerFactory.getInstance();
        reasoner = reasonerFacotry.createReasoner(ontology,
                new SimpleConfiguration());

        pm = (PrefixOWLOntologyFormat) manager.getOntologyFormat(ontology);
        pm.setDefaultPrefix(BASE_URI + "#");

    }

    public Boolean hasUncle(String sub, String obj) {
        OWLNamedIndividual indivdualSub = dataFacotry.getOWLNamedIndividual(":"
                + sub, pm);
        OWLNamedIndividual indivdualObj = dataFacotry.getOWLNamedIndividual(":"
                + obj, pm);

        OWLObjectProperty hasUncle = dataFacotry.getOWLObjectProperty(
                ":hasUncle", pm);

        OWLObjectPropertyAssertionAxiom assertionTree = dataFacotry
                .getOWLObjectPropertyAssertionAxiom(hasUncle, indivdualSub,
                        indivdualObj);

        return reasoner.isEntailed(assertionTree);
    }

    public Boolean hasAunt(String sub, String obj) {
        OWLNamedIndividual indivdualSub = dataFacotry.getOWLNamedIndividual(":"
                + sub, pm);
        OWLNamedIndividual indivdualObj = dataFacotry.getOWLNamedIndividual(":"
                + obj, pm);

        OWLObjectProperty hasAunt = dataFacotry.getOWLObjectProperty(
                ":hasAunt", pm);

        OWLObjectPropertyAssertionAxiom assertionTree = dataFacotry
                .getOWLObjectPropertyAssertionAxiom(hasAunt, indivdualSub,
                        indivdualObj);

        return reasoner.isEntailed(assertionTree);
    }

    public Boolean hasNephew(String sub, String obj) {
        OWLNamedIndividual indivdualSub = dataFacotry.getOWLNamedIndividual(":"
                + sub, pm);
        OWLNamedIndividual indivdualObj = dataFacotry.getOWLNamedIndividual(":"
                + obj, pm);

        OWLObjectProperty hasNephew = dataFacotry.getOWLObjectProperty(
                ":hasNephew", pm);

        OWLObjectPropertyAssertionAxiom assertionTree = dataFacotry
                .getOWLObjectPropertyAssertionAxiom(hasNephew, indivdualSub,
                        indivdualObj);

        return reasoner.isEntailed(assertionTree);
    }

    public Boolean hasNiece(String sub, String obj) {
        OWLNamedIndividual indivdualSub = dataFacotry.getOWLNamedIndividual(":"
                + sub, pm);
        OWLNamedIndividual indivdualObj = dataFacotry.getOWLNamedIndividual(":"
                + obj, pm);

        OWLObjectProperty hasNiece = dataFacotry.getOWLObjectProperty(
                ":hasNiece", pm);

        OWLObjectPropertyAssertionAxiom assertionTree = dataFacotry
                .getOWLObjectPropertyAssertionAxiom(hasNiece, indivdualSub,
                        indivdualObj);

        return reasoner.isEntailed(assertionTree);
    }

    public Boolean hasSibling(String sub, String obj) {
        OWLNamedIndividual indivdualSub = dataFacotry.getOWLNamedIndividual(":"
                + sub, pm);
        OWLNamedIndividual indivdualObj = dataFacotry.getOWLNamedIndividual(":"
                + obj, pm);

        OWLObjectProperty hasSibling = dataFacotry.getOWLObjectProperty(
                ":hasSibling", pm);

        OWLObjectPropertyAssertionAxiom assertionTree = dataFacotry
                .getOWLObjectPropertyAssertionAxiom(hasSibling, indivdualSub,
                        indivdualObj);

        return reasoner.isEntailed(assertionTree);
    }

    public Boolean hasChild(String sub, String obj) {
        OWLNamedIndividual indivdualSub = dataFacotry.getOWLNamedIndividual(":"
                + sub, pm);
        OWLNamedIndividual indivdualObj = dataFacotry.getOWLNamedIndividual(":"
                + obj, pm);

        OWLObjectProperty hasChild = dataFacotry.getOWLObjectProperty(
                ":hasChild", pm);

        OWLObjectPropertyAssertionAxiom assertionTree = dataFacotry
                .getOWLObjectPropertyAssertionAxiom(hasChild, indivdualSub,
                        indivdualObj);

        return reasoner.isEntailed(assertionTree);
    }

    public Boolean hasParent(String sub, String obj) {
        OWLNamedIndividual indivdualSub = dataFacotry.getOWLNamedIndividual(":"
                + sub, pm);
        OWLNamedIndividual indivdualObj = dataFacotry.getOWLNamedIndividual(":"
                + obj, pm);

        OWLObjectProperty hasParent = dataFacotry.getOWLObjectProperty(
                ":hasParent", pm);

        OWLObjectPropertyAssertionAxiom assertionTree = dataFacotry
                .getOWLObjectPropertyAssertionAxiom(hasParent, indivdualSub,
                        indivdualObj);

        return reasoner.isEntailed(assertionTree);
    }

    public Boolean hasCousin(String sub, String obj) {
        OWLNamedIndividual indivdualSub = dataFacotry.getOWLNamedIndividual(":"
                + sub, pm);
        OWLNamedIndividual indivdualObj = dataFacotry.getOWLNamedIndividual(":"
                + obj, pm);

        OWLObjectProperty hasCousin = dataFacotry.getOWLObjectProperty(
                ":hasCousin", pm);

        OWLObjectPropertyAssertionAxiom assertionTree = dataFacotry
                .getOWLObjectPropertyAssertionAxiom(hasCousin, indivdualSub,
                        indivdualObj);

        return reasoner.isEntailed(assertionTree);
    }

    public Boolean isRelative(String sub, String obj) {
        Boolean hasParent = hasParent(sub, obj);
        Boolean hasChild = hasChild(sub, obj);
        Boolean hasSibling = hasSibling(sub, obj);

        Boolean hasAunt = hasAunt(sub, obj);
        Boolean hasUncle = hasUncle(sub, obj);
        Boolean hasNephew = hasNephew(sub, obj);
        Boolean hasNiece = hasNiece(sub, obj);

        Boolean hasCousin = hasCousin(sub, obj);

        return hasParent || hasChild || hasSibling || hasAunt || hasUncle
                || hasNephew || hasNiece || hasCousin;

    }

    public static void main(String[] args) {
        NepotismChecker checker = new NepotismChecker();

        System.out.println("----- Does Diana hasUncle Ben? "
                + checker.hasUncle("Diana", "Ben"));
        System.out.println("----- Does Diana hasAunt Alyssa? "
                + checker.hasAunt("Diana", "Alyssa"));
        // System.out.println( "----- Does Alyssa hasChild Cathy? " +
        // checker.hasChild("Alyssa", "Cathy") );
        System.out.println("----- Does Alice hasNiece Cathy? "
                + checker.hasNiece("Alice", "Cathy"));
        System.out.println("----- Does Bob hasNiece Cathy? "
                + checker.hasNiece("Bob", "Cathy"));
        System.out.println("----- Does Bob hasNephew Eric? "
                + checker.hasNephew("Bob", "Eric"));

        System.out.println("----- Does Cathy hasCousin David? "
                + checker.hasCousin("Cathy", "David"));
        System.out.println("----- Does Cathy hasCousin Emma? "
                + checker.hasCousin("Cathy", "Emma"));
        System.out.println("----- Does Emma hasCousin David? "
                + checker.hasCousin("Emma", "David"));

        System.out.println("----- Are Emma & David relatives? "
                + checker.isRelative("Emma", "David"));
        System.out.println("----- Are Emma & Cathy relatives? "
                + checker.isRelative("Emma", "Cathy"));
    }
}
