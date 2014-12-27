package vn.edu.uit.owleditor.data.property;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;

import javax.annotation.Nonnull;

public final class OWLNamedIndividualSource implements OWLLogicalEntitySource<OWLNamedIndividual> {

    private boolean readOnly = false;

    private OWLNamedIndividual owlNamedIndividual;

    public OWLNamedIndividualSource() {
    }

    public OWLNamedIndividualSource(@Nonnull OWLNamedIndividual namedIndividual) {
        this.owlNamedIndividual = namedIndividual;
    }

    @Override
    public OWLNamedIndividual getValue() {
        return owlNamedIndividual;
    }

    @Override
    public void setValue(OWLNamedIndividual newValue) throws ReadOnlyException {
        if (readOnly) {
            throw new ReadOnlyException("Read-only OWLNamedIndividual data source");
        }
        this.owlNamedIndividual = newValue;
    }

    @Override
    public Class<? extends OWLNamedIndividual> getType() {
        return OWLNamedIndividual.class;
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
        return String.valueOf(OWLEditorKitImpl.getShortForm(owlNamedIndividual));
    }
}
