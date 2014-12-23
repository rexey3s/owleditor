package vn.edu.uit.owleditor.view.window;

import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.data.list.OWL2DatatypeContainer;
import vn.edu.uit.owleditor.utils.EditorUtils;
import vn.edu.uit.owleditor.view.panel.DataPropertyHierarchicalPanel;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/30/2014.
 */
public abstract class DataPropertyAssertionEditorWindow extends Window {

    private final OWLEditorKit editorKit;

    private final DataPropertyHierarchicalPanel hierarchy;

    private final TextArea lexicalValue = new TextArea();

    private final ComboBox dataType = new ComboBox();

    private final VerticalLayout root = new VerticalLayout();


    public DataPropertyAssertionEditorWindow(@Nonnull OWLEditorKit eKit) {
        this.editorKit = eKit;
        hierarchy = new DataPropertyHierarchicalPanel(editorKit);
        hierarchy.setCaption("DataProperty");
        dataType.setContainerDataSource(new OWL2DatatypeContainer());
        initialise();
    }

    private void initialise() {
        setModal(true);
        setClosable(false);
        setResizable(false);
        setWidth(600.0f, Unit.PIXELS);
        setHeight(600.0f, Unit.PIXELS);
        setCaption("Data Property Assertions Editor");
        VerticalLayout leftWrapper = new VerticalLayout();
        lexicalValue.setSizeFull();
        dataType.setWidth("100%");
        leftWrapper.addComponent(lexicalValue);
        leftWrapper.addComponent(dataType);
        leftWrapper.setExpandRatio(lexicalValue, 1);
//        leftWrapper.setExpandRatio(dataType, 1);
        leftWrapper.addStyleName(ValoTheme.LAYOUT_CARD);
        leftWrapper.setSizeFull();
        final HorizontalSplitPanel main = new HorizontalSplitPanel();

        main.setFirstComponent(hierarchy);
        main.setSecondComponent(leftWrapper);
        root.setHeight(100.0f, Unit.PERCENTAGE);
        root.addStyleName(ValoTheme.SPLITPANEL_LARGE);
        root.addComponent(main);
        root.addComponent(buildFooter());
        root.setExpandRatio(main, 1.0f);
        root.setSizeFull();
        setContent(root);

    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.setSpacing(true);
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);

        Button cancel = new Button("Cancel");
        cancel.addClickListener(event -> close());
        cancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE, null);

        Button save = new Button("Save");
        save.addStyleName(ValoTheme.BUTTON_PRIMARY);

        save.addClickListener(initSaveListener());

        footer.addComponents(cancel, save);
        footer.setExpandRatio(cancel, 1);
        footer.setComponentAlignment(cancel, Alignment.TOP_RIGHT);
        return footer;
    }

    public OWLLiteral getOWLLiteral() {
        EditorUtils.checkNotNull((OWLDatatype) dataType.getValue(), "Please select a OWL2Datatype");
        return editorKit.getOWLDataFactory()
                .getOWLLiteral(lexicalValue.getValue(), (OWLDatatype) dataType.getValue());
    }

    public OWLDataPropertyExpression getSelectedDataProperty() {
        EditorUtils.checkNotNull(hierarchy.getSelectedProperty().getValue(), "Please select a Data Property");
        return hierarchy.getSelectedProperty().getValue();
    }

    abstract Button.ClickListener initSaveListener();
}
