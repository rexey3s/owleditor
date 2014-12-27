package vn.edu.uit.owleditor.core;


import com.clarkparsia.owlapi.explanation.DefaultExplanationGenerator;
import com.clarkparsia.owlapi.explanation.util.ExplanationProgressMonitor;
import com.clarkparsia.owlapi.explanation.util.SilentExplanationProgressMonitor;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import com.vaadin.ui.Notification;
import org.mindswap.pellet.exceptions.InconsistentOntologyException;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxOWLObjectRendererImpl;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxPrefixNameShortFormProvider;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.*;
import org.semanticweb.owlapi.util.mansyntax.ManchesterOWLSyntaxParser;
import org.springframework.stereotype.Repository;
import org.swrlapi.core.SWRLAPIFactory;
import org.swrlapi.core.SWRLAPIOWLOntology;
import org.swrlapi.core.SWRLAPIRenderer;
import org.swrlapi.core.impl.DefaultSWRLAPIRenderer;
import org.vaadin.spring.VaadinSessionScope;
import uk.ac.manchester.cs.owl.explanation.ordering.ExplanationOrderer;
import uk.ac.manchester.cs.owl.explanation.ordering.ExplanationOrdererImpl;
import uk.ac.manchester.cs.owl.explanation.ordering.ExplanationTree;
import vn.edu.uit.owleditor.data.OWLEditorDataFactory;
import vn.edu.uit.owleditor.data.OWLEditorDataFactoryImpl;
import vn.edu.uit.owleditor.utils.EditorUtils;

import javax.annotation.Nonnull;
import java.util.Collections;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/11/14.
 */
@Repository
//@Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
@VaadinSessionScope
public class OWLEditorKitImpl implements OWLEditorKit {

    private static final ShortFormProvider sfp = new SimpleShortFormProvider();

    private static final OWLObjectRenderer renderer = new ManchesterOWLSyntaxOWLObjectRendererImpl();

    private final ExplanationProgressMonitor progressMonitor = new SilentExplanationProgressMonitor();

    private final OWLOntologyManager modelManager = OWLManager.createOWLOntologyManager();

    private SWRLAPIRenderer ruleRenderer;
    private ExplanationOrderer explanationOrderer;
    private DefaultExplanationGenerator explanationGenerator;
    private OWLEditorDataFactory editorDataFactory;
    private PrefixManager prefixManager;
    private OWLOntology activeOntology;
    private OWLEntityRemover entityRemover;
    /* Pellet Reasoner Interface */
    private OWLReasonerFactory reasonerFactory;
    private OWLReasoner reasoner;
    private Boolean reasonerStatus = false;
    /* Converted SWRLOntology  used for writing and reading SWRL rules */
    private SWRLAPIOWLOntology swrlActiveOntology;
    /* Variables for OWLClassExpression Parser */
    private ManchesterOWLSyntaxParser parser;
    private ShortFormProvider sfpFormat;
    private BidirectionalShortFormProvider bidirectionalSfp;


    public OWLEditorKitImpl() {
        initialise();

    }

    public static String getShortForm(OWLEntity entity) {
        return sfp.getShortForm(entity);
    }

    public static String render(OWLObject object) {
        return renderer.render(object);
    }


    public void loadOntologyFromOntologyDocument(@Nonnull IRI documentIRI) throws OWLOntologyCreationException {
        initialise();
        activeOntology = modelManager.loadOntologyFromOntologyDocument(documentIRI);
        swrlActiveOntology = SWRLAPIFactory.createOntology(activeOntology);
        activeOntology.getDirectImportsDocuments();
        modelManager.setOntologyDocumentIRI(activeOntology, activeOntology.getOntologyID().getDefaultDocumentIRI().get());
        prefixManager = new DefaultPrefixManager(null, null, modelManager.getOntologyDocumentIRI(activeOntology) + "#");
        ruleRenderer = new DefaultSWRLAPIRenderer(swrlActiveOntology);
        entityRemover = new OWLEntityRemover(Collections.singleton(activeOntology));

        sfpFormat = new ManchesterOWLSyntaxPrefixNameShortFormProvider(activeOntology);
        bidirectionalSfp = new BidirectionalShortFormProviderAdapter(modelManager.getOntologies(), sfpFormat);
        parser.setOWLEntityChecker(new ShortFormEntityChecker(bidirectionalSfp));
        parser.setDefaultOntology(activeOntology);

        reasonerToggle();
    }

    public ExplanationTree explain(OWLAxiom axiom) {
        EditorUtils.checkNotNull(axiom, "Cannot generate any explanations!");
        return explanationOrderer.getOrderedExplanation(axiom, explanationGenerator.getExplanation(axiom));
    }
    private void initialise() {
        reasonerFactory = PelletReasonerFactory.getInstance();
        explanationOrderer = new ExplanationOrdererImpl(modelManager);
        parser = OWLManager.createManchesterParser();
        editorDataFactory = new OWLEditorDataFactoryImpl(this);

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
                reasoner = reasonerFactory.createReasoner(activeOntology, new SimpleConfiguration());
                reasoner.precomputeInferences();


                explanationGenerator = new DefaultExplanationGenerator(modelManager, reasonerFactory, activeOntology, reasoner, progressMonitor);
            } catch (InconsistentOntologyException iconsEx) {
                Notification.show(iconsEx.getMessage(), Notification.Type.ERROR_MESSAGE);
            }

        } else {
            if (reasoner != null) reasoner.dispose();
            if (explanationGenerator != null) explanationGenerator = null;
        }
    }

    public OWLEntityRemover getEntityRemover() {
        return entityRemover;
    }

    public ManchesterOWLSyntaxParser getParser() {
        return parser;
    }

    public SWRLAPIRenderer getRuleRenderer() {
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
        sfpFormat = new ManchesterOWLSyntaxPrefixNameShortFormProvider(activeOntology);
        bidirectionalSfp = new BidirectionalShortFormProviderAdapter(modelManager.getOntologies(), sfpFormat);
        parser.setOWLEntityChecker(new ShortFormEntityChecker(bidirectionalSfp));
        parser.setDefaultOntology(activeOntology);
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

}
