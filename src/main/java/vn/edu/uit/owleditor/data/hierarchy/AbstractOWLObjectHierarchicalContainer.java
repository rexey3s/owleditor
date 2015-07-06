package vn.edu.uit.owleditor.data.hierarchy;


import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.spring.annotation.SpringComponent;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.semanticweb.owlapi.util.OWLOntologyChangeVisitorAdapter;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;
import vn.edu.uit.owleditor.data.HasOntologyChangeListener;
import vn.edu.uit.owleditor.data.OWLObjectContainer;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 11/6/14.
 */
@SpringComponent
public abstract class AbstractOWLObjectHierarchicalContainer extends HierarchicalContainer
        implements OWLObjectContainer, HasOntologyChangeListener {
    
    private final OWLOntologyChangeVisitor changeVisitor;
    private final OWLAxiomVisitor nodeAdder;
    private final OWLAxiomVisitor nodeRemover;
    protected OWLEntityRemover entityRemover;
    protected OWLOntology activeOntology;

    public AbstractOWLObjectHierarchicalContainer() {
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
