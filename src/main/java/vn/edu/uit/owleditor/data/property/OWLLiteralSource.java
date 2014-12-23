package vn.edu.uit.owleditor.data.property;

import vn.edu.uit.owleditor.core.OWLEditorKit;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/11/2014.
 */
public class OWLLiteralSource implements OWLPropertyAssertionObjectSource<OWLLiteral> {
    private boolean readOnly = false;
    private OWLLiteral data;

    public OWLLiteralSource() {
    }

    public OWLLiteralSource(@Nonnull OWLLiteral data) {
        this.data = data;
    }

    @Override
    public OWLLiteral getValue() {
        return data;
    }

    @Override
    public void setValue(OWLLiteral owlLiteral) throws ReadOnlyException {
        if (readOnly) {
            throw new ReadOnlyException("Read only " + data.getClass());
        }
        this.data = owlLiteral;
    }

    @Override
    public Class<? extends OWLLiteral> getType() {
        return OWLLiteral.class;
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public void setReadOnly(boolean b) {
        readOnly = b;
    }

    @Override
    public String toString() {
        return OWLEditorKit.render(data);
    }
}
