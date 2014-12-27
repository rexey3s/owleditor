package vn.edu.uit.owleditor;


import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.vaadin.spring.VaadinUI;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;
import vn.edu.uit.owleditor.event.OWLEditorEventBus;
import vn.edu.uit.owleditor.view.EntryView;


@Theme("mytheme")
@VaadinUI
public class OWLEditorUI extends UI {
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    OWLEditorEventBus editorEventBus;

    @Autowired
    OWLEditorKit editorKit;
    
    public static OWLEditorEventBus getGuavaEventBus() {
        return ((OWLEditorUI) getCurrent()).editorEventBus;
    }

    public static OWLEditorKit getEditorKit() {
        return ((OWLEditorUI) UI.getCurrent()).editorKit;
    }

    public OWLOntology getActiveOntology() {
        return ((OWLEditorKitImpl) UI.getCurrent().getSession().getAttribute("kit")).getActiveOntology();
    }

    public OWLOntologyManager getOWLOntManager() {
        return ((OWLEditorKitImpl) UI.getCurrent().getSession().getAttribute("kit")).getModelManager();
    }

    public OWLDataFactory getDataFactory() {
        return ((OWLEditorKitImpl) UI.getCurrent().getSession().getAttribute("kit")).getOWLDataFactory();
    }

    public PrefixManager getPM() {
        return ((OWLEditorKitImpl) UI.getCurrent().getSession().getAttribute("kit")).getPrefixManager();
    }

    @Override
    protected void init(VaadinRequest request) {
        setContent(new EntryView());
    }


}
