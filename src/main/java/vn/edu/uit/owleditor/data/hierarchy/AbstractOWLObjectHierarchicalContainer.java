package vn.edu.uit.owleditor.data.hierarchy;


import com.vaadin.data.util.HierarchicalContainer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.semanticweb.owlapi.util.OWLOntologyChangeVisitorAdapter;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;
import vn.edu.uit.owleditor.data.HasOntologyChangeListener;
import vn.edu.uit.owleditor.data.OWLObjectContainer;

import javax.annotation.Nonnull;
import java.util.Collections;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 11/6/14.
 */
public abstract class AbstractOWLObjectHierarchicalContainer<T extends OWLEntity>
        extends HierarchicalContainer
        implements OWLObjectContainer, HasOntologyChangeListener {


    private final OWLEntityRemover entityRemover;

    private final OWLOntologyChangeVisitor changeListener ;
    
    private final OWLAxiomVisitor nodeAdder;
    
    private final OWLAxiomVisitor nodeRemover;
    
    protected OWLOntology activeOntology;

    public AbstractOWLObjectHierarchicalContainer(@Nonnull OWLOntology ontology) {
        activeOntology = ontology;
        entityRemover = new OWLEntityRemover(Collections.singleton(activeOntology));
        nodeAdder = initNodeAdder();
        nodeRemover = initNodeRemover();
        changeListener = new OWLOntologyChangeVisitorAdapter() {
            @Override
            public void visit(@Nonnull AddAxiom change) {
                change.getAxiom().accept(nodeAdder);
            }

            @Override
            public void visit(@Nonnull RemoveAxiom change) {
                change.getAxiom().accept(nodeRemover);
            }
        };
    }






    public OWLEntityRemover getEntityRemover() {
        return entityRemover;
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
        return OWLEditorKitImpl.getShortForm(entity);
    }


}
