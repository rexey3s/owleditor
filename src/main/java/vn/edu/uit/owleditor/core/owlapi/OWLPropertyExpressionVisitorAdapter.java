package vn.edu.uit.owleditor.core.owlapi;

import org.semanticweb.owlapi.model.*;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/19/14.
 */
public class OWLPropertyExpressionVisitorAdapter implements OWLPropertyExpressionVisitor {
    @SuppressWarnings({"unused"})
    protected void handleDefault(
            @SuppressWarnings("unused") OWLPropertyExpression pe) {}

    @Override

    public void visit(OWLObjectProperty property) {
        handleDefault(property);
    }

    @Override
    public void visit(OWLObjectInverseOf property) {
        handleDefault(property);
    }

    @Override
    public void visit(OWLDataProperty property) {
        handleDefault(property);
    }

    @Override
    public void visit(OWLAnnotationProperty property) {
        handleDefault(property);
    }
}
