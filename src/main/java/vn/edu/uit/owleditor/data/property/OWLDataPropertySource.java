package vn.edu.uit.owleditor.data.property;

import vn.edu.uit.owleditor.core.OWLEditorKit;
import org.semanticweb.owlapi.model.OWLDataProperty;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/22/2014.
 */
public final class OWLDataPropertySource implements OWLPropertySource<OWLDataProperty> {

    private boolean readOnly = false;

    private OWLDataProperty dataProperty;

    public OWLDataPropertySource() {
    }

    public OWLDataPropertySource(@Nonnull OWLDataProperty dataProperty) {
        this.dataProperty = dataProperty;
    }

    @Override
    public OWLDataProperty getValue() {
        return dataProperty;
    }

    @Override
    public void setValue(OWLDataProperty newValue) throws ReadOnlyException {
        if (readOnly) {
            throw new ReadOnlyException("Read-only OWLDataProperty data source");
        }
        this.dataProperty = newValue;
    }

    @Override
    public Class<? extends OWLDataProperty> getType() {
        return OWLDataProperty.class;
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
        return String.valueOf(OWLEditorKit.getShortForm(dataProperty));
    }
}
