package vn.edu.uit.owleditor.core;


import com.clarkparsia.owlapi.explanation.DefaultExplanationGenerator;
import com.clarkparsia.owlapi.explanation.util.ExplanationProgressMonitor;
import com.clarkparsia.owlapi.explanation.util.SilentExplanationProgressMonitor;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Notification;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxOWLObjectRendererImpl;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxPrefixNameShortFormProvider;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.*;
import org.semanticweb.owlapi.util.*;
import org.semanticweb.owlapi.util.mansyntax.ManchesterOWLSyntaxParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swrlapi.core.SWRLAPIOWLOntology;
import org.swrlapi.core.SWRLRuleRenderer;
import org.swrlapi.factory.DefaultIRIResolver;
import org.swrlapi.factory.SWRLAPIFactory;
import org.swrlapi.sqwrl.exceptions.SQWRLException;
import uk.ac.manchester.cs.owl.explanation.ordering.ExplanationOrderer;
import uk.ac.manchester.cs.owl.explanation.ordering.ExplanationOrdererImpl;
import uk.ac.manchester.cs.owl.explanation.ordering.ExplanationTree;
import vn.edu.uit.owleditor.data.OWLEditorDataFactory;
import vn.edu.uit.owleditor.data.OWLEditorDataFactoryImpl;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Iterator;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 11/11/14.
 */
@UIScope
@SpringComponent
public class OWLEditorKitImpl implements OWLEditorKit {

    private static final Logger LOG = LoggerFactory.getLogger(OWLEditorKitImpl.class);

    private static final ShortFormProvider sfp = new SimpleShortFormProvider();

    private static final OWLObjectRenderer renderer = new ManchesterOWLSyntaxOWLObjectRendererImpl();

    private final ExplanationProgressMonitor progressMonitor = new SilentExplanationProgressMonitor();

    private final OWLOntologyManager modelManager = OWLManager.createOWLOntologyManager();

    private SWRLRuleRenderer ruleRenderer;
    private ExplanationOrderer explanationOrderer = new ExplanationOrdererImpl(modelManager);
    private DefaultExplanationGenerator explanationGenerator;
    private OWLEditorDataFactory editorDataFactory = new OWLEditorDataFactoryImpl();
    private PrefixManager prefixManager;
    private OWLOntology activeOntology;
    private OWLEntityRemover entityRemover;
    /* Pellet Reasoner Interface */
    private OWLReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance();
    private OWLReasoner reasoner;
    private Boolean reasonerStatus = false;
    /* Converted SWRLOntology  used for writing and reading SWRL rules */
    private SWRLAPIOWLOntology swrlActiveOntology;
    /* Variables for OWLClassExpression Parser */
    private ManchesterOWLSyntaxParser parser = OWLManager.createManchesterParser();

    private ShortFormProvider sfpFormat;
    private BidirectionalShortFormProvider bidirectionalSfp;


    public OWLEditorKitImpl() {
    }

    public static String getShortForm(OWLEntity entity) {
        return sfp.getShortForm(entity);
    }

    public static String render(OWLObject object) {
        return renderer.render(object);
    }

    public void createOntologyFromOntologyDocument(@Nonnull IRI documentIRI) throws OWLOntologyCreationException, SQWRLException {
        activeOntology = modelManager.createOntology(documentIRI);
        initialise();
    }

    public void loadOntologyFromOntologyDocument(@Nonnull IRI documentIRI) throws OWLOntologyCreationException, SQWRLException {
        activeOntology = modelManager.loadOntologyFromOntologyDocument(documentIRI);
        initialise();
    }

    public ExplanationTree explain(OWLAxiom axiom) {
        return explanationOrderer.getOrderedExplanation(axiom, explanationGenerator.getExplanation(axiom));
    }
    private void initialise() throws SQWRLException {
        swrlActiveOntology = SWRLAPIFactory.createSWRLAPIOntology(activeOntology);
        editorDataFactory.setActiveOntology(this.activeOntology);

//        activeOntology.getDirectImportsDocuments();
        modelManager.setOntologyDocumentIRI(activeOntology, activeOntology.getOntologyID().getDefaultDocumentIRI().get());
        prefixManager = new DefaultPrefixManager(null, null, modelManager.getOntologyDocumentIRI(activeOntology) + "#");
        ruleRenderer = SWRLAPIFactory.createSWRLRuleRenderer(activeOntology, new DefaultIRIResolver(modelManager.getOntologyDocumentIRI(activeOntology) + "#"));

        entityRemover = new OWLEntityRemover(Collections.singleton(activeOntology));
        sfpFormat = new ManchesterOWLSyntaxPrefixNameShortFormProvider(activeOntology);
        bidirectionalSfp = new BidirectionalShortFormProviderAdapter(modelManager.getOntologies(), sfpFormat);
        parser.setOWLEntityChecker(new ShortFormEntityChecker(bidirectionalSfp));
        parser.setDefaultOntology(activeOntology);
        reasoner = reasonerFactory.createReasoner(activeOntology, new SimpleConfiguration());
        explanationGenerator = new DefaultExplanationGenerator(modelManager, reasonerFactory, activeOntology, reasoner, progressMonitor);

    }


    public OWLReasoner getReasoner() {
        return reasoner;
    }

    public Boolean getReasonerStatus() {
        return reasonerStatus;
    }

    public void setReasonerStatus(Boolean value) {
        this.reasonerStatus = value;
        reasonerToggle();
    }

    private void reasonerToggle() {
        if (reasonerStatus) {
            try {
                reasoner.flush();
                reasoner.precomputeInferences(
                        InferenceType.CLASS_HIERARCHY,
                        InferenceType.CLASS_ASSERTIONS,
                        InferenceType.OBJECT_PROPERTY_ASSERTIONS,
                        InferenceType.DATA_PROPERTY_ASSERTIONS);

                Notification.show("Reasoner activated");

            } catch (InconsistentOntologyException iconsEx) {
                LOG.error(iconsEx.getMessage(), this);
            }
        } else {
//            if (reasoner != null) reasoner.dispose();
//            if (explanationGenerator != null) explanationGenerator = null;

            Notification.show("Reasoner deactivated");

        }
    }

    public OWLEntityRemover getEntityRemover() {
        return entityRemover;
    }

    public ManchesterOWLSyntaxParser getParser() {
        return parser;
    }


    public OWLClassExpression parseClassExpression(String s) throws OWLParserException {

        sfpFormat = new ManchesterOWLSyntaxPrefixNameShortFormProvider(activeOntology);
        bidirectionalSfp = new BidirectionalShortFormProviderAdapter(modelManager.getOntologies(), sfpFormat);
        parser.setOWLEntityChecker(new ShortFormEntityChecker(bidirectionalSfp));
        parser.setStringToParse(s);
        return parser.parseClassExpression();
    }

    public SWRLRuleRenderer getRuleRenderer() {
        return ruleRenderer;
    }

    public OWLDataFactory getOWLDataFactory() {
        return modelManager.getOWLDataFactory();
    }

    public OWLDataFactory getSWRLDataFactory() {
        return swrlActiveOntology.getOWLDataFactory();
    }

    public OWLOntology getActiveOntology() {
        return activeOntology;
    }

    public void setActiveOntology(OWLOntology activeOntology) {
        this.activeOntology = activeOntology;
        editorDataFactory.setActiveOntology(this.activeOntology);
        sfpFormat = new ManchesterOWLSyntaxPrefixNameShortFormProvider(this.activeOntology);
        bidirectionalSfp = new BidirectionalShortFormProviderAdapter(modelManager.getOntologies(), sfpFormat);
        parser.setOWLEntityChecker(new ShortFormEntityChecker(bidirectionalSfp));
        parser.setDefaultOntology(this.activeOntology);
    }


    public SWRLAPIOWLOntology getSWRLActiveOntology() {
        return swrlActiveOntology;
    }

    public OWLEditorDataFactory getDataFactory() {
        return editorDataFactory;
    }

    public OWLOntologyManager getModelManager() {
        return modelManager;
    }

    public PrefixManager getPrefixManager() {
        return prefixManager;
    }

    public void setPrefixManager(PrefixManager prefixManager) {
        this.prefixManager = prefixManager;
    }

    public void removeActiveOntology() {
        if (activeOntology != null && modelManager.contains(activeOntology)) {
            modelManager.removeOntology(activeOntology);
            activeOntology = null;
        }
    }

    public void removeAllOntologies() {
        Iterator<OWLOntology> onts = modelManager.getOntologies().iterator();
        while (onts.hasNext()) {
            OWLOntology ont = onts.next();
            onts.remove();
        }
    }
    public void addMoreOntology(@Nonnull IRI iri) throws OWLOntologyCreationException {
        if (!modelManager.contains(iri)) {
            activeOntology = modelManager.loadOntologyFromOntologyDocument(iri);
        }

    }

}
