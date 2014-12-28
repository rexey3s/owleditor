package vn.edu.uit.owleditor.data.property;

import org.semanticweb.owlapi.model.OWLClass;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;

import javax.annotation.Nonnull;


/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/16/14.
 */
public final class OWLClassSource implements OWLLogicalEntitySource<OWLClass> {

    private boolean readOnly = false;

    private OWLClass owlClass;

    public OWLClassSource() {
    }

    public OWLClassSource(@Nonnull OWLClass cls)
    {
        this.owlClass = cls;
    }

    @Override
    public OWLClass getValue() {
        return owlClass;
    }

    @Override
    public void setValue(OWLClass newValue) throws ReadOnlyException {
        if(readOnly) {
            throw new ReadOnlyException("Read-only OWLClass data source");
        }
        this.owlClass = newValue;
    }

    @Override
    public Class<? extends OWLClass> getType() {
        return OWLClass.class;
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
        return String.valueOf(OWLEditorKitImpl.getShortForm(owlClass));
    }
}
