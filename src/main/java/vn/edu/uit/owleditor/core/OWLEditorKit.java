package vn.edu.uit.owleditor.core;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.semanticweb.owlapi.util.mansyntax.ManchesterOWLSyntaxParser;
import org.springframework.stereotype.Service;
import org.swrlapi.core.SWRLAPIOWLOntology;
import org.swrlapi.core.SWRLAPIRenderer;
import uk.ac.manchester.cs.owl.explanation.ordering.ExplanationTree;
import vn.edu.uit.owleditor.data.OWLEditorDataFactory;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/27/14.
 */
@Service
public interface OWLEditorKit {


    public void loadOntologyFromOntologyDocument(@Nonnull IRI documentIRI) throws OWLOntologyCreationException;

    public Boolean getReasonerStatus();

    public void setReasonerStatus(Boolean value);

    public OWLReasoner getReasoner();

    public ExplanationTree explain(OWLAxiom axiom);

    public OWLEntityRemover getEntityRemover();

    public SWRLAPIRenderer getRuleRenderer();

    public OWLDataFactory getOWLDataFactory();

    public OWLOntology getActiveOntology();

    public void setActiveOntology(OWLOntology activeOntology);

    public SWRLAPIOWLOntology getSWRLActiveOntology();

    public OWLEditorDataFactory getDataFactory();

    public ManchesterOWLSyntaxParser getParser();

    public OWLClassExpression parseClassExpression(String s);

    public OWLOntologyManager getModelManager();

    public PrefixManager getPrefixManager();

    public void setPrefixManager(PrefixManager prefixManager);
    
    public void removeActiveOntology();
    
}
