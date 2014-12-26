package vn.edu.uit.owleditor.view.component;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.semanticweb.owlapi.model.OWLObject;
import vn.edu.uit.owleditor.OWLEditorUI;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.event.ExplanationHandler;
import vn.edu.uit.owleditor.view.window.ExplanationWindow;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/8/2014.
 */
public class InferredLabel extends HorizontalLayout {
    final OWLEditorKit editorKit;
    private final Label label = new Label();
    private final ExplanationHandler explanationGetter;

    public InferredLabel(@Nonnull OWLObject owlObject, @Nonnull ExplanationHandler explanationHandler) {
        editorKit = ((OWLEditorUI) UI.getCurrent()).getEditorKit();
        label.setValue(OWLEditorKit.render(owlObject));
        explanationGetter = explanationHandler;
        initialise();
    }

    private void initialise() {
        label.addStyleName(ValoTheme.LABEL_COLORED);
        label.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        label.setSizeUndefined();

        Panel lbPanel = new Panel();
        lbPanel.setWidth("100%");
        lbPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        lbPanel.addStyleName("expression-label");
        lbPanel.setContent(label);
        addStyleName("v-inferred-label");
        addComponents(lbPanel, buildToolBar());
        setExpandRatio(lbPanel, 1);
        setComponentAlignment(lbPanel, Alignment.MIDDLE_LEFT);
        setWidth(100.0f, Unit.PERCENTAGE);
    }

    private Component buildToolBar() {
        final MenuBar tools = new MenuBar();
        tools.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        tools.addStyleName("icon-only");
        tools.addItem("", FontAwesome.QUESTION_CIRCLE, clicked -> {
            Notification.show("Generating explanations", Notification.Type.TRAY_NOTIFICATION);
            try {
                UI.getCurrent().addWindow(new ExplanationWindow(editorKit, explanationGetter.onGenerateExplanation()));
            } catch (NullPointerException nullEx) {
                Notification.show(nullEx.getMessage(), Notification.Type.WARNING_MESSAGE);
            }
        });

        return tools;
    }


}