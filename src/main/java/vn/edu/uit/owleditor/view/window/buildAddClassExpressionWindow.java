package vn.edu.uit.owleditor.view.window;

import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;
import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.model.OWLClassExpression;
import vn.edu.uit.owleditor.event.OWLEditorEventBus;
import vn.edu.uit.owleditor.event.OWLExpressionAddHandler;
import vn.edu.uit.owleditor.view.component.ClassExpressionEditor;
import vn.edu.uit.owleditor.view.component.ObjectRestrictionCreator;
import vn.edu.uit.owleditor.view.panel.ClassHierarchicalPanel;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/5/2014.
 */
public class buildAddClassExpressionWindow extends AbstractOWLExpressionEditorWindow<OWLClassExpression> {

    private final ClassExpressionEditor editor;
    private final ClassHierarchicalPanel hierarchy;
    private final ObjectRestrictionCreator restrictionEditor;

    public buildAddClassExpressionWindow(@Nonnull OWLExpressionAddHandler<OWLClassExpression> addExpression) {
        super(addExpression);
        editor = new ClassExpressionEditor();
        hierarchy = new ClassHierarchicalPanel();
        restrictionEditor = new ObjectRestrictionCreator();
        addTabStyle(ValoTheme.TABSHEET_COMPACT_TABBAR);
        addMoreTab(editor, "Expression Editor");
        addMoreTab(hierarchy, "Choose a class");
        addMoreTab(restrictionEditor, "Object Restriction Editor");
    }


    @Override
    protected Button.ClickListener initSaveListener() {
        return click -> {
            if (getSelectedTab() instanceof ClassExpressionEditor) {
                try {
//                    editorKit.getParser().setStringToParse(String.valueOf(editor.getValue()));
                    OWLClassExpression ce = editorKit.parseClassExpression(String.valueOf(editor.getValue()));
                    OWLEditorEventBus.post(addExpression.addingExpression(ce));
                    close();
                } catch (OWLParserException ex) {
                    Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
                }

            } else if (getSelectedTab() instanceof ObjectRestrictionCreator) {
                try {
                    OWLClassExpression ce = restrictionEditor.getDataProperty().getValue();
                    OWLEditorEventBus.post(addExpression.addingExpression(ce));
                    close();
                } catch (NullPointerException ex) {
                    Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
                }

            } else if (getSelectedTab() instanceof ClassHierarchicalPanel) {
                OWLEditorEventBus.post(addExpression
                        .addingExpression(hierarchy.getSelectedProperty().getValue()));
                close();
            }
        };
    }
}