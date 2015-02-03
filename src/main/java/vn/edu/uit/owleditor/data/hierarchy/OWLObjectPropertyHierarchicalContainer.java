package vn.edu.uit.owleditor.data.hierarchy;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.semanticweb.owlapi.util.OWLEntityVisitorAdapter;
import org.semanticweb.owlapi.util.OWLObjectVisitorAdapter;
import org.vaadin.spring.annotation.VaadinComponent;
import vn.edu.uit.owleditor.core.owlapi.OWLPropertyExpressionVisitorAdapter;
import vn.edu.uit.owleditor.utils.OWLEditorData;
import vn.edu.uit.owleditor.utils.exception.OWLEditorException;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Set;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 11/6/14.
 */
@VaadinComponent
public class OWLObjectPropertyHierarchicalContainer extends AbstractOWLObjectHierarchicalContainer {

    private final OWLObjectProperty topObjectProp = OWLManager.getOWLDataFactory().getOWLTopObjectProperty();

    public OWLObjectPropertyHierarchicalContainer() {
        super();
    }


    @PostConstruct
    private void initialise() {
        addContainerProperty(OWLEditorData.OWLObjectPropertyName, String.class, "Unknown");
        addTopObjectProperty();
    }

    private void addTopObjectProperty() {
        if (!containsId(topObjectProp)) {
            addItem(topObjectProp);
            getContainerProperty(topObjectProp, OWLEditorData.OWLObjectPropertyName).setValue("TopObjectProperty");
            getContainerProperty(topObjectProp, OWLEditorData.OWLObjectPropertyName).setReadOnly(true);
            setChildrenAllowed(topObjectProp, true);

        }

    }

    @Override
    public void setActiveOntology(@Nonnull OWLOntology ontology) throws OWLEditorException.DuplicatedActiveOntologyException {
        if (!ontology.equals(activeOntology)) {
            removeItemRecursively(topObjectProp);
            addTopObjectProperty();

            activeOntology = ontology;
            entityRemover = new OWLEntityRemover(Collections.singleton(activeOntology));
            Set<OWLObjectProperty> allProperties = activeOntology.getObjectPropertiesInSignature();
            allProperties.remove(topObjectProp);
            allProperties.forEach(o -> {
                if (!containsId(o)) {
                    recursive(activeOntology, o, null);
                }
            });
        } else
            throw new OWLEditorException.DuplicatedActiveOntologyException("Duplicated active ontology");
    }

    @SuppressWarnings({"unchecked"})
    private void recursive(OWLOntology ontology, OWLObjectProperty child, OWLObjectProperty parent) {
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

    private void recursive2(OWLOntology ontology, OWLObjectProperty child, OWLObjectProperty parent) {
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


    @Override
    public OWLAxiomVisitor initOWLAxiomAdder() {
        return new OWLObjectVisitorAdapter() {

            @Override
            public void visit(OWLDeclarationAxiom axiom) {
                axiom.getEntity().accept(new OWLEntityVisitorAdapter() {
                    public void visit(OWLObjectProperty property) {
//                        recursive(activeOntology, property, null);
                        addItem(property);
                        getContainerProperty(property, OWLEditorData.OWLObjectPropertyName).setValue(sf(property));
                        setParent(property, topObjectProp);
                    }
                });
            }

            @Override
            public void visit(OWLSubObjectPropertyOfAxiom axiom) {
                if (!axiom.getSuperProperty().isAnonymous() && !axiom.getSubProperty().isAnonymous()) {
                    OWLObjectProperty supProp = axiom.getSuperProperty().asOWLObjectProperty();

                    OWLObjectProperty subProp = axiom.getSubProperty().asOWLObjectProperty();

                    recursive2(activeOntology, subProp, supProp);
                }
            }
        };
    }

    @Override
    public OWLAxiomVisitor initOWLAxiomRemover() {
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
                    if(!subProp.isOWLTopObjectProperty()) {
                        if (EntitySearcher.getSubProperties(supProp,
                                activeOntology).size() == 0) {

                            setChildrenAllowed(supProp, false);
                        }
                    }
                }
            }
        };
    }


}
