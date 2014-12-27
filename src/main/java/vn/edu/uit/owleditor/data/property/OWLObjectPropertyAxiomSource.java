package vn.edu.uit.owleditor.data.property;

import com.vaadin.data.Property;
import org.semanticweb.owlapi.model.OWLObjectPropertyAxiom;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/25/2014.
 */
public final class OWLObjectPropertyAxiomSource implements Property<OWLObjectPropertyAxiom> {
    private boolean readOnly = false;

    private OWLObjectPropertyAxiom objectPropertyAx;

    public OWLObjectPropertyAxiomSource() {
    }

    public OWLObjectPropertyAxiomSource(@Nonnull OWLObjectPropertyAxiom propEx) {
        objectPropertyAx = propEx;
    }

    @Override
    public OWLObjectPropertyAxiom getValue() {
        return objectPropertyAx;
    }

    @Override
    public void setValue(OWLObjectPropertyAxiom newValue) throws ReadOnlyException {
        if (readOnly) {
            throw new ReadOnlyException("Read-only " + this.getType());
        }
        this.objectPropertyAx = newValue;
    }

    @Override
    public Class<? extends OWLObjectPropertyAxiom> getType() {
        return OWLObjectPropertyAxiom.class;
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
        return String.valueOf(OWLEditorKitImpl.render(objectPropertyAx));
    }
}
