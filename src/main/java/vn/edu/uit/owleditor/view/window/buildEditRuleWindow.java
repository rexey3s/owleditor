package vn.edu.uit.owleditor.view.window;

import vn.edu.uit.owleditor.ui.OWLEditorUI;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.core.OWLEditorSWRLAPIRuleRenderer;
import vn.edu.uit.owleditor.data.property.SWRLAPIRuleSource;
import vn.edu.uit.owleditor.event.OWLExpressionUpdateHandler;
import vn.edu.uit.owleditor.view.component.SWRLRuleEditor;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;
import org.swrlapi.core.SWRLAPIRenderer;
import org.swrlapi.core.SWRLAPIRule;
import org.swrlapi.parser.SWRLParseException;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/10/14.
 */
public class buildEditRuleWindow extends AbstractOWLExpressionEditorWindow<SWRLAPIRule> {
    private final SWRLRuleEditor editor;
    private final SWRLAPIRenderer myRenderer;
    public buildEditRuleWindow(@Nonnull OWLEditorKit eKit,
                               @Nonnull SWRLAPIRuleSource source, @Nonnull OWLExpressionUpdateHandler<SWRLAPIRule> mod) throws NullPointerException {
        super(eKit, source, mod);

        editor = new SWRLRuleEditor(editorKit);
        myRenderer = new OWLEditorSWRLAPIRuleRenderer(editorKit.getSWRLActiveOntology());
        editor.setValue(myRenderer.renderSWRLRule(source.getValue()));
        editor.setRuleName(source.getValue().getRuleName());
        editor.setRuleComment(source.getValue().getComment());
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
                    OWLEditorUI.getEventBus().post(modifyExpression.modifyingExpression(rule));
                    close();
                } catch (SWRLParseException ex) {
                    Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
                }

            }
        };
    }
}
