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
import org.vaadin.spring.servlet.SpringAwareVaadinServlet;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.event.OWLEditorEventBus;
import vn.edu.uit.owleditor.view.EntryView;

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
    SpringAwareVaadinServlet springAwareVaadinServlet;
    @Autowired
    private HttpSession httpSession;

    public static OWLEditorEventBus getGuavaEventBus() {
        return ((OWLEditorUI) getCurrent()).editorEventBus;
    }

    public static OWLEditorKit getEditorKit() {
        return ((OWLEditorUI) UI.getCurrent()).editorKit;
    }


    @Override
    protected void init(VaadinRequest request) {
        setContent(new EntryView());
        LOG.info("Here are things VaadinRequest WrapSession");
        request.getWrappedSession().getAttributeNames()
                .forEach(LOG::info);

        LOG.info("SpringAwareVaadinServlet Context -> " + springAwareVaadinServlet.getServletContext());
        LOG.info("HttpSession Servlet Context -> " + httpSession.getServletContext());
        LOG.info("This VaadinRequest SessionId -> " + request.getWrappedSession().getId());
        LOG.info("HttpSession Id -> " + httpSession.getId());
        LOG.info("Session Attr EditorKit in HttpSession -> " + httpSession.getAttribute("OWLEditorKit"));
        LOG.info("Session Attr EditorKit in VaadinRequestSession -> " + request.getWrappedSession().getAttribute("OWLEditorKit"));
    }


}
