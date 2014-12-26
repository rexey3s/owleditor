package vn.edu.uit.owleditor.view.window;

import com.vaadin.data.Validator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;
import org.swrlapi.core.SWRLAPIRule;
import org.swrlapi.parser.SWRLParseException;
import vn.edu.uit.owleditor.OWLEditorUI;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.event.OWLExpressionAddHandler;
import vn.edu.uit.owleditor.view.component.SWRLRuleEditor;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/8/2014.
 */
public class buildAddRuleWindow extends AbstractOWLExpressionEditorWindow<SWRLAPIRule> {

    private final SWRLRuleEditor editor;

    public buildAddRuleWindow(@Nonnull OWLEditorKit eKit, OWLExpressionAddHandler<SWRLAPIRule> adder) {
        super(eKit, adder);
        editor = new SWRLRuleEditor(editorKit);
        addTabStyle(ValoTheme.TABSHEET_EQUAL_WIDTH_TABS);
        setCaption(null);
        addMoreTab(editor, "SWRL Rule Editor");
    }

    @Override
    protected Button.ClickListener initSaveListener() {
        return clicked -> {
            if (getSelectedTab() instanceof SWRLRuleEditor) {
                try {
                    SWRLAPIRule rule = editorKit.getSWRLActiveOntology()
                            .createSWRLRule(editor.getRuleName(),
                                    String.valueOf(editor.getValue()), editor.getRuleComment(), true);
                    OWLEditorUI.getGuavaEventBus().post(addExpression.addingExpression(rule));
                    close();
                } catch (SWRLParseException ex) {
                    Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
                } catch (Validator.InvalidValueException valueEx) {
                    Notification.show(valueEx.getMessage(), Notification.Type.WARNING_MESSAGE);
                }

            }
        };
    }
}
