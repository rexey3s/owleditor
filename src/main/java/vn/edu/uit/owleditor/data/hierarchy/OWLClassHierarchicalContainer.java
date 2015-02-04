package vn.edu.uit.owleditor.data.hierarchy;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.semanticweb.owlapi.util.OWLEntityVisitorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.vaadin.spring.annotation.VaadinSessionScope;
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
@Component
@VaadinSessionScope
public class OWLClassHierarchicalContainer extends AbstractOWLObjectHierarchicalContainer {
    private static final Logger LOG = LoggerFactory.getLogger(OWLClassHierarchicalContainer.class);
    private final OWLClass thing = OWLManager.getOWLDataFactory().getOWLThing();

    public OWLClassHierarchicalContainer() {
        super();
    }

    @PostConstruct
    private void initialise() {
        addContainerProperty(OWLEditorData.OWLClassName, String.class, "");
        addThing();
    }

    private void addThing() {
        if (!containsId(thing)) {
            addItem(thing);
            getItem(thing).getItemProperty(OWLEditorData.OWLClassName).setValue("Thing");
            setChildrenAllowed(thing, true);
        }
    }

    public void setActiveOntology(@Nonnull OWLOntology ontology) throws OWLEditorException.DuplicatedActiveOntologyException {
        removeItemRecursively(thing);
        addThing();

        activeOntology = ontology;
        entityRemover = new OWLEntityRemover(Collections.singleton(activeOntology));
        Set<OWLClass> allClasses = activeOntology.getClassesInSignature();
        allClasses.remove(thing);
        allClasses.forEach(c -> {
            if (!containsId(c)) {
                    recursive(activeOntology, c, null);
            }
        });
    }
    
    private void recursive(OWLOntology ontology, OWLClass child, OWLClass parent) {
        addItem(child);
        getContainerProperty(child, OWLEditorData.OWLClassName).setValue(sf(child));
        setChildrenAllowed(child, false);
        LOG.info(sf(child));
        if (parent != null) {
            setChildrenAllowed(parent, true);
            setParent(child, parent);
        } else {
            setParent(child, thing);
        }

        EntitySearcher.getSubClasses(child, ontology).forEach(
                ce -> ce.accept(new OWLClassExpressionVisitorAdapter() {
                        public void visit(OWLClass owlClass) {
                            recursive(ontology, owlClass, child);
                        }
                })
        );

    }

    private void rearrange(OWLClass child, OWLClass parent) {
        setChildrenAllowed(child, false);
        if (parent != null) {
            setChildrenAllowed(parent, true);
            setParent(child, parent);
        } else {
            setParent(child, thing);
        }

        EntitySearcher.getSubClasses(child, activeOntology).forEach(
                ce -> ce.accept(new OWLClassExpressionVisitorAdapter() {
                    public void visit(OWLClass owlClass) {
                        rearrange(owlClass, child);
                    }
                })
        );

    }

    @Override
    public OWLAxiomVisitor initOWLAxiomAdder() {
        return new OWLAxiomVisitorAdapter() {
            @Override
            public void visit(@Nonnull OWLDeclarationAxiom axiom) {
               axiom.getEntity().accept(new OWLEntityVisitorAdapter() {
                   public void visit(OWLClass cls) {
                       addItem(cls);
                       getContainerProperty(cls, OWLEditorData.OWLClassName).setValue(sf(cls));
                       setParent(cls, thing);
                   }
               });

            }

            @Override
            public void visit(@Nonnull OWLSubClassOfAxiom axiom) {
                if (!axiom.getSubClass().isAnonymous() && !axiom.getSuperClass().isAnonymous()) {
                    OWLClass subCls = axiom.getSubClass().asOWLClass();
                    OWLClass supCls = axiom.getSuperClass().asOWLClass();
//                    getContainerProperty(subCls, OWLEditorData.OWLClassName).setValue(sf(subCls));
                    
//                    setChildrenAllowed(supCls, true);
//                    setChildrenAllowed(subCls, false);
//                    setParent(subCls, supCls);
                    rearrange(subCls, supCls);
                }
            }
        };

    }

    @Override
    public OWLAxiomVisitor initOWLAxiomRemover() {
        return new OWLAxiomVisitorAdapter() {
            @Override
            public void visit(@Nonnull OWLDeclarationAxiom axiom) {
                removeItem(axiom.getEntity());
            }

            @Override
            public void visit(@Nonnull OWLSubClassOfAxiom axiom) {
                if (!axiom.getSubClass().isAnonymous() && !axiom.getSuperClass().isAnonymous()) {
                    OWLClass supCls = axiom.getSuperClass().asOWLClass();
                    OWLClass subCls = axiom.getSubClass().asOWLClass();

                    setParent(subCls, thing);
                    if(!supCls.isOWLThing()) {
                        if (EntitySearcher.getSubClasses(supCls, activeOntology).size() == 0) {
                            setChildrenAllowed(supCls, false);
                        }
                    }
                }
            }
        };
    }
}
