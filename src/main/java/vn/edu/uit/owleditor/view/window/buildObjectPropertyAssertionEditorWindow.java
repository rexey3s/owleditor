package vn.edu.uit.owleditor.view.window;

import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.event.OWLEditorEventBus;
import vn.edu.uit.owleditor.event.OWLPropertyAssertionAddHandler;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/11/2014.
 */
public class buildObjectPropertyAssertionEditorWindow extends ObjectPropertyAssertionEditorWindow {
    private final OWLPropertyAssertionAddHandler<OWLObjectPropertyExpression, OWLIndividual> adder;

    public buildObjectPropertyAssertionEditorWindow(@Nonnull OWLEditorKit eKit,
                                                    @Nonnull OWLPropertyAssertionAddHandler<OWLObjectPropertyExpression, OWLIndividual> adder) {
        super(eKit);
        this.adder = adder;
    }

    @Override
    Button.ClickListener initSaveListener() {
        return clicked -> {
            try {
                OWLEditorEventBus.post(adder.addingRestriction(
                        hierarchy.getSelectedProperty().getValue(), owlIndividualList.getSelectedProperty().getValue()
                ));
                close();
            } catch (NullPointerException nullEx) {
                Notification.show(nullEx.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        };
    }
}
