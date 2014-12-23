package vn.edu.uit.owleditor.view.window;

import vn.edu.uit.owleditor.core.OWLEditorKit;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.SWRLRule;
import uk.ac.manchester.cs.owl.explanation.ordering.ExplanationTree;
import uk.ac.manchester.cs.owl.explanation.ordering.Tree;

import javax.annotation.Nonnull;


/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/15/14.
 */
public class ExplanationWindow extends Window {
    private final VerticalLayout root = new VerticalLayout();
    private final VerticalLayout body = new VerticalLayout();
    private final OWLEditorKit editorKit;

    public ExplanationWindow(@Nonnull OWLEditorKit editorKit, ExplanationTree explanationTree) {
        this.editorKit = editorKit;
        initialize();
        printIndented(explanationTree, "");
    }


    private void printIndented(Tree<OWLAxiom> node, String indent) {
        OWLAxiom axiom = node.getUserObject();
        body.addComponent(new ExplanationLabel(axiom));
        if (!node.isLeaf()) {
            for (Tree<OWLAxiom> child : node.getChildren()) {
                printIndented(child, indent + "    ");
            }
        }
    }

    private void initialize() {
        setModal(true);
        setClosable(false);
        setResizable(false);
        setWidth(700.0f, Unit.PIXELS);
        setHeight(500.0f, Unit.PIXELS);
        setContent(buildContent());

    }


    private Component buildContent() {
        body.setMargin(true);
        body.setSpacing(true);
        body.setWidth("100%");
        root.addStyleName(ValoTheme.LAYOUT_CARD);
        root.addComponent(body);
        root.addComponent(buildFooter());
        root.setExpandRatio(body, 1.0f);
        root.setSizeFull();
        return root;
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.setSpacing(true);
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);

        Button cancel = new Button("OK");
        cancel.addClickListener(event -> close());
        cancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE, null);


        footer.addComponents(cancel);
        footer.setComponentAlignment(cancel, Alignment.BOTTOM_RIGHT);
        return footer;
    }

    protected class ExplanationLabel extends Panel {
        private final Label label = new Label();

        public ExplanationLabel(OWLAxiom axiom) {
            if (axiom instanceof SWRLRule)
                label.setValue(editorKit.getRuleRenderer().renderSWRLRule((SWRLRule) axiom));
            else
                label.setValue(OWLEditorKit.render(axiom));

            label.addStyleName(ValoTheme.LABEL_COLORED);
            label.addStyleName(ValoTheme.LABEL_NO_MARGIN);
            label.setSizeUndefined();

            addStyleName(ValoTheme.PANEL_BORDERLESS);
            setContent(label);
            setWidth("100%");

        }
    }

}
