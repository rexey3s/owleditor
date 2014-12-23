package vn.edu.uit.owleditor.data.hierarchy;


import com.vaadin.data.util.HierarchicalContainer;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedObjectVisitor;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChangeVisitor;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.data.HasOntologyChangeListener;
import vn.edu.uit.owleditor.data.OWLObjectContainer;

import javax.annotation.Nonnull;
import java.util.Collections;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/6/14.
 */
public abstract class AbstractOWLObjectHierarchicalContainer<T extends OWLEntity>
        extends HierarchicalContainer
        implements OWLObjectContainer, HasOntologyChangeListener {


    private final OWLEntityRemover entityRemover;

    private final OWLNamedObjectVisitor populationEngine;

    private final OWLOntologyChangeVisitor changeListener;

    protected OWLOntology activeOntology;

    public AbstractOWLObjectHierarchicalContainer(@Nonnull OWLOntology ontology) {
        activeOntology = ontology;
        entityRemover = new OWLEntityRemover(Collections.singleton(activeOntology));
        populationEngine = initPopulationEngine();
        changeListener = initChangeListener();
    }




    abstract OWLNamedObjectVisitor initPopulationEngine();



    public OWLEntityRemover getEntityRemover() {
        return entityRemover;
    }

    public OWLNamedObjectVisitor getPopulationEngine() {
        return populationEngine;
    }

    public OWLOntologyChangeVisitor getOWLOntologyChangeListener() {
        return changeListener;
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
        return OWLEditorKit.getShortForm(entity);
    }


}
