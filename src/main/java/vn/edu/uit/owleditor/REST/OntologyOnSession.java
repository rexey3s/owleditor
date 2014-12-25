package vn.edu.uit.owleditor.REST;

import com.vaadin.ui.UI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.vaadin.spring.VaadinComponent;
import vn.edu.uit.owleditor.core.OWLEditorKit;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/25/14.
 */

@VaadinComponent
@Scope(value = "vaadin-session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OntologyOnSession {
    public OWLOntology getActiveOntology() throws NullPointerException {
        OWLEditorKit eKit = (OWLEditorKit) UI.getCurrent().getSession().getAttribute("kit");

        if (eKit == null) {
            throw new NullPointerException("Editor Kit has not been instantiate");
        }
        return eKit.getActiveOntology();
    }

}
