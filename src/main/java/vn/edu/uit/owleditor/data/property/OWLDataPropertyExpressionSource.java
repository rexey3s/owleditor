package vn.edu.uit.owleditor.data.property;

import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/30/2014.
 */
public final class OWLDataPropertyExpressionSource implements OWLPropertyExpressionSource<OWLDataPropertyExpression> {

    private boolean readOnly = false;

    private OWLDataPropertyExpression dataPropertyExpression;

    public OWLDataPropertyExpressionSource() {
    }

    public OWLDataPropertyExpressionSource(@Nonnull OWLDataPropertyExpression de) {
        this.dataPropertyExpression = de;
    }

    @Override
    public OWLDataPropertyExpression getValue() {
        return this.dataPropertyExpression;
    }

    @Override
    public void setValue(OWLDataPropertyExpression newValue) throws ReadOnlyException {
        if (readOnly) {
            throw new ReadOnlyException("Read-only OWLClassExpression data source");
        }
        this.dataPropertyExpression = newValue;
    }

    @Override
    public Class<? extends OWLDataPropertyExpression> getType() {
        return OWLDataPropertyExpression.class;
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public void setReadOnly(boolean newStatus) {
        this.readOnly = newStatus;
    }

    @Override
    public String toString() {
        return String.valueOf(OWLEditorKitImpl.render(dataPropertyExpression));
    }

}
