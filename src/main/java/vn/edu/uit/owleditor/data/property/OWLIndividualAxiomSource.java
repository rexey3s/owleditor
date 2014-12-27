package vn.edu.uit.owleditor.data.property;

import org.semanticweb.owlapi.model.OWLIndividualAxiom;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/27/2014.
 */
public final class OWLIndividualAxiomSource implements OWLAxiomSource<OWLIndividualAxiom> {

    private boolean readOnly = false;

    private OWLIndividualAxiom axiom;

    public OWLIndividualAxiomSource() {

    }

    public OWLIndividualAxiomSource(OWLIndividualAxiom axiom) {
        this.axiom = axiom;
    }

    @Override
    public OWLIndividualAxiom getValue() {
        return axiom;
    }

    @Override
    public void setValue(OWLIndividualAxiom newValue) throws ReadOnlyException {
        if (readOnly) {
            throw new ReadOnlyException("Read-only " + this.getType());
        }
        this.axiom = newValue;
    }

    @Override
    public Class<? extends OWLIndividualAxiom> getType() {
        return OWLIndividualAxiom.class;
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
        return String.valueOf(OWLEditorKitImpl.render(axiom));
    }
}
