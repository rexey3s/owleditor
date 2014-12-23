package vn.edu.uit.owleditor.data;

import org.semanticweb.owlapi.model.OWLOntologyChangeVisitor;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/8/2014.
 */
public interface HasOntologyChangeListener {
    public OWLOntologyChangeVisitor initChangeListener();
}
