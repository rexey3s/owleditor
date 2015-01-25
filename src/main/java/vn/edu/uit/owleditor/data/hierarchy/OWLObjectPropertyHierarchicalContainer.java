package vn.edu.uit.owleditor.data.hierarchy;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLEntityVisitorAdapter;
import org.semanticweb.owlapi.util.OWLObjectVisitorAdapter;
import vn.edu.uit.owleditor.core.owlapi.OWLPropertyExpressionVisitorAdapter;
import vn.edu.uit.owleditor.utils.OWLEditorData;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 11/6/14.
 */
public class OWLObjectPropertyHierarchicalContainer extends AbstractOWLObjectHierarchicalContainer<OWLObjectProperty> {

    private final OWLObjectProperty topObjectProp = OWLManager.getOWLDataFactory().getOWLTopObjectProperty();

    @SuppressWarnings("unchecked")
    public OWLObjectPropertyHierarchicalContainer(@Nonnull OWLOntology ontology) {
        super(ontology);
        addContainerProperty(OWLEditorData.OWLObjectPropertyName, String.class, "Unknown");


        addItem(topObjectProp);
        getContainerProperty(topObjectProp, OWLEditorData.OWLObjectPropertyName).setValue("TopObjectProperty");
        getContainerProperty(topObjectProp, OWLEditorData.OWLObjectPropertyName).setReadOnly(true);
        setChildrenAllowed(topObjectProp, true);
        Set<OWLObjectProperty> allProperties = ontology.getObjectPropertiesInSignature();
        allProperties.remove(topObjectProp);
        allProperties.forEach(o ->  recursive(activeOntology, o, null));

    }


    @SuppressWarnings({"unchecked"})
    private void recursive(OWLOntology ontology, OWLObjectProperty child, OWLObjectProperty parent) {
        if(!containsId(child)) {
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

    @Override
    public OWLAxiomVisitor initNodeAdder() {
        return new OWLObjectVisitorAdapter() {

            @Override
            public void visit(OWLDeclarationAxiom axiom) {
                axiom.getEntity().accept(new OWLEntityVisitorAdapter() {
                    public void visit(OWLObjectProperty property) {
                        recursive(activeOntology, property, null);
                    }
                });
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
    }

    @Override
    public OWLAxiomVisitor initNodeRemover() {
        return new OWLObjectVisitorAdapter() {

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
    }
}
