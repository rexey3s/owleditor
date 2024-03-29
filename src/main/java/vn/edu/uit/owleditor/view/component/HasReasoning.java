package vn.edu.uit.owleditor.view.component;


import org.semanticweb.owlapi.reasoner.InconsistentOntologyException;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 1/25/15.
 */
public interface HasReasoning {
    public void addInferredExpressions() throws InconsistentOntologyException, NullPointerException;
}
