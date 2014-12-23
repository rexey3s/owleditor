package vn.edu.uit.owleditor.data.list;

import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.utils.OWLEditorData;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/26/2014.
 */
public class OWLEntitiesShortFormContainer extends IndexedContainer {
    private final OWLOntology ontology;
    @SuppressWarnings({"unchecked"})
    public OWLEntitiesShortFormContainer(@Nonnull OWLEditorKit editorKit) {
        ontology = editorKit.getActiveOntology();
        addContainerProperty(OWLEditorData.OWLEntityIcon, Resource.class, null);
        ontology.getClassesInSignature().forEach(c -> {
            String sf = OWLEditorKit.getShortForm(c);
            addItem(sf);
            getContainerProperty(sf, OWLEditorData.OWLEntityIcon)
                    .setValue(new ThemeResource("../owleditor/class.png"));
        });
        ontology.getObjectPropertiesInSignature().forEach(o -> {
            String sf = OWLEditorKit.getShortForm(o);
            addItem(sf);
            getContainerProperty(sf, OWLEditorData.OWLEntityIcon)
                    .setValue(new ThemeResource("../owleditor/class.png"));
        });
        ontology.getDataPropertiesInSignature().forEach(d -> {
            String sf = OWLEditorKit.getShortForm(d);
            addItem(sf);
            getContainerProperty(sf, OWLEditorData.OWLEntityIcon)
                    .setValue(new ThemeResource("../owleditor/class.png"));
        });
        ontology.getIndividualsInSignature().forEach(i -> {
            String sf = OWLEditorKit.getShortForm(i);
            addItem(sf);
            getContainerProperty(sf, OWLEditorData.OWLEntityIcon)
                    .setValue(new ThemeResource("../owleditor/individual.png"));
        });
        addItem("and");
        addItem("or");
        addItem("not");
        addItem("(");
        addItem(')');
        addItem("{");
        addItem("}");
        addItem("some");
        addItem("min");
        addItem("max");
        addItem("only");
        addItem("exact");
    }
}
