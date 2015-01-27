package vn.edu.uit.owleditor.data;


import org.semanticweb.owlapi.model.OWLAxiomVisitor;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 12/8/2014.
 */
public interface HasOntologyChangeListener {
    OWLAxiomVisitor getOWLAxiomAdder();

    OWLAxiomVisitor getOWLAxiomRemover();
}
