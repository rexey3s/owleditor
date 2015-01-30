package vn.edu.uit.owleditor.view.window;

import com.vaadin.data.Validator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.util.mansyntax.ManchesterOWLSyntaxParser;
import vn.edu.uit.owleditor.event.OWLExpressionAddHandler;
import vn.edu.uit.owleditor.view.component.DLSafeRuleEditor;
import vn.edu.uit.owleditor.view.component.SWRLRuleEditor;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 1/30/15.
 */
public class buildAddDLSafeRuleWindow extends AbstractOWLExpressionEditorWindow<SWRLRule> {
    private final DLSafeRuleEditor dlSafeRuleEditor;

    public buildAddDLSafeRuleWindow(OWLExpressionAddHandler<SWRLRule> ruleAdder) {
        super(ruleAdder);
        dlSafeRuleEditor = new DLSafeRuleEditor();
        addTabStyle(ValoTheme.TABSHEET_EQUAL_WIDTH_TABS);
        setCaption(null);
        addMoreTab(dlSafeRuleEditor, "SWRL Rule Editor");
    }

    @Override
    protected Button.ClickListener initSaveListener() {
        return click -> {
            if (getSelectedTab() instanceof SWRLRuleEditor) {
                try {
                    ManchesterOWLSyntaxParser parser = editorKit.getParser();

//                    OWLEditorEventBus.post(addExpression.addingExpression(rule));
                    close();

                } catch (Validator.InvalidValueException valueEx) {
                    Notification.show(valueEx.getMessage(), Notification.Type.WARNING_MESSAGE);
                }

            }
        };
    }
}
