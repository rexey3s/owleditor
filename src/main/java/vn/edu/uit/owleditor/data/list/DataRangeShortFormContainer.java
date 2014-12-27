package vn.edu.uit.owleditor.data.list;

import com.vaadin.data.util.IndexedContainer;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;
import vn.edu.uit.owleditor.utils.OWLEditorData;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/20/14.
 */
public class DataRangeShortFormContainer extends IndexedContainer {
    private final OWLDataFactory factory = OWLManager.getOWLDataFactory();

    public DataRangeShortFormContainer() {
        addContainerProperty(OWLEditorData.OWL2BuiltInDataType, String.class, "any");
        initialise();
        addItem("{");
        addItem("}");
        addItem("\"");
        addItem("(");
        addItem(")");
        addItem("'");

    }

    @SuppressWarnings({"unchecked"})
    private void initialise() {
        OWL2Datatype.getDatatypeIRIs().forEach(iri -> {
            OWLDatatype datatype = factory.getOWLDatatype(iri);
            addItem(sf(datatype));
        });
    }

    private String sf(OWLDatatype owlDatatype) {
        return OWLEditorKitImpl.getShortForm(owlDatatype);
    }
}
