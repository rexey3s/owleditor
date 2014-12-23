package vn.edu.uit.owleditor.data.list;

import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.utils.OWLEditorData;
import com.vaadin.data.util.IndexedContainer;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/1/2014.
 */
public class OWL2DatatypeContainer extends IndexedContainer {
    private final OWLDataFactory factory = OWLManager.getOWLDataFactory();

    public OWL2DatatypeContainer() {
        addContainerProperty(OWLEditorData.OWL2BuiltInDataType, String.class, "any");
        initialise();

    }

    @SuppressWarnings({"unchecked"})
    private void initialise() {
        OWL2Datatype.getDatatypeIRIs().forEach(iri -> {
            OWLDatatype datatype = factory.getOWLDatatype(iri);
            addItem(datatype);
            getContainerProperty(datatype, OWLEditorData.OWL2BuiltInDataType).setValue(sf(datatype));
        });
    }

    private String sf(OWLDatatype owlDatatype) {
        return OWLEditorKit.getShortForm(owlDatatype);
    }
}

