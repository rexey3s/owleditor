package vn.edu.uit.owleditor.view.window;

import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLLiteral;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.event.OWLEditorEventBus;
import vn.edu.uit.owleditor.event.OWLPropertyAssertionAddHandler;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/11/2014.
 */
public class buildAddDataPropertyAssertionEditorWindow extends DataPropertyAssertionEditorWindow {
    private final OWLPropertyAssertionAddHandler<OWLDataPropertyExpression, OWLLiteral> adder;

    public buildAddDataPropertyAssertionEditorWindow(@Nonnull OWLEditorKit eKit, @Nonnull OWLPropertyAssertionAddHandler<OWLDataPropertyExpression, OWLLiteral> adder) {
        super(eKit);
        this.adder = adder;
    }

    @Override
    Button.ClickListener initSaveListener() {
        return clicked -> {
            try {
                OWLDataPropertyExpression expression = getSelectedDataProperty();
                OWLLiteral restriction = getOWLLiteral();
                OWLEditorEventBus.post(adder.addingRestriction(expression, restriction));
                close();
            } catch (Exception ex) {
                Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        };
    }
}
