package vn.edu.uit.owleditor.data.property;

import vn.edu.uit.owleditor.core.OWLEditorKit;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/25/2014.
 */
public final class OWLObjectPropertyExpressionSource implements OWLPropertyExpressionSource<OWLObjectPropertyExpression> {

    private boolean readOnly = false;

    private OWLObjectPropertyExpression objExpressionSource;

    public OWLObjectPropertyExpressionSource() {
    }
    public OWLObjectPropertyExpressionSource(@Nonnull OWLObjectPropertyExpression ce) {
        this.objExpressionSource = ce;
    }

    @Override
    public OWLObjectPropertyExpression getValue() {
        return this.objExpressionSource;
    }

    @Override
    public void setValue(OWLObjectPropertyExpression newValue) throws ReadOnlyException {
        if(readOnly) {
            throw new ReadOnlyException("Read-only OWLClassExpression data source");
        }
        this.objExpressionSource = newValue;
    }

    @Override
    public Class<? extends OWLObjectPropertyExpression> getType() {
        return OWLObjectPropertyExpression.class;
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
        return String.valueOf(OWLEditorKit.render(objExpressionSource));
    }
}

