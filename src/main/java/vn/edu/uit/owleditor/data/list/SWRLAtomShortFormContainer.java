package vn.edu.uit.owleditor.data.list;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.Resource;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.swrlapi.core.SWRLAPIOWLOntology;
import vn.edu.uit.owleditor.OWLEditorUI;
import vn.edu.uit.owleditor.utils.OWLEditorData;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/9/2014.
 */
public class SWRLAtomShortFormContainer extends IndexedContainer {

    @SuppressWarnings({"unchecked"})
    public SWRLAtomShortFormContainer() {
        SWRLAPIOWLOntology ontology = OWLEditorUI.getEditorKit().getSWRLActiveOntology();

        DefaultPrefixManager prefixManager = (DefaultPrefixManager) OWLEditorUI.getEditorKit().getPrefixManager();
        addContainerProperty(OWLEditorData.OWLEntityIcon, Resource.class, null);
        ontology.getOWLOntology().getSignature(Imports.INCLUDED).forEach(e -> {
            String shortForm = prefixManager.getShortForm(e.getIRI());
            if (shortForm.startsWith(":"))
                addItem(shortForm.substring(1));
            else
            addItem(shortForm);
        });
        for (IRI swrlBuiltInIRI : ontology.getSWRLBuiltInIRIs()) {
            String shortForm = prefixManager.getShortForm(swrlBuiltInIRI);
            if (shortForm.startsWith(":"))
                addItem(shortForm.substring(1));
            else
            addItem(shortForm);
        }

        for (OWLRDFVocabulary v : OWLRDFVocabulary.values()) {
            String shortForm = v.getPrefixedName();
            addItem(shortForm);
        }
        addItem("^");
        addItem("(");
        addItem(")");
        addItem("sameAs");
        addItem("differentFrom");

    }
}
