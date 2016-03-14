package vn.edu.uit.owleditor.core;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.semanticweb.owlapi.util.mansyntax.ManchesterOWLSyntaxParser;
import org.swrlapi.core.SWRLAPIOWLOntology;
import org.swrlapi.core.SWRLRuleRenderer;
import org.swrlapi.sqwrl.exceptions.SQWRLException;
import uk.ac.manchester.cs.owl.explanation.ordering.ExplanationTree;
import vn.edu.uit.owleditor.data.OWLEditorDataFactory;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 12/27/14.
 */
public interface OWLEditorKit {

    void createOntologyFromOntologyDocument(@Nonnull IRI documentIRI) throws OWLOntologyCreationException, SQWRLException;

    void loadOntologyFromOntologyDocument(@Nonnull IRI documentIRI) throws OWLOntologyCreationException, SQWRLException;

    Boolean getReasonerStatus();

    void setReasonerStatus(Boolean value);

    OWLReasoner getReasoner();

    ExplanationTree explain(OWLAxiom axiom);

    OWLEntityRemover getEntityRemover();

    SWRLRuleRenderer getRuleRenderer();

    OWLDataFactory getOWLDataFactory();

    OWLOntology getActiveOntology();

    void setActiveOntology(OWLOntology activeOntology);

    SWRLAPIOWLOntology getSWRLActiveOntology();

    OWLEditorDataFactory getDataFactory();

    ManchesterOWLSyntaxParser getParser();

    OWLClassExpression parseClassExpression(String s);

    OWLOntologyManager getModelManager();

    PrefixManager getPrefixManager();

    void setPrefixManager(PrefixManager prefixManager);

    void removeAllOntologies();

    void removeActiveOntology();

    void addMoreOntology(IRI iri) throws OWLOntologyCreationException;


}
