package vn.edu.uit.owleditor.view.component;

import vn.edu.uit.owleditor.data.property.OWLIndividualAxiomSource;
import vn.edu.uit.owleditor.event.OWLExpressionRemoveHandler;
import org.semanticweb.owlapi.model.OWLIndividualAxiom;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/11/2014.
 */
public class OWLIndividualAxiomLabel extends AbstractNonEditableOWLObjectLabel<OWLIndividualAxiom> {

    public OWLIndividualAxiomLabel(@Nonnull OWLIndividualAxiomSource expressionSource, OWLExpressionRemoveHandler removeExpression) {
        super(expressionSource, removeExpression);
    }
}
