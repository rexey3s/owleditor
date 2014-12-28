package vn.edu.uit.owleditor;


import com.vaadin.annotations.Theme;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.spring.VaadinUI;
import org.vaadin.spring.navigator.SpringViewProvider;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.event.OWLEditorEventBus;
import vn.edu.uit.owleditor.view.EntryView;
import vn.edu.uit.owleditor.view.MainView;

import javax.servlet.http.HttpSession;


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

    @Autowired 
    EntryView entryView;

    @Autowired
    private HttpSession httpSession;

    public static OWLEditorEventBus getGuavaEventBus() {
        return ((OWLEditorUI) getCurrent()).editorEventBus;
    }

    public static OWLEditorKit getEditorKit() {
        return ((OWLEditorUI) UI.getCurrent()).editorKit;
    }

    public static HttpSession getHttpSession() {
        return ((OWLEditorUI) UI.getCurrent()).httpSession;
    }

    public static Component getEntryView() {
        return ((OWLEditorUI) UI.getCurrent()).entryView;
    }
    
    
    @Override
    protected void init(VaadinRequest request) {

        LOG.info("Notice: VaadinRequest WrapSession is equivalent to HttpSession");
        LOG.info("This VaadinRequest SessionId -> " + request.getWrappedSession().getId());
        LOG.info("HttpSession Id -> " + httpSession.getId());

        updateContent();
        Page.getCurrent().addBrowserWindowResizeListener(OWLEditorEventBus::post);
        
    }

    private void updateContent() {
        OWLEditorKit eKit = (OWLEditorKit) httpSession.getAttribute("OWLEditorKit");
        if (eKit != null && eKit.getActiveOntology() != null) {
            ConfirmDialog.show(this, "Do you want to load a new ontology ?", dialog -> {
                if (dialog.isConfirmed()) {
                    setContent(entryView);
                    editorKit.removeActiveOntology();

                } else {
                    setContent(new MainView());
                }
            });
        } else {
            setContent(entryView);
        }

    }


}
