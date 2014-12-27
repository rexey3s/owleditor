package vn.edu.uit.owleditor;


import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.vaadin.spring.VaadinUI;
import org.vaadin.spring.navigator.SpringViewProvider;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.event.OWLEditorEventBus;
import vn.edu.uit.owleditor.view.EntryView;


@Theme("mytheme")
@VaadinUI
public class OWLEditorUI extends UI {
    private static final Logger LOG = LoggerFactory.getLogger(OWLEditorUI.class);
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    OWLEditorEventBus editorEventBus;

    @Autowired
    OWLEditorKit editorKit;

    @Autowired
    SpringViewProvider viewProvider;


    public static OWLEditorEventBus getGuavaEventBus() {
        return ((OWLEditorUI) getCurrent()).editorEventBus;
    }

    public static OWLEditorKit getEditorKit() {
        return ((OWLEditorUI) UI.getCurrent()).editorKit;
    }


    @Override
    protected void init(VaadinRequest request) {
        setContent(new EntryView());
        LOG.info(request.getWrappedSession().getAttributeNames().toString());
    }


}
