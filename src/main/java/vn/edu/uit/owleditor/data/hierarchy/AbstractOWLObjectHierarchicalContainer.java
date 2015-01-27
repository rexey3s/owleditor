package vn.edu.uit.owleditor.data.hierarchy;


import com.google.common.eventbus.Subscribe;
import com.vaadin.data.util.HierarchicalContainer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.semanticweb.owlapi.util.OWLOntologyChangeVisitorAdapter;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;
import vn.edu.uit.owleditor.data.HasOntologyChangeListener;
import vn.edu.uit.owleditor.data.OWLObjectContainer;
import vn.edu.uit.owleditor.event.OWLEditorEvent;
import vn.edu.uit.owleditor.event.OWLEditorEventBus;

import javax.annotation.Nonnull;
import java.util.Collections;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 11/6/14.
 */
public abstract class AbstractOWLObjectHierarchicalContainer
        extends HierarchicalContainer
        implements OWLObjectContainer, HasOntologyChangeListener {
    
    private final OWLEntityRemover entityRemover;
    private final OWLOntologyChangeVisitor changeVisitor;
    private final OWLAxiomVisitor nodeAdder;
    private final OWLAxiomVisitor nodeRemover;
    protected OWLOntology activeOntology;
    private Boolean reasonerStatus = false;

    public AbstractOWLObjectHierarchicalContainer(@Nonnull OWLOntology ontology) {
        activeOntology = ontology;
        entityRemover = new OWLEntityRemover(Collections.singleton(activeOntology));
        nodeAdder = initOWLAxiomAdder();
        nodeRemover = initOWLAxiomRemover();
        changeVisitor = new OWLOntologyChangeVisitorAdapter() {
            @Override
            public void visit(@Nonnull AddAxiom change) {
                change.getAxiom().accept(nodeAdder);
            }

            @Override
            public void visit(@Nonnull RemoveAxiom change) {
                change.getAxiom().accept(nodeRemover);
            }
        };
        OWLEditorEventBus.register(this);
    }
    
    @Subscribe
    public void safeToggleReasoner(OWLEditorEvent.ReasonerToggleEvent event) {
        reasonerStatus = event.getReasonerStatus();
    }

    protected abstract OWLAxiomVisitor initOWLAxiomAdder();

    protected abstract OWLAxiomVisitor initOWLAxiomRemover();





    public OWLEntityRemover getEntityRemover() {
        return entityRemover;
    }

    public OWLOntologyChangeVisitor getOWLOntologyChangeVisitor() {
        return changeVisitor;
    }

    @Override
    public OWLOntology getActiveOntology() {
        return activeOntology;
    }
    @Override
    public void setActiveOntology(OWLOntology ontology) {
        if (!activeOntology.getOntologyID().equals(ontology.getOntologyID())) {
            activeOntology = ontology;
        }
    }

    @Override
    public String sf(@Nonnull OWLEntity entity) {
        return OWLEditorKitImpl.getShortForm(entity);
    }


    @Override
    public OWLAxiomVisitor getOWLAxiomAdder() {
        return nodeAdder;
    }

    @Override
    public OWLAxiomVisitor getOWLAxiomRemover() {
        return nodeRemover;
    }
}
