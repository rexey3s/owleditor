package vn.edu.uit.owleditor.data.list;

import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.utils.OWLEditorData;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.Resource;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.swrlapi.core.SWRLAPIOWLOntology;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/9/2014.
 */
public class SWRLAtomShortFormContainer extends IndexedContainer {
    private final SWRLAPIOWLOntology ontology;

    @SuppressWarnings({"unchecked"})
    public SWRLAtomShortFormContainer(@Nonnull OWLEditorKit editorKit) {
        ontology = editorKit.getSWRLActiveOntology();

        DefaultPrefixManager prefixManager = ontology.getPrefixManager();
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
