package vn.edu.uit.owleditor.core.owlapi;

import org.semanticweb.owlapi.model.*;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 12/19/14.
 */
public class OWLDataRangeVisitorAdapter implements OWLDataRangeVisitor {
    @SuppressWarnings({"unused"})
    protected void handleDefault(
            @SuppressWarnings("unused") OWLDataRange pe) {}

    @Override
    public void visit(OWLDatatype node) {
        handleDefault(node);
    }

    @Override
    public void visit(OWLDataOneOf node) {
        handleDefault(node);

    }

    @Override
    public void visit(OWLDataComplementOf node) {
        handleDefault(node);

    }

    @Override
    public void visit(OWLDataIntersectionOf node) {
        handleDefault(node);

    }

    @Override
    public void visit(OWLDataUnionOf node) {
        handleDefault(node);

    }

    @Override
    public void visit(OWLDatatypeRestriction node) {
        handleDefault(node);

    }
}
