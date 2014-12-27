package vn.edu.uit.owleditor.data.hierarchy;

import com.vaadin.data.util.HierarchicalContainer;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLObjectVisitorAdapter;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/8/2014.
 */
public class OWLEntitiesHierarchicalContainer extends HierarchicalContainer {
    private final OWLEditorKitImpl editorKit;
    private final OWLNamedObjectVisitor populationEngine;
    private final OWLClass thing = OWLManager.getOWLDataFactory().getOWLThing();

    public OWLEntitiesHierarchicalContainer(@Nonnull OWLEditorKitImpl eKit) {
        editorKit = eKit;
        populationEngine = initPopulationEngine();
        addContainerProperty("Entities", String.class, "Unknown");
        addItem(thing);
        int size = EntitySearcher.getIndividuals(thing, editorKit.getActiveOntology()).size();
        getContainerProperty(thing, "Entities").setValue("Thing (" + size + ")");
        setChildrenAllowed(thing, true);
        editorKit.getActiveOntology().accept(populationEngine);
    }

    private OWLNamedObjectVisitor initPopulationEngine() {
        return new OWLObjectVisitorAdapter() {
            @Override
            public void visit(@Nonnull OWLOntology ontology) {
                ontology.getClassesInSignature()
                        .stream()
                        .filter(c -> !c.isOWLThing())
                        .forEach(c -> c.accept(this));
            }

            @Override
            public void visit(@Nonnull OWLClass owlClass) {
                if (!containsId(owlClass)) {
                    recursive(editorKit.getActiveOntology(), owlClass, null);
                }
            }
        };
    }

    private void recursive(OWLOntology ontology,
                           OWLClass child, OWLClass parent) {

        addItem(child);
        getContainerProperty(child, "Entities").setValue(sf(child));
        setChildrenAllowed(child, false);

        if (parent != null) {
            setChildrenAllowed(parent, true);
            setParent(child, parent);
        } else {
            setParent(child, thing);
        }
        for (OWLClassExpression ce : EntitySearcher
                .getSubClasses(child, ontology)) {

            for (OWLClass c : ce.getClassesInSignature()) {
                recursive(ontology, c, child);
            }
        }
    }

    public String sf(@Nonnull OWLEntity entity) {
        return OWLEditorKitImpl.getShortForm(entity);
    }


}
