package vn.edu.uit.owleditor.data.property;

import org.semanticweb.owlapi.model.OWLClassExpression;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 11/15/14.
 */
public final class OWLClassExpressionSource implements OWLObjectSource<OWLClassExpression> {

    private boolean readOnly = false;

    private OWLClassExpression owlClassExpressionSource;

    public OWLClassExpressionSource() {
    }

    public OWLClassExpressionSource(@Nonnull OWLClassExpression ce) {
       this.owlClassExpressionSource = ce;
    }

    @Override
    public OWLClassExpression getValue() {
        return this.owlClassExpressionSource;
    }

    @Override
    public void setValue(OWLClassExpression newValue) throws ReadOnlyException {
        if(readOnly) {
            throw new ReadOnlyException("Read-only OWLClassExpression data source");
        }
        this.owlClassExpressionSource = newValue;
    }

    @Override
    public Class<? extends OWLClassExpression> getType() {
        return OWLClassExpression.class;
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
        return String.valueOf(OWLEditorKitImpl.render(owlClassExpressionSource));
    }
}
