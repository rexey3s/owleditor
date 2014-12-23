package vn.edu.uit.owleditor.data.property;

import vn.edu.uit.owleditor.core.OWLEditorKit;
import org.semanticweb.owlapi.model.OWLClassExpression;

import javax.annotation.Nonnull;

/**
 * Created by Chuong Dang on 11/15/14.
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
        return String.valueOf(OWLEditorKit.render(owlClassExpressionSource));
    }
}
