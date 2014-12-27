package vn.edu.uit.owleditor.data.property;

import org.semanticweb.owlapi.model.OWLObjectProperty;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;

import javax.annotation.Nonnull;

/**
 * Created by Chuong Dang on 11/21/14.
 */
public final class OWLObjectPropertySource implements OWLPropertySource<OWLObjectProperty> {
    private boolean readOnly = false;

    private OWLObjectProperty objectProperty;

    public OWLObjectPropertySource() {
    }

    public OWLObjectPropertySource(@Nonnull OWLObjectProperty prop) {
        objectProperty = prop;
    }

    @Override
    public OWLObjectProperty getValue() {
        return objectProperty;
    }

    @Override
    public void setValue(OWLObjectProperty newValue) throws ReadOnlyException {
        if (readOnly) {
            throw new ReadOnlyException("Read-only OWLObjectProperty data source");
        }
        this.objectProperty = newValue;
    }

    @Override
    public Class<? extends OWLObjectProperty> getType() {
        return OWLObjectProperty.class;
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public void setReadOnly(boolean newStatus) {
        readOnly = newStatus;
    }

    @Override
    public String toString() {
        return String.valueOf(OWLEditorKitImpl.getShortForm(objectProperty));
    }
}
