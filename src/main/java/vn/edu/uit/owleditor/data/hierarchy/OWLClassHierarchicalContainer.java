package vn.edu.uit.owleditor.data.hierarchy;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;
import org.semanticweb.owlapi.util.OWLObjectVisitorAdapter;
import org.semanticweb.owlapi.util.OWLOntologyChangeVisitorAdapter;
import vn.edu.uit.owleditor.utils.OWLEditorData;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 11/6/14.
 */
public class OWLClassHierarchicalContainer extends AbstractOWLObjectHierarchicalContainer<OWLClass> {
    private final OWLClass thing = OWLManager.getOWLDataFactory().getOWLThing();
    private final OWLAxiomVisitor nodeRemover = new OWLAxiomVisitorAdapter() {

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

                if (EntitySearcher.getSubClasses(supCls,
                        activeOntology).size() == 0) {
                    setChildrenAllowed(supCls, false);
                }
            }
        }

    };
    private final OWLAxiomVisitor nodeAdder = new OWLAxiomVisitorAdapter() {

        @Override
        public void visit(@Nonnull OWLDeclarationAxiom axiom) {
            axiom.getEntity().accept(getPopulationEngine());

        }

        @Override
        public void visit(@Nonnull OWLSubClassOfAxiom axiom) {
            if (!axiom.getSubClass().isAnonymous() && !axiom.getSuperClass().isAnonymous()) {
                OWLClass subCls = axiom.getSubClass().asOWLClass();
                OWLClass supCls = axiom.getSuperClass().asOWLClass();
                getItem(subCls)
                        .getItemProperty(OWLEditorData.OWLClassName)
                        .setValue(sf(subCls));
                    setChildrenAllowed(subCls, false);
                    setChildrenAllowed(supCls, true);


                setParent(subCls, supCls);

            }
        }
    };


    @SuppressWarnings("unchecked")
    public OWLClassHierarchicalContainer(@Nonnull OWLOntology ontology) {
        super(ontology);
        addContainerProperty(OWLEditorData.OWLClassName, String.class, "Unknown");
        addItem(thing);
        getItem(thing).getItemProperty(OWLEditorData.OWLClassName).setValue("Thing");
        setChildrenAllowed(thing, true);
        activeOntology.accept(getPopulationEngine());
    }


    @Override
    protected OWLObjectVisitorAdapter initPopulationEngine() {
        return new OWLObjectVisitorAdapter() {
            @Override
            public void visit(@Nonnull OWLOntology ontology) {
                ontology.getClassesInSignature()
                        .stream().filter(c -> !c.isOWLThing())
                        .forEach(c -> c.accept(this));
            }

            @Override
            public void visit(@Nonnull OWLClass owlClass) {
                if (!containsId(owlClass)) {
                    recursive(activeOntology, owlClass, null);
                }
            }
        };
    }

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
    private void recursive(OWLOntology ontology, OWLClass child, OWLClass parent) {
        addItem(child);
        getItem(child).getItemProperty(OWLEditorData.OWLClassName).setValue(sf(child));
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
