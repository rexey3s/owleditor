package vn.edu.uit.owleditor.view.window;

import vn.edu.uit.owleditor.ui.OWLEditorUI;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.event.OWLExpressionAddHandler;
import vn.edu.uit.owleditor.view.component.ClassExpressionEditor;
import vn.edu.uit.owleditor.view.component.ObjectRestrictionCreator;
import vn.edu.uit.owleditor.view.panel.ClassHierarchicalPanel;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntaxParserException;
import org.semanticweb.owlapi.model.OWLClassExpression;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/5/2014.
 */
public class buildAddClassExpressionWindow extends AbstractOWLExpressionEditorWindow<OWLClassExpression> {

    private final ClassExpressionEditor editor;
    private final ClassHierarchicalPanel hierarchy;
    private final ObjectRestrictionCreator restrictionEditor;

    public buildAddClassExpressionWindow(@Nonnull OWLEditorKit eKit,
                                         @Nonnull OWLExpressionAddHandler<OWLClassExpression> addExpression) {
        super(eKit, addExpression);
        editor = new ClassExpressionEditor(editorKit);
        hierarchy = new ClassHierarchicalPanel(editorKit);
        restrictionEditor = new ObjectRestrictionCreator(editorKit);
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
                    editorKit.getParser().setStringToParse(String.valueOf(editor.getValue()));
                    OWLClassExpression ce = editorKit.getParser().parseClassExpression();
                    OWLEditorUI.getEventBus().post(addExpression.addingExpression(ce));
                    close();
                } catch (ManchesterOWLSyntaxParserException ex) {
                    Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
                }

            } else if (getSelectedTab() instanceof ObjectRestrictionCreator) {
                try {
                    OWLClassExpression ce = restrictionEditor.getDataProperty().getValue();
                    OWLEditorUI.getEventBus().post(addExpression.addingExpression(ce));
                    close();
                } catch (NullPointerException ex) {
                    Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
                }

            } else if (getSelectedTab() instanceof ClassHierarchicalPanel) {
                OWLEditorUI.getEventBus().post(addExpression
                        .addingExpression(hierarchy.getSelectedProperty().getValue()));
                close();
            }
        };
    }
}