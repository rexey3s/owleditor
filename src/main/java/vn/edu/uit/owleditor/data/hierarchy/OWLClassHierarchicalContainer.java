package vn.edu.uit.owleditor.data.hierarchy;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;
import org.semanticweb.owlapi.util.OWLEntityVisitorAdapter;
import vn.edu.uit.owleditor.utils.OWLEditorData;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 11/6/14.
 */
public class OWLClassHierarchicalContainer extends AbstractOWLObjectHierarchicalContainer<OWLClass> {
    private final OWLClass thing = OWLManager.getOWLDataFactory().getOWLThing();

    @SuppressWarnings("unchecked")
    public OWLClassHierarchicalContainer(@Nonnull OWLOntology ontology) {
        super(ontology);
        addContainerProperty(OWLEditorData.OWLClassName, String.class, "");
        
        addItem(thing);
        getItem(thing).getItemProperty(OWLEditorData.OWLClassName).setValue("Thing");
        setChildrenAllowed(thing, true);
        
        Set<OWLClass> allClasses = ontology.getClassesInSignature();
        allClasses.remove(thing);
        allClasses.forEach(c ->  recursive(activeOntology, c, null));
    }
    
    private void recursive(OWLOntology ontology, OWLClass child, OWLClass parent) {
        if(!containsId(child)) {
            addItem(child);
            getContainerProperty(child, OWLEditorData.OWLClassName).setValue(sf(child));
            setChildrenAllowed(child, false);

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
    }


    @Override
    public OWLAxiomVisitor initNodeAdder() {
        return new OWLAxiomVisitorAdapter() {
            @Override
            public void visit(@Nonnull OWLDeclarationAxiom axiom) {
               axiom.getEntity().accept(new OWLEntityVisitorAdapter() {
                   public void visit(OWLClass cls) {
                       recursive(activeOntology, cls, null);
                   }
               });


            }

            @Override
            public void visit(@Nonnull OWLSubClassOfAxiom axiom) {
                if (!axiom.getSubClass().isAnonymous() && !axiom.getSuperClass().isAnonymous()) {
                    OWLClass subCls = axiom.getSubClass().asOWLClass();
                    OWLClass supCls = axiom.getSuperClass().asOWLClass();
                    getContainerProperty(subCls, OWLEditorData.OWLClassName).setValue(sf(subCls));
                    setChildrenAllowed(subCls, false);
                    setChildrenAllowed(supCls, true);
                    setParent(subCls, supCls);
                }
            }
        };

    }

    @Override
    public OWLAxiomVisitor initNodeRemover() {
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
                        if (EntitySearcher.getSubClasses(supCls,
                                activeOntology).size() == 0) {
                            setChildrenAllowed(supCls, false);
                        }
                    }
                }
            }
        };
    }
}
