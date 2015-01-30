package vn.edu.uit.owleditor.data.list;

import com.vaadin.data.util.IndexedContainer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;
import org.semanticweb.owlapi.util.OWLObjectVisitorAdapter;
import org.semanticweb.owlapi.util.OWLOntologyChangeVisitorAdapter;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;
import vn.edu.uit.owleditor.data.HasOntologyChangeListener;
import vn.edu.uit.owleditor.data.OWLObjectContainer;
import vn.edu.uit.owleditor.data.property.OWLNamedIndividualSource;
import vn.edu.uit.owleditor.utils.OWLEditorData;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 18/11/2014.
 */
public class OWLNamedIndividualContainer extends IndexedContainer implements
        OWLObjectContainer, HasOntologyChangeListener {

    private final OWLAxiomVisitor nodeAdder = new OWLAxiomVisitorAdapter() {
        @Override
        public void visit(@Nonnull OWLClassAssertionAxiom axiom) {
            if (axiom.getIndividual().isNamed()) {
                addItem(axiom.getIndividual().asOWLNamedIndividual());
                getContainerProperty(axiom.getIndividual().asOWLNamedIndividual(),
                        OWLEditorData.OWLNamedIndividualName).setValue(
                        OWLEditorKitImpl.getShortForm(axiom.getIndividual().asOWLNamedIndividual())
                );
            }
        }
    };
    private final OWLAxiomVisitor nodeRemover = new OWLAxiomVisitorAdapter() {
        @Override
        public void visit(@Nonnull OWLClassAssertionAxiom axiom) {
            removeItem(axiom.getIndividual());
        }
    };
    private final OWLOntologyChangeVisitor changeVisitor = new OWLOntologyChangeVisitorAdapter() {
        @Override
        public void visit(@Nonnull AddAxiom change) {
            change.getAxiom().accept(nodeAdder);
        }

        @Override
        public void visit(@Nonnull RemoveAxiom change) {
            change.getAxiom().accept(nodeRemover);
        }
    };
    private OWLOntology activeOntology;
    private OWLNamedIndividualSource dataSource = new OWLNamedIndividualSource();

    public OWLNamedIndividualContainer(@Nonnull OWLOntology ontology) {
        activeOntology = ontology;
        addContainerProperty(OWLEditorData.OWLNamedIndividualName, String.class, "UnknownNamedIndividual");

        activeOntology.accept(new OWLObjectVisitorAdapter() {

            @Override
            public void visit(@Nonnull OWLNamedIndividual owlIndividual) {
                addItem(owlIndividual);
                getContainerProperty(owlIndividual, OWLEditorData.OWLNamedIndividualName).setValue(sf(owlIndividual));
            }

            @Override
            public void visit(@Nonnull OWLOntology ontology) {
                for (OWLNamedIndividual i : ontology.getIndividualsInSignature()) {
                    i.accept(this);
                }
            }
        });

    }

    public OWLNamedIndividualContainer(@Nonnull OWLOntology ontology, @Nonnull OWLClass owlClass) {
        activeOntology = ontology;
        addContainerProperty(OWLEditorData.OWLNamedIndividualName, String.class, "UnknownNamedIndividual");

//        manager.addOntologyChangeListener(changes -> changes.forEach(change -> change.accept(changeListener)));
        EntitySearcher.getIndividuals(owlClass, activeOntology).forEach(ind -> {
            if (ind.isNamed()) {
                addItem(ind.asOWLNamedIndividual());
                getContainerProperty(ind.asOWLNamedIndividual(), OWLEditorData.OWLNamedIndividualName).setValue(sf(ind.asOWLNamedIndividual()));
            }
        });
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

    public OWLOntologyChangeVisitor getOWLOntologyChangeVisitor() {
        return changeVisitor;
    }
}
