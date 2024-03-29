package vn.edu.uit.owleditor;


import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.vaadin.dialogs.ConfirmDialog;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.event.OWLEditorEventBus;
import vn.edu.uit.owleditor.view.EntryView;
import vn.edu.uit.owleditor.view.MainView;

import javax.servlet.http.HttpSession;


@Theme("mytheme")
@SpringUI
public class OWLEditorUI extends UI {
    private static final Logger LOG = LoggerFactory.getLogger(OWLEditorUI.class);
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    OWLEditorEventBus editorEventBus;

    @Autowired
    OWLEditorKit editorKit;

    @Autowired 
    EntryView entryView;

    @Autowired
    private HttpSession httpSession;

    public static OWLEditorEventBus getGuavaEventBus() {
        return ((OWLEditorUI) getCurrent()).editorEventBus;
    }

    public static OWLEditorKit getEditorKit() {
        return ((OWLEditorUI) getCurrent()).eKit;
    }

    private OWLEditorKit eKit;

    public static HttpSession getHttpSession() {
        return ((OWLEditorUI) getCurrent()).httpSession;
    }


    @Override
    protected void init(VaadinRequest request) {

        LOG.info("Note: VaadinRequest WrapSession is equivalent to HttpSession");
        LOG.info("This VaadinRequest SessionId -> " + request.getWrappedSession().getId());
        LOG.info("HttpSessionId -> " + httpSession.getId());
        eKit = editorKit;
        updateContent();

    }

    private void updateContent() {
        OWLEditorKit eKit = (OWLEditorKit) httpSession.getAttribute("OWLEditorKit");

        if (eKit != null && eKit.getActiveOntology() != null) {
            ConfirmDialog.show(this, "Do you want to load a new ontology ?", dialog -> {
                if (dialog.isConfirmed()) {
                    setContent(entryView);
                    eKit.removeAllOntologies();
                    eKit.removeActiveOntology();

                } else {
                    this.eKit = eKit;
                    setContent(new MainView());
                }
            });
        } else {
            setContent(entryView);
        }

    }


}
