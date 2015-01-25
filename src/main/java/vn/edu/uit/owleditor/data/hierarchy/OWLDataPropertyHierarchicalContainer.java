package vn.edu.uit.owleditor.data.hierarchy;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.ChangeApplied;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;
import org.semanticweb.owlapi.util.OWLEntityVisitorAdapter;
import vn.edu.uit.owleditor.core.owlapi.OWLPropertyExpressionVisitorAdapter;
import vn.edu.uit.owleditor.utils.OWLEditorData;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 11/22/2014.
 */
@SuppressWarnings({"unchecked"})
public class OWLDataPropertyHierarchicalContainer extends AbstractOWLObjectHierarchicalContainer {

    private final OWLDataProperty topDataProp = OWLManager.getOWLDataFactory().getOWLTopDataProperty();


    private final OWLDataFactory factory = OWLManager.getOWLDataFactory();
    private final OWLOntologyManager manager;

    public OWLDataPropertyHierarchicalContainer(@Nonnull OWLOntology ontology) {
        super(ontology);
        manager = ontology.getOWLOntologyManager();

        addContainerProperty(OWLEditorData.OWLDataPropertyName, String.class, "Unknown");
        addContainerProperty(OWLEditorData.OWLFunctionalProperty, Boolean.class, null);
        addContainerProperty(OWLEditorData.OWLEntityIcon, Resource.class, null);

        addItem(topDataProp);
        getContainerProperty(topDataProp, OWLEditorData.OWLDataPropertyName).setValue("TopDataProperty");

        getContainerProperty(topDataProp, OWLEditorData.OWLFunctionalProperty)
                .setValue(containedInOntology(factory.getOWLFunctionalDataPropertyAxiom(topDataProp)));
        checkFunctionalIcon(topDataProp);
        setChildrenAllowed(topDataProp, true);
        Set<OWLDataProperty> allProperties = ontology.getDataPropertiesInSignature();
        allProperties.remove(topDataProp);
        allProperties.forEach(d ->  recursive(activeOntology, d, null));
        
    }



    @SuppressWarnings("unchecked")
    private void recursive(OWLOntology ontology, OWLDataProperty child, OWLDataProperty parent) {
        if(!containsId(child)) {
            addItem(child);
            getContainerProperty(child, OWLEditorData.OWLDataPropertyName).setValue(sf(child));
            getContainerProperty(child, OWLEditorData.OWLFunctionalProperty)
                    .setValue(containedInOntology(factory.getOWLFunctionalDataPropertyAxiom(child)));
            checkFunctionalIcon(child);

            setChildrenAllowed(child, false);

            if (parent != null) {
                setChildrenAllowed(parent, true);
                setParent(child, parent);
            } else {
                setParent(child, topDataProp);
            }
            EntitySearcher.getSubProperties(child, ontology).forEach(
                    pe -> pe.accept(new OWLPropertyExpressionVisitorAdapter() {
                        public void visit(OWLDataProperty property) {
                            recursive(ontology, property, child);
                        }
                    })
            );
        }
    }

    public Boolean containedInOntology(OWLAxiom axiom) {
        return activeOntology.containsAxiom(axiom);
    }

    public void checkFunctionalIcon(OWLDataProperty objectId) {
        Boolean checked = (Boolean) getContainerProperty(objectId, OWLEditorData.OWLFunctionalProperty).getValue();
        if (checked) {
            getContainerProperty(objectId, OWLEditorData.OWLEntityIcon).setValue(FontAwesome.CHAIN);
        } else {
            getContainerProperty(objectId, OWLEditorData.OWLEntityIcon).setValue(null);
        }
        
    }

    public void toggle(Boolean isFunctional, OWLAxiom axiom) {
        if (isFunctional) {
            ChangeApplied applied = manager.addAxiom(activeOntology, axiom);

        } else {
            List<OWLOntologyChange> changes = manager.removeAxiom(activeOntology, axiom);
            manager.applyChanges(changes);
            
        }
    }

    @Override
    public OWLAxiomVisitor initNodeAdder() {
        return new OWLAxiomVisitorAdapter() {
            @Override
            public void visit(@Nonnull OWLDeclarationAxiom axiom) {
                axiom.getEntity().accept(new OWLEntityVisitorAdapter() {
                    public void visit(OWLDataProperty property) {
                        recursive(activeOntology, property, null);
                    }
                });
            }

            @Override
            public void visit(@Nonnull OWLSubDataPropertyOfAxiom axiom) {
                if (!axiom.getSubProperty().isAnonymous() && !axiom.getSuperProperty().isAnonymous()) {
                    OWLDataProperty subProp = axiom.getSubProperty().asOWLDataProperty();
                    OWLDataProperty supProp = axiom.getSuperProperty().asOWLDataProperty();

                    getItem(subProp)
                            .getItemProperty(OWLEditorData.OWLDataPropertyName)
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
        return  new OWLAxiomVisitorAdapter() {
            @Override
            public void visit(@Nonnull OWLDeclarationAxiom axiom) {
                removeItem(axiom.getEntity());
            }

            @Override
            public void visit(@Nonnull OWLSubDataPropertyOfAxiom axiom) {
                if (!axiom.getSubProperty().isAnonymous() && !axiom.getSuperProperty().isAnonymous()) {
                    OWLDataProperty supProp = axiom.getSuperProperty().asOWLDataProperty();
                    OWLDataProperty subProp = axiom.getSubProperty().asOWLDataProperty();

                    setParent(subProp, topDataProp);

                    if (EntitySearcher.getSubProperties(supProp, activeOntology).size() == 0) {
                        setChildrenAllowed(supProp, false);
                    }
                }
            }
        };
    }
}
