package vn.edu.uit.owleditor.ui;


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
import org.vaadin.spring.events.EventBus;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.event.OWLEditorEventBus;
import vn.edu.uit.owleditor.view.EntryView;


@Theme("mytheme")
@VaadinUI
public class OWLEditorUI extends UI {
    private static final String URL = "http://chuongdang.com/transport.owl";
    private final OWLEditorEventBus editorEventBus = new OWLEditorEventBus();

    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    EventBus eventBus;

    public static OWLEditorEventBus getGuavaEventBus() {
        return ((OWLEditorUI) getCurrent()).editorEventBus;
    }

    public OWLOntology getActiveOntology() {
        return ((OWLEditorKit) UI.getCurrent().getSession().getAttribute("kit")).getActiveOntology();
    }

    public OWLOntologyManager getOWLOntManager() {
        return ((OWLEditorKit) UI.getCurrent().getSession().getAttribute("kit")).getModelManager();
    }

    public OWLDataFactory getDataFactory() {
        return ((OWLEditorKit) UI.getCurrent().getSession().getAttribute("kit")).getOWLDataFactory();
    }

    public PrefixManager getPM() {
        return ((OWLEditorKit) UI.getCurrent().getSession().getAttribute("kit")).getPrefixManager();
    }

    public OWLEditorKit getEditorKit() {
        return ((OWLEditorKit) UI.getCurrent().getSession().getAttribute("kit"));
    }

    @Override
    protected void init(VaadinRequest request) {
        eventBus.publish(this, "Hello ontology ");
        setContent(new EntryView());
        
    }

    public EventBus getEventBus() {
        return this.eventBus;
    }
}
