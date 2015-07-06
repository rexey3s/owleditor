package vn.edu.uit.owleditor.core.swrlapi;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;
import org.swrlapi.core.SWRLAPIOWLOntology;
import vn.edu.uit.owleditor.core.OWLEditorKit;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 12/14/14.
 */
public class SWRLAtomSearchByDefinedClass {

    private final OWLEditorKit editorKit;
    private final SWRLAPIOWLOntology ontology;

    private Collection<OWLClassExpression> knownDomains;


    public SWRLAtomSearchByDefinedClass(OWLEditorKit eKit) {
        editorKit = eKit;
        ontology = editorKit.getSWRLActiveOntology();
    }

    public Set<OWLObjectPropertyExpression> getObjectPropertiesByDefinedClass(OWLClass owlClass) {
        final Set<OWLObjectPropertyExpression> retOEs = new HashSet<>();
        ontology.getSWRLRules()
                .stream().filter(rule -> !rule.isSQWRLQuery())
                .forEach(rule -> {
                    for (SWRLAtom atom : rule.getBodyAtoms()) {
                        if (atom.getClassesInSignature().contains(owlClass)) {
                            retOEs.addAll(rule.getObjectPropertiesInSignature());
                            break;
                        }
                    }
                });
        return retOEs;

    }

    public Set<OWLDataPropertyExpression> getDataPropertiesByDefinedClass(OWLClass owlClass) {
        final Set<OWLDataPropertyExpression> retDEs = new HashSet<>();
        ontology.getSWRLRules()
                .stream().filter(rule -> !rule.isSQWRLQuery())
                .forEach(rule -> {
                    for (SWRLAtom atom : rule.getBodyAtoms()) {
                        if (atom.getClassesInSignature().contains(owlClass)) {
                            retDEs.addAll(rule.getDataPropertiesInSignature());
                            break;
                        }
                    }
                });

        return retDEs;
    }


    private OWLClassExpression intersectDomainExpression(Collection<OWLClassExpression> domains) {
        final Set<OWLClassExpression> intersectExpressions = new HashSet<>();
        final OWLObjectIntersectionOf owlObjectIntersectionOf;
        domains.forEach(ce -> ce.accept(new OWLClassExpressionVisitorAdapter() {
            @Override
            public void visit(OWLClass ce) {
                super.visit(ce);
            }

            @Override
            public void visit(OWLObjectIntersectionOf ce) {
                super.visit(ce);
            }

            @Override
            public void visit(OWLObjectUnionOf ce) {
                super.visit(ce);
            }

            @Override
            public void visit(OWLObjectComplementOf ce) {
                super.visit(ce);
            }
        }));
        return null;
    }

}
