package vn.edu.uit.owleditor.data.list;

import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.data.HasOntologyChangeListener;
import vn.edu.uit.owleditor.data.OWLObjectContainer;
import vn.edu.uit.owleditor.data.property.OWLNamedIndividualSource;
import vn.edu.uit.owleditor.utils.OWLEditorData;
import com.vaadin.data.util.IndexedContainer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;
import org.semanticweb.owlapi.util.OWLObjectVisitorAdapter;
import org.semanticweb.owlapi.util.OWLOntologyChangeVisitorAdapter;

import javax.annotation.Nonnull;

/**
 * Created by Chuong Dang on 18/11/2014.
 */
public class OWLNamedIndividualContainer extends IndexedContainer implements
        OWLObjectContainer, HasOntologyChangeListener {

    private final OWLOntologyChangeVisitor changeListener;

    private OWLOntology activeOntology;

    private OWLNamedIndividualSource dataSource = new OWLNamedIndividualSource();

    public OWLNamedIndividualContainer(@Nonnull OWLOntology ontology) {
        activeOntology = ontology;
        addContainerProperty(OWLEditorData.OWLNamedIndividualName, String.class, "UnknownNamedIndividual");
        changeListener = initChangeListener();
        activeOntology.accept(new OWLObjectVisitorAdapter() {

            @Override
            public void visit(@Nonnull OWLNamedIndividual owlIndividual) {
                addItem(owlIndividual);
                getItem(owlIndividual).getItemProperty(OWLEditorData.OWLNamedIndividualName).setValue(sf(owlIndividual));
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
        changeListener = initChangeListener();
        final OWLOntologyManager manager = ontology.getOWLOntologyManager();
        manager.addOntologyChangeListener(changes -> changes.forEach(change -> change.accept(changeListener)));
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
        return OWLEditorKit.getShortForm(entity);
    }


    @Override
    public OWLOntologyChangeVisitor initChangeListener() {
        return new OWLOntologyChangeVisitorAdapter() {
            @Override
            public void visit(@Nonnull AddAxiom change) {
                change.getAxiom().accept(new OWLAxiomVisitorAdapter() {
                    @Override
                    public void visit(@Nonnull OWLClassAssertionAxiom axiom) {
                        if (axiom.getIndividual().isNamed()) {
                            addItem(axiom.getIndividual().asOWLNamedIndividual());
                            getContainerProperty(axiom.getIndividual().asOWLNamedIndividual(),
                                    OWLEditorData.OWLNamedIndividualName).setValue(
                                    OWLEditorKit.getShortForm(axiom.getIndividual().asOWLNamedIndividual())
                            );
                        }
                    }
                });
            }

            @Override
            public void visit(@Nonnull RemoveAxiom change) {
                change.getAxiom().accept(new OWLAxiomVisitorAdapter() {
                    @Override
                    public void visit(@Nonnull OWLClassAssertionAxiom axiom) {
                        removeItem(axiom.getIndividual());
                    }
                });
            }
        };
    }
}
