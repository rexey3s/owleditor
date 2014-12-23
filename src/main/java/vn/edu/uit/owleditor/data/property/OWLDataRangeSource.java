package vn.edu.uit.owleditor.data.property;

import vn.edu.uit.owleditor.core.OWLEditorKit;
import org.semanticweb.owlapi.model.OWLDataRange;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/2/2014.
 */
public final class OWLDataRangeSource implements OWLObjectSource<OWLDataRange> {
    private boolean readOnly = false;

    private OWLDataRange dataRange;

    public OWLDataRangeSource() {
    }

    public OWLDataRangeSource(@Nonnull OWLDataRange dataRange) {
        this.dataRange = dataRange;
    }

    @Override
    public OWLDataRange getValue() {
        return dataRange;
    }

    @Override
    public void setValue(OWLDataRange newValue) throws ReadOnlyException {
        if (readOnly) {
            throw new ReadOnlyException("Read-only OWLDataProperty data source");
        }
        this.dataRange = newValue;
    }

    @Override
    public Class<? extends OWLDataRange> getType() {
        return OWLDataRange.class;
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
        return String.valueOf(OWLEditorKit.render(dataRange));
    }
}
