package vn.edu.uit.owleditor.data.hierarchy;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLObjectVisitorAdapter;
import org.semanticweb.owlapi.util.OWLOntologyChangeVisitorAdapter;
import vn.edu.uit.owleditor.core.owlapi.OWLPropertyExpressionVisitorAdapter;
import vn.edu.uit.owleditor.utils.OWLEditorData;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/6/14.
 */
public class OWLObjectPropertyHierarchicalContainer extends AbstractOWLObjectHierarchicalContainer<OWLObjectProperty> {

    private final OWLObjectProperty topObjectProp = OWLManager.getOWLDataFactory().getOWLTopObjectProperty();
    private final OWLAxiomVisitor nodeAdder = new OWLObjectVisitorAdapter() {

        @Override
        public void visit(OWLDeclarationAxiom axiom) {
            axiom.getEntity()
                    .accept(getPopulationEngine());
        }

        @Override
        public void visit(OWLSubObjectPropertyOfAxiom axiom) {
            if (!axiom.getSuperProperty().isAnonymous() && !axiom.getSubProperty().isAnonymous()) {
                OWLObjectProperty supProp = axiom.getSuperProperty().asOWLObjectProperty();

                OWLObjectProperty subProp = axiom.getSubProperty().asOWLObjectProperty();

                getContainerProperty(subProp, OWLEditorData.OWLObjectPropertyName)
                        .setValue(sf(subProp));

                setChildrenAllowed(subProp, false);
                setChildrenAllowed(supProp, true);
                setParent(subProp, supProp);
            }
        }
    };
    private final OWLAxiomVisitor nodeRemover = new OWLObjectVisitorAdapter() {

        @Override
        public void visit(OWLDeclarationAxiom axiom) {
            removeItem(axiom.getEntity());
        }

        @Override
        public void visit(OWLSubObjectPropertyOfAxiom axiom) {
            if (!axiom.getSubProperty().isAnonymous() && !axiom.getSuperProperty().isAnonymous()) {
                OWLObjectProperty supProp = axiom
                        .getSuperProperty()
                        .asOWLObjectProperty();

                OWLObjectProperty subProp = axiom
                        .getSubProperty()
                        .asOWLObjectProperty();

                setParent(subProp, topObjectProp);

                if (EntitySearcher.getSubProperties(supProp,
                        activeOntology).size() == 0) {

                    setChildrenAllowed(supProp, false);
                }
            }
        }
    };
    @SuppressWarnings("unchecked")
    public OWLObjectPropertyHierarchicalContainer(@Nonnull OWLOntology ontology) {
        super(ontology);
        addContainerProperty(OWLEditorData.OWLObjectPropertyName,
                String.class, "Unknown");


        addItem(topObjectProp);
        getContainerProperty(topObjectProp, OWLEditorData.OWLObjectPropertyName).setValue("TopObjectProperty");
        getContainerProperty(topObjectProp, OWLEditorData.OWLObjectPropertyName).setReadOnly(true);
        activeOntology.accept(getPopulationEngine());


    }

    @Override
    OWLNamedObjectVisitor initPopulationEngine() {
        return new OWLObjectVisitorAdapter() {
            @Override
            public void visit(@Nonnull OWLOntology ontology) {
                ontology.getObjectPropertiesInSignature().stream().filter(obj -> !obj.isOWLTopObjectProperty())
                        .forEach(obj -> obj.accept(this));
            }

            @Override
            public void visit(@Nonnull OWLObjectProperty owlObjectProperty) {
                if (!containsId(owlObjectProperty)) {
                    recursive(activeOntology, owlObjectProperty, null);
                }
            }
        };
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public OWLOntologyChangeVisitor initChangeListener() {
        return new OWLOntologyChangeVisitorAdapter() {

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

    @SuppressWarnings({"unchecked"})
    private void recursive(OWLOntology ontology,
                           OWLObjectProperty child, OWLObjectProperty parent) {

        addItem(child);
        getContainerProperty(child, OWLEditorData.OWLObjectPropertyName).setValue(sf(child));
        getContainerProperty(child, OWLEditorData.OWLObjectPropertyName).setReadOnly(true);
        setChildrenAllowed(child, false);

        if (parent != null) {
            setChildrenAllowed(parent, true);
            setParent(child, parent);
        } else {
            setParent(child, topObjectProp);
        }

        EntitySearcher.getSubProperties(child, ontology).forEach(
                pe -> pe.accept(new OWLPropertyExpressionVisitorAdapter() {
                    public void visit(OWLObjectProperty property) {
                        recursive(ontology, property, child);
                    }
                })
        );
    }
}
